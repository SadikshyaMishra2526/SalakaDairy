package com.eightpeak.salakafarm.views.home.products.productbyid

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import com.eightpeak.salakafarm.databinding.ActivityHomeBinding
import com.eightpeak.salakafarm.databinding.FragmentProductViewByIdBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.utils.Constants.Companion.PRODUCT_ID
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.ProductByIdViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
import com.hadi.retrofitmvvm.util.errorSnack
import kotlinx.android.synthetic.main.fragment_product_view_by_id.*
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController

import com.smarteist.autoimageslider.SliderView

import com.smarteist.autoimageslider.SliderAnimations

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType

import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController.ClickListener


class ProductByIdActivity : AppCompatActivity() {
    private lateinit var viewModel: ProductByIdViewModel
    private var _binding: FragmentProductViewByIdBinding? = null
    private lateinit var binding: FragmentProductViewByIdBinding
    private lateinit var sliderList: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProductViewByIdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }


    private fun init() {
        return_home.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        go_to_cart.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        setupViewModel()
    }

    private fun setupViewModel() {
        Log.i("TAG", "onCreateView: i reached here 2")

        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(ProductByIdViewModel::class.java)
        getPictures()
    }

    private fun getPictures() {
        val product_id = intent.getStringExtra(PRODUCT_ID)
        Log.i("TAG", "getPictures: $product_id")
        if (product_id != null) {
            viewModel.getProductById(product_id)
        }
        viewModel.productDetailsById.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        setProductsDetails(picsResponse)

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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setProductsDetails(productDetailsByIdResponse: ProductByIdModel) {

        if (productDetailsByIdResponse.descriptions.isNotEmpty()) {
            product_details_name.text = productDetailsByIdResponse.descriptions[0].name
            product_content.text =
                Html.fromHtml(
                    productDetailsByIdResponse.descriptions[0].content,
                    Html.FROM_HTML_MODE_COMPACT
                )
        }

        product_price.text = productDetailsByIdResponse.price.toString()
        product_details_sku.text = productDetailsByIdResponse.sku
        if (productDetailsByIdResponse.images.isNotEmpty()) {
            binding.productDetailsThumbnail.visibility = View.GONE
            sliderList = ArrayList()
            sliderList.add(productDetailsByIdResponse.image)
            for (item in productDetailsByIdResponse.images) {
                Log.i("TAG", "setProductsDetails: " + item.image)
                sliderList.add(item.image)
            }
            showSlider(sliderList!!)
        } else {
            binding.productsDetailSlider.visibility = View.GONE
            binding.productDetailsThumbnail.load(EndPoints.BASE_URL + productDetailsByIdResponse.image)
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

