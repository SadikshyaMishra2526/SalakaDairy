package com.eightpeak.salakafarm.views.addtocart.addtocartfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.google.android.material.snackbar.Snackbar
import com.hadi.retrofitmvvm.util.errorSnack
import kotlinx.android.synthetic.main.fragment_add_to_cart.*
import kotlinx.android.synthetic.main.fragment_categories.*

class ViewCartFragment : Fragment() {
    private lateinit var viewModel: GetResponseViewModel
    private var _binding: FragmentAddToCartBinding? = null
    private var tokenManager: TokenManager? = null
    private lateinit var binding: FragmentAddToCartBinding
    private  var quantity: Int = 0


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
        setupViewModel()
        return binding.addToCart
    }


    private fun init() {
//        categoriesAdapter = CategoriesAdapter()
//        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        binding.viewCartList.layoutManager = layoutManager
//        binding.viewCartList.setHasFixedSize(true)
//        binding.viewCartList.isFocusable = false
//        binding.viewCartList.adapter = categoriesAdapter
//        binding.seeAllCategories.setOnClickListener {
//            val intent = Intent(context, CategoriesSeeAllActivity::class.java)
//            startActivity(intent)
//        }
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(GetResponseViewModel::class.java)
        getPictures()
    }

    private fun getPictures() {
        tokenManager?.let { it1 -> viewModel.getCartResponse(it1) }

        viewModel.cartResponse.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        Log.i("TAG", "getPictures: $picsResponse")
                        getSelectedProducts(picsResponse)
//                        binding.shimmerLayout.stopShimmer()
//                        binding.shimmerLayout.visibility = View.GONE
//
//                        categoriesAdapter.differ.submitList(picsResponse.data)
//                        binding.binding..adapter = categoriesAdapter
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                      binding.addToCart.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun getSelectedProducts(cartResponse: List<CartResponse>) {
        for (i in cartResponse.indices) {
            val itemView: View =
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.cart_item, binding.viewCartList, false)

            val categorySKU = itemView.findViewById<TextView>(R.id.product_sku)
            val productThumbnail = itemView.findViewById<ImageView>(R.id.product_thumbnail)
            val productName = itemView.findViewById<TextView>(R.id.product_name)
            val productPrice = itemView.findViewById<TextView>(R.id.product_price)
            val productAttribute = itemView.findViewById<TextView>(R.id.category_name)
            val increaseQuantity = itemView.findViewById<ImageButton>(R.id.increase_quantity)
            val decreaseQuantity = itemView.findViewById<ImageButton>(R.id.decrease_quantity)
            val quantityView = itemView.findViewById<TextView>(R.id.product_quantity)
            quantity=cartResponse[i].qty

            binding.headerLayout.text="Shopping Cart"
            increaseQuantity.setOnClickListener { quantity=1
                quantityView.text=quantity.toString()}
            decreaseQuantity.setOnClickListener { if(quantity>1){
                quantity -= 1
                quantityView.text=quantity.toString()
            }
            }
            categorySKU.text = cartResponse[i].products_with_description.sku
            productName.text = cartResponse[i].products_with_description.descriptions[0].name
            productPrice.text = cartResponse[i].products_with_description.price.toString()
            quantityView.text = cartResponse[i].qty.toString()
            productThumbnail.load(EndPoints.BASE_URL + cartResponse[i].products_with_description.image)
            binding.viewCartList.addView(itemView)
        }
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