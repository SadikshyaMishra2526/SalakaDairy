package com.eightpeak.salakafarm.views.comparelist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityCompareListBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.CompareViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.utils.subutils.successAddToCartSnack
import com.eightpeak.salakafarm.utils.subutils.successWishListSnack

class CompareListActivity : AppCompatActivity() {

    private lateinit var viewModel: CompareViewModel
    private var _binding: ActivityCompareListBinding? = null
    private var tokenManager: TokenManager? = null
    private lateinit var binding: ActivityCompareListBinding
    lateinit var userPrefManager: UserPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompareListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPrefManager = UserPrefManager(this)

        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        binding.header.text = getString(R.string.my_compare_list)

        setupViewModel()


    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(CompareViewModel::class.java)
        getCompareList()
    }

    private fun getCompareList() {
        var ids = ""
        for (i in App.getData()) {
            ids += "$i,"
        }
        tokenManager?.let { it1 -> viewModel.getCompareResponse(it1, ids) }

        viewModel.compareResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let { picsResponse ->
                        Log.i(
                            "TAG",
                            "getCompareList: gggggggggggggggggggggg" + picsResponse.products
                        )
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                        displayCompareList(picsResponse)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.compareListView.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun displayCompareList(compareResponse: CompareResponse) {
        for (i in compareResponse.products.indices) {
            val itemView: View =
                LayoutInflater.from(this)
                    .inflate(R.layout.compare_list_item, binding.productsList, false)

            val categorySKU = itemView.findViewById<TextView>(R.id.product_sku)
            val productThumbnail = itemView.findViewById<ImageView>(R.id.product_thumbnail)
            val productName = itemView.findViewById<TextView>(R.id.product_name)
            val productPrice = itemView.findViewById<TextView>(R.id.product_price)
            val productAttribute = itemView.findViewById<TextView>(R.id.category_name)
            val productBrand = itemView.findViewById<TextView>(R.id.product_brand)
            val stock = itemView.findViewById<TextView>(R.id.stock)
            val wishList = itemView.findViewById<TextView>(R.id.product_add_to_wishlist)
            val addToCart = itemView.findViewById<TextView>(R.id.product_add_to_cart)
            Log.i("TAG", "displayCompareList: "+compareResponse.products.toString())
            categorySKU.text = compareResponse.products[i].sku
            productName.text = compareResponse.products[i].descriptions[0].title
            productPrice.text = compareResponse.products[i].price.toString()
            productBrand.text = compareResponse.products[i].categories_description[0].alias
            productThumbnail.load(BASE_URL + compareResponse.products[i].image)
            if(compareResponse.products[i].stock>0){
                stock.text = getString(R.string.in_stock)
            }else{
                stock.text = getString(R.string.out_of_stock)

            }

            wishList.setOnClickListener {
                tokenManager?.let { it1 -> viewModel.addtowishlist(it1, compareResponse.products[i].id.toString()) }
                getWishListResponse()

            }

            addToCart.setOnClickListener {
                tokenManager?.let { it1 -> viewModel.addToCartView(it1, compareResponse.products[i].id.toString(),"1","") }
                getCartResponse()
            }
            binding.productsList.addView(itemView)

        }
    }

    private fun getWishListResponse() {
        viewModel.wishlist.observe(this, Observer { response ->
        when (response) {
            is Resource.Success -> {
                hideProgressBar()
                response.data?.let {
                    binding.compareListView.successWishListSnack(this,getString(R.string.add_to_cart),Snackbar.LENGTH_LONG)

                }
            }

            is Resource.Error -> {
                hideProgressBar()
                response.message?.let { message ->
                    binding.compareListView.errorSnack(message, Snackbar.LENGTH_LONG)
                }
            }

            is Resource.Loading -> {
                showProgressBar()
            }
        }
    })
    }

    private fun getCartResponse() {
        viewModel.addToCart.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        binding.compareListView.successAddToCartSnack(this,getString(R.string.add_to_cart),Snackbar.LENGTH_LONG)

                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.compareListView.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
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
}