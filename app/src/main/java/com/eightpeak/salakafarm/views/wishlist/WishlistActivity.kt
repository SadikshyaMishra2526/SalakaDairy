package com.eightpeak.salakafarm.views.wishlist

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityWishlistBinding
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
import com.google.android.material.snackbar.Snackbar
import com.hadi.retrofitmvvm.util.errorSnack
import com.hadi.retrofitmvvm.util.successAddToCartSnack
import kotlinx.android.synthetic.main.fragment_add_to_cart.*

class WishlistActivity : AppCompatActivity() {
    private lateinit var viewModel: GetResponseViewModel
    private var _binding: ActivityWishlistBinding? = null
    private var tokenManager: TokenManager? = null
    private lateinit var binding: ActivityWishlistBinding
    lateinit var userPrefManager: UserPrefManager

    lateinit var productAdapter: ProductAdapter
    private var layoutManager: GridLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        binding.headerTitle.text="WishList"
        userPrefManager= UserPrefManager(this)
        setContentView(binding.root)
        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )

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

    private fun getPictures() {
        tokenManager?.let { it1 -> viewModel.getWishListResponse(it1) }

        viewModel.wishListResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
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
        if(cartResponse.isNotEmpty()){

        }
        for (i in cartResponse.indices) {
            val itemView: View =
                LayoutInflater.from(this)
                    .inflate(R.layout.wishlist_item, binding.viewCartList, false)

            val categorySKU = itemView.findViewById<TextView>(R.id.product_sku)
            val productThumbnail = itemView.findViewById<ImageView>(R.id.product_thumbnail)
            val productName = itemView.findViewById<TextView>(R.id.product_name)
            val productPrice = itemView.findViewById<TextView>(R.id.product_price)
            val addToCart = itemView.findViewById<Button>(R.id.add_to_cart)

            val deleteWishListItem = itemView.findViewById<ImageView>(R.id.delete_wishlist_item)

            categorySKU.text = cartResponse[i].products_with_description.sku
             productPrice.text = cartResponse[i].products_with_description.price.toString()
            productThumbnail.load(EndPoints.BASE_URL + cartResponse[i].products_with_description.image)



            deleteWishListItem.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete Wishlist Item")
                builder.setMessage("Are you sure you want to delete this item?")
                builder.setPositiveButton("Confirm",
                    DialogInterface.OnClickListener { dialog, which ->
                      tokenManager?.let { it1 -> viewModel.deleteWishlistById(it1,cartResponse[i].id.toString()) }
                        dialog.dismiss()
                    })
                builder.setNegativeButton(R.string.cancel, null)

                val dialog = builder.create()
                dialog.show()

            }








           if(cartResponse[i].products_with_description.descriptions.isEmpty()){
               if(userPrefManager.language.equals("ne")){
                   productName.text = cartResponse[i].products_with_description.descriptions[1].name
               }else{
                   productName.text = cartResponse[i].products_with_description.descriptions[0].name
               }
           }

            addToCart.setOnClickListener {
                tokenManager?.let { it1 -> viewModel.addToCartView(it1,cartResponse[i].id.toString(),"1","") }
                getCartResponse()
            }
            binding.viewCartList.addView(itemView)
        }
    }

    private fun getCartResponse() {
        viewModel.addToCart.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                         binding.wishlistView.successAddToCartSnack(this,getString(R.string.add_to_cart),Snackbar.LENGTH_LONG)

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
    private fun init() {
        productAdapter = ProductAdapter()
        layoutManager = GridLayoutManager(this, 2)
        binding.productRecyclerView.layoutManager = layoutManager
        binding.productRecyclerView.setHasFixedSize(true)
        binding.productRecyclerView.isFocusable = false
        binding.productRecyclerView.adapter = productAdapter

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