package com.eightpeak.salakafarm.views.addtocart.addtocartfragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.FragmentAddToCartBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.GetResponseViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.products.Data
import com.eightpeak.salakafarm.views.home.products.ProductAdapter
import com.google.android.material.snackbar.Snackbar
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import kotlinx.android.synthetic.main.fragment_add_to_cart.*

import android.view.*
import android.widget.*
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.eightpeak.salakafarm.views.order.orderview.viewordercheckoutdetails.CheckoutDetailsView


class ViewCartFragment : Fragment() {
    private lateinit var viewModel: GetResponseViewModel
    private var _binding: FragmentAddToCartBinding? = null
    private var tokenManager: TokenManager? = null
    private lateinit var binding: FragmentAddToCartBinding
    private  var quantity: Int = 0

    lateinit var productAdapter: ProductAdapter
    private var layoutManager: GridLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddToCartBinding.inflate(layoutInflater, parent, false)
        tokenManager = TokenManager.getInstance(
            requireActivity().getSharedPreferences(
                Constants.TOKEN_PREF,
                AppCompatActivity.MODE_PRIVATE
            )
        )
         init()

        binding.headerLayout.text=getString(R.string.cart)
        binding.proceedWithCheckout.setOnClickListener {
            startActivity(Intent(requireContext(), CheckoutDetailsView::class.java))
        }
        binding.continueShoppingEmpty.setOnClickListener {
            startActivity(Intent(requireContext(), HomeActivity::class.java))
             }
        return binding.buttomAddToCart
    }
    private fun init() {
        productAdapter = ProductAdapter()
        layoutManager = GridLayoutManager(requireContext(), 2)
        binding.productRecyclerView.layoutManager = layoutManager
        binding.productRecyclerView.setHasFixedSize(true)
        binding.productRecyclerView.isFocusable = false
        binding.productRecyclerView.adapter = productAdapter
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(GetResponseViewModel::class.java)
        getPictures()
        getRandomProducts()
    }

    private fun getPictures() {
        tokenManager?.let { it1 -> viewModel.getCartResponse(it1) }

        viewModel.cartResponse.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        getSelectedProducts(picsResponse.cart)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
//                        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT)
                      binding.buttomAddToCart.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun getSelectedProducts(cartResponse: List<Cart>) {
        binding.viewCartList.removeAllViews()
        if(cartResponse.isNotEmpty()){
            binding.ifEmpty.visibility=View.GONE
            for (i in cartResponse.indices) {
                val itemView: View =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.cart_item, binding.viewCartList, false)

                val categorySKU = itemView.findViewById<TextView>(R.id.product_sku)
                val productThumbnail = itemView.findViewById<ImageView>(R.id.product_thumbnail)
                val productName = itemView.findViewById<TextView>(R.id.product_name)
                val productPrice = itemView.findViewById<TextView>(R.id.product_price)
                val increaseQuantity = itemView.findViewById<ImageButton>(R.id.increase_quantity)
                val decreaseQuantity = itemView.findViewById<ImageButton>(R.id.decrease_quantity)
                val quantityView = itemView.findViewById<TextView>(R.id.product_quantity)
                val itemSelected = itemView.findViewById<ImageView>(R.id.item_selected)
                quantity=cartResponse[i].qty

                increaseQuantity.setOnClickListener { quantity=1
                    quantityView.text=quantity.toString()}
                decreaseQuantity.setOnClickListener { if(quantity>1){
                    quantity -= 1
                    quantityView.text=quantity.toString()
                }
                }
                itemSelected.setOnClickListener {
                    tokenManager?.let { it1 -> viewModel.deleteCartItemById(it1,cartResponse[i].id.toString()) }
                    observeData()
                }

                categorySKU.text = cartResponse[i].products_with_description.sku
                productName.text = cartResponse[i].products_with_description.descriptions[0].name
                productPrice.text = cartResponse[i].products_with_description.price.toString()
                quantityView.text = cartResponse[i].qty.toString()
                productThumbnail.load(EndPoints.BASE_URL + cartResponse[i].products_with_description.image)
                binding.viewCartList.addView(itemView)
            }
        }else{
            binding.ifEmpty.visibility=View.VISIBLE
        }

    }

    private fun observeData() {
        viewModel.deleteCartItem.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let { picsResponse ->
//                        finish()
//                        startActivity(intent)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.buttomAddToCart.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }
    
    private fun getRandomProducts() {
        viewModel.getRandomListResponseView()

        viewModel.getRandomListResponse.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        getRandomProductsDetails(picsResponse)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.buttomAddToCart.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }

    private fun getRandomProductsDetails(data: List<Data>) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        productAdapter.differ.submitList(data)
        binding.productRecyclerView.adapter = productAdapter
     }


    private fun hideProgressBar() {
        binding.progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progress.visibility = View.VISIBLE
    }


    fun onProgressClick(view: View) {
        //Preventing Click during loading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}