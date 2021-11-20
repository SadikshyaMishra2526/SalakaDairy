package com.eightpeak.salakafarm.views.addtocart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.ActivityCartBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.GetResponseViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.addtocart.addtocartfragment.CartResponse
import com.eightpeak.salakafarm.views.home.products.Data
import com.eightpeak.salakafarm.views.home.products.ProductAdapter
import com.eightpeak.salakafarm.views.home.products.ProductModel
import com.eightpeak.salakafarm.views.order.orderview.viewordercheckoutdetails.CheckoutDetailsView
import com.hadi.retrofitmvvm.util.errorSnack
import kotlinx.android.synthetic.main.fragment_add_to_cart.*
import kotlinx.android.synthetic.main.product_item.view.*

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var viewModel: GetResponseViewModel
    private var _binding: ActivityCartBinding? = null
    private var tokenManager: TokenManager? = null
    private  var quantity: Int = 0

    lateinit var productAdapter: ProductAdapter
    private var layoutManager: GridLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)


        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        binding.proceedWithCheckout.setOnClickListener {
            startActivity(Intent(this@CartActivity, CheckoutDetailsView::class.java))
            finish()
        }

        init()
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(GetResponseViewModel::class.java)
        getPictures()
        getRandomProducts()
    }
    private fun init() {
        productAdapter = ProductAdapter()
        layoutManager = GridLayoutManager(this, 2)
        binding.productRecyclerView.layoutManager = layoutManager
        binding.productRecyclerView.setHasFixedSize(true)
        binding.productRecyclerView.isFocusable = false
        binding.productRecyclerView.adapter = productAdapter

    }
    private fun getPictures() {
        tokenManager?.let { it1 -> viewModel.getCartResponse(it1) }

        viewModel.cartResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        getSelectedProducts(picsResponse)
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
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

    private fun getRandomProducts() {
        viewModel.getRandomListResponseView()

        viewModel.getRandomListResponse.observe(this, Observer { response ->
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
        productAdapter.differ.submitList(data)
        binding.productRecyclerView.adapter = productAdapter

    }

    private fun getSelectedProducts(cartResponse: List<CartResponse>) {
        for (i in cartResponse.indices) {
            val itemView: View =
                LayoutInflater.from(this)
                    .inflate(R.layout.cart_item, binding.viewCartList, false)
            binding.header.text = "Shopping Cart"

            val categorySKU = itemView.findViewById<TextView>(R.id.product_sku)
            val productThumbnail = itemView.findViewById<ImageView>(R.id.product_thumbnail)
            val productName = itemView.findViewById<TextView>(R.id.product_name)
            val productPrice = itemView.findViewById<TextView>(R.id.product_price)
            val productAttribute = itemView.findViewById<TextView>(R.id.category_name)
            val increaseQuantity = itemView.findViewById<ImageButton>(R.id.increase_quantity)
            val decreaseQuantity = itemView.findViewById<ImageButton>(R.id.decrease_quantity)
            val quantityView = itemView.findViewById<TextView>(R.id.product_quantity)
            val itemSelected = itemView.findViewById<ImageView>(R.id.item_selected)
            quantityView.text =  cartResponse[i].qty.toString()

           binding.deleteCartItems.setOnClickListener {
               itemSelected.load(R.drawable.green_tick)

           }
            increaseQuantity.setOnClickListener {
                quantity= cartResponse[i].qty
                quantity += 1
                quantityView.text = quantity.toString()
            }
            decreaseQuantity.setOnClickListener { if(quantity>1){
                quantity= cartResponse[i].qty
                quantity -= 1
                quantityView.text = quantity.toString()
             }
            }
           var isClicked=true
            itemSelected.setOnClickListener {
                if(isClicked){
                    itemSelected.load(R.drawable.green_tick)
                    isClicked=false
                }else{
                    itemSelected.load(R.drawable.ic_baseline_radio_button_unchecked_24)
                    isClicked=true
                }

            }
            categorySKU.text = cartResponse[i].products_with_description.sku
            productName.text = cartResponse[i].products_with_description.descriptions[0].name
            productPrice.text = cartResponse[i].products_with_description.price.toString()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}