package com.eightpeak.salakafarm.views.addtocart.addtocartfragment

import android.R.attr
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.hadi.retrofitmvvm.util.errorSnack
import kotlinx.android.synthetic.main.fragment_add_to_cart.*
import android.view.View.OnTouchListener

import androidx.core.view.MotionEventCompat
import android.R.attr.right

import android.R.attr.left
import android.view.*
import android.widget.*


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
        return binding.addToCart
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
        binding.viewCartList.removeAllViews()
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
                        addToCart.errorSnack(message, Snackbar.LENGTH_LONG)
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
//        binding.productRecyclerView.setOnTouchListener(object : OnTouchListener {
//            var initialY = 0f
//            var finalY = 0f
//            var isScrollingUp = false
//            override fun onTouch(v: View, event: MotionEvent): Boolean {
//                val action = MotionEventCompat.getActionMasked(event)
//                when (action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        initialY = event.y
//                        finalY = event.y
//                        if (initialY < finalY) {
//                            Log.d("TAG", "Scrolling up")
//                            isScrollingUp = true
//                        } else if (initialY > finalY) {
//                            Log.d("TAG", "Scrolling down")
//                            isScrollingUp = false
//                        }
//                    }
//                    MotionEvent.ACTION_UP -> {
//                        finalY = event.y
//                        if (initialY < finalY) {
//                            Log.d("TAG HERE", "Scrolling up")
//                            isScrollingUp = true
//                        } else if (initialY > finalY) {
//                            Log.d("TAG HERE", "Scrolling down")
//                            isScrollingUp = false
//                        }
//                    }
//                    else -> {
//                    }
//                }
//                if (isScrollingUp) {
//                    Log.i("TAG", "onTouch: UPPPPPPPPPPPPPPPPPPP")
//                    // do animation for scrolling up
//                    params.gravity = Gravity.END
//                    binding.proceedWithCheckout.layoutParams = params
//                } else {
//                    Log.i("TAG", "onTouch: DOWNNNNNNNNNNNNNNNNN")
//
////                    params.setMargins(100, 100, 100, 10)
////                    binding.proceedWithCheckout.layoutParams = params
//
//                    // do animation for scrolling down
//                }
//                return false // has to be false, or it will freeze the listView
//            }
//        })
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