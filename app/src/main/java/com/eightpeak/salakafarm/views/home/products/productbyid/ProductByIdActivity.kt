package com.eightpeak.salakafarm.views.home.products.productbyid

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.FragmentProductViewByIdBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.Constants.Companion.PRODUCT_ID
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.ProductByIdViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.utils.subutils.successAddToCartSnack
import com.eightpeak.salakafarm.utils.subutils.successWishListSnack
import com.eightpeak.salakafarm.views.addtocart.CartActivity
import kotlinx.android.synthetic.main.fragment_product_view_by_id.*

import com.smarteist.autoimageslider.SliderView

import com.smarteist.autoimageslider.SliderAnimations

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType

import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController.ClickListener
import kotlinx.android.synthetic.main.product_item.view.*
import kotlin.math.roundToInt


class ProductByIdActivity : AppCompatActivity() {
    private lateinit var viewModel: ProductByIdViewModel
    private var _binding: FragmentProductViewByIdBinding? = null
    private lateinit var binding: FragmentProductViewByIdBinding
    private lateinit var sliderList: ArrayList<String>

    lateinit var relatedProductAdapter: RelatedProductAdapter
    private var layoutManager: GridLayoutManager? = null

    private var tokenManager: TokenManager? = null

    private var userPrefManager: UserPrefManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProductViewByIdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tokenManager = TokenManager.getInstance(getSharedPreferences(Constants.TOKEN_PREF, MODE_PRIVATE))
        userPrefManager = UserPrefManager(this)

        init()
    }


    private fun init() {
        binding.goToCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
            finish()
        }
        setupViewModel()

        relatedProductAdapter = RelatedProductAdapter()
        layoutManager = GridLayoutManager(this, 2)
        binding.relatedProductRecycler.layoutManager = layoutManager
        binding.relatedProductRecycler.setHasFixedSize(true)
        binding.relatedProductRecycler.isFocusable = false
        binding.relatedProductRecycler.adapter = relatedProductAdapter
        binding.returnHome.setOnClickListener { finish() }

    }

    private fun setupViewModel() {

        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(ProductByIdViewModel::class.java)
        getPictures()
    }

    private fun getPictures() {
        val productId = intent.getStringExtra(PRODUCT_ID)
          if (productId != null) {
            viewModel.getProductById(productId)
        }
        viewModel.productDetailsById.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        setProductsDetails(picsResponse)
                        if(picsResponse.productRelation.isNotEmpty())
                        relatedProductAdapter.differ.submitList(picsResponse.productRelation)
                        binding.relatedProductRecycler.adapter = relatedProductAdapter
                        getRating()
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        product_view_id_layout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }
    private fun getRating() {
        val productId = intent.getStringExtra(PRODUCT_ID)
        if (productId != null) {
            viewModel.productRating(productId)
        }
        viewModel.productRating.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                      plotRating(picsResponse)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        product_view_id_layout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun plotRating(ratingResponse: ProductRatingModel) {

        if(ratingResponse.ratings.data.isNotEmpty()) {
             val data: List<Data> =ratingResponse.ratings.data
            for (i in data.indices) {
                val itemView: View =
                    LayoutInflater.from(this)
                        .inflate(R.layout.rating_view_item, binding.ratingView, false)
                val commentedAt = itemView.findViewById<TextView>(R.id.commented_at)
                val commentedBy = itemView.findViewById<TextView>(R.id.commented_by)
                val comment = itemView.findViewById<TextView>(R.id.comment)

                val rating1 = itemView.findViewById<ImageView>(R.id.rating_1)
                val rating2 = itemView.findViewById<ImageView>(R.id.rating_2)
                val rating3 = itemView.findViewById<ImageView>(R.id.rating_3)
                val rating4 = itemView.findViewById<ImageView>(R.id.rating_4)
                val rating5 = itemView.findViewById<ImageView>(R.id.rating_5)
                commentedAt.text = data[i].created_at
                commentedBy.text =(data[i].customer_id.toString())
                comment.text =(data[i].comment)
                val rating:Int= data[i].rating
                if(rating==1){
                    rating1.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
                }else if(rating==2){
                    rating1.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
                    rating2.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
                }else if(rating==3){
                    rating1.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
                    rating2.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
                    rating3.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
                }else if(rating==4){
                    rating1.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
                    rating2.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
                    rating3.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
                    rating4.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
                }else if(rating==5){
                    rating1.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
                    rating2.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
                    rating3.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
                    rating4.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
                    rating5.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
                }
                binding.ratingView.addView(itemView)
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun setProductsDetails(productDetailsByIdResponse: ProductByIdModel) {
        if(productDetailsByIdResponse.stock>0){
            binding.outOfStock.visibility=View.GONE
        }else{
            binding.outOfStock.visibility=View.VISIBLE
        }
        product_price.text = productDetailsByIdResponse.price.toString()
        if (productDetailsByIdResponse.descriptions.isNotEmpty()) {
            if (userPrefManager?.language.equals("ne")) {
                product_details_name.text = productDetailsByIdResponse.descriptions[1].name
                product_content.text =
                    Html.fromHtml(
                        productDetailsByIdResponse.descriptions[1].content,
                        Html.FROM_HTML_MODE_COMPACT
                    )
                if(!productDetailsByIdResponse.cost.equals("0")){
                    product_price_discount.text= GeneralUtils.getUnicodeNumber(productDetailsByIdResponse.price.toString())
                    product_price_discount.paintFlags = product_price_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    product_price.text =
                      getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(productDetailsByIdResponse.cost.toString())
                }else{
                    product_price.text =
                        getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(productDetailsByIdResponse.price.toString())
                }

            } else {
                product_details_name.text = productDetailsByIdResponse.descriptions[0].name
                product_content.text =
                    Html.fromHtml(
                        productDetailsByIdResponse.descriptions[0].content,
                        Html.FROM_HTML_MODE_COMPACT
                    )

                if(!productDetailsByIdResponse.cost.equals("0")){
                    product_price_discount.text=productDetailsByIdResponse.price.toString()
                    product_price_discount.paintFlags = product_price_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    product_price.text =
                        getString(R.string.rs) + productDetailsByIdResponse.cost.toString()
                }else{
                    product_price.text =
                        getString(R.string.rs) + productDetailsByIdResponse.price.toString()
                }
            }
        }


        product_details_sku.text = productDetailsByIdResponse.sku
        if (productDetailsByIdResponse.images.isNotEmpty()) {
            binding.productDetailsThumbnail.visibility = View.GONE
            sliderList = ArrayList()
            sliderList.add(productDetailsByIdResponse.image)
            for (item in productDetailsByIdResponse.images) {
                sliderList.add(item.image)
            }
            showSlider(sliderList)
        } else {
            binding.productsDetailSlider.visibility = View.GONE
            binding.productDetailsThumbnail.load(EndPoints.BASE_URL + productDetailsByIdResponse.image)
        }



        binding.productAddToWishlist.setOnClickListener {
            val productId = productDetailsByIdResponse.id.toString()
            tokenManager?.let { it1 -> viewModel.addTowishlist(it1,productId) }
            viewModel.wishlist.observe(this, Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let {
                            binding.productViewIdLayout.successWishListSnack(this,getString(R.string.add_to_wishlist),Snackbar.LENGTH_LONG)
                        }
                    }

                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let { message ->
                            product_view_id_layout.errorSnack(message, Snackbar.LENGTH_LONG)
                        }
                    }

                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            })
        }


        binding.btAddToCart.setOnClickListener {


            val product_id = intent.getStringExtra(PRODUCT_ID)
            if (product_id != null) {
                tokenManager?.let { it1 -> viewModel.addToCart(it1,product_id,"1","") }
            }
            viewModel.addToCart.observe(this, Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let {
                            binding.productViewIdLayout.successAddToCartSnack(this,getString(R.string.add_to_cart),Snackbar.LENGTH_LONG)
                        }
                    }

                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let { message ->
                            product_view_id_layout.errorSnack(message, Snackbar.LENGTH_LONG)
                        }
                    }

                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            })

        }

        val rating:Int= productDetailsByIdResponse.average_rating.roundToInt()
        rated_by.text = "("+productDetailsByIdResponse.no_of_rating+") "
        if(rating==1){
            binding.rating1.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
        }else if(rating==2){
           binding.rating1.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
           binding.rating2.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
        }else if(rating==3){
           binding.rating1.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
           binding.rating2.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
           binding.rating3.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
        }else if(rating==4){
           binding.rating1.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
           binding.rating2.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
           binding.rating3.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
           binding.rating4.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
        }else if(rating==5){
           binding.rating1.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
           binding.rating2.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
           binding.rating3.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
           binding.rating4.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
           binding.rating5.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_rate_24))
        }

    }

    private fun showSlider(sliderList: ArrayList<String>) {
        Log.i("TAG", "showSlider: " + sliderList[0].toString())
        val sliderAdapter = ProductDetailsSliderAdapter(this, sliderList)
        sliderAdapter.renewItems(sliderList)
        binding.productsDetailSlider.setSliderAdapter(sliderAdapter)
        binding.productsDetailSlider.setIndicatorAnimation(IndicatorAnimationType.SWAP) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.productsDetailSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.productsDetailSlider.autoCycleDirection =
            SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        binding.productsDetailSlider.setIndicatorSelectedColor(Color.WHITE)
        binding.productsDetailSlider.setIndicatorUnselectedColor(Color.GRAY)
        binding.productsDetailSlider.setScrollTimeInSec(3)
        binding.productsDetailSlider.setAutoCycle(true)
        binding.productsDetailSlider.startAutoCycle()
        binding.productsDetailSlider.setOnIndicatorClickListener(ClickListener {
            Log.i(
                "GGG",
                "onIndicatorClicked: " + binding.productsDetailSlider.getCurrentPagePosition()
            )
        })
    }

    private fun hideProgressBar() {
        progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        progress.visibility = View.VISIBLE
    }


    fun onProgressClick(view: View) {
        //Preventing Click during loading
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

