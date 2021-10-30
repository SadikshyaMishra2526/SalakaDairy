package com.eightpeak.salakafarm.views.wishlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.ActivityWishlistBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.GetResponseViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.ui.addtocart.CartResponse
import com.google.android.material.snackbar.Snackbar
import com.hadi.retrofitmvvm.util.errorSnack

class WishlistActivity : AppCompatActivity() {
    private lateinit var viewModel: GetResponseViewModel
    private var _binding: ActivityWishlistBinding? = null

    var layoutManager: LinearLayoutManager? = null
    private var tokenManager: TokenManager? = null

    private lateinit var binding: ActivityWishlistBinding
    private  var quantity: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        setupViewModel()
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
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(GetResponseViewModel::class.java)
        getPictures()
    }

    private fun getPictures() {
        tokenManager?.let { it1 -> viewModel.getWishListResponse(it1) }

        viewModel.wishListResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        Log.i("TAG", "getPictures:cccccccccccccccccccc ${picsResponse.size}")
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
                        binding.wishlistView.errorSnack(message, Snackbar.LENGTH_LONG)
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
                LayoutInflater.from(this)
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
            decreaseQuantity.setOnClickListener {

            }
            increaseQuantity.setOnClickListener {

            }

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
            categorySKU.text = cartResponse[i].products_with_description.sku
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