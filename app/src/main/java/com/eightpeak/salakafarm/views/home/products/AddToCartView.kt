package com.eightpeak.salakafarm.views.home.products

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.ProductByIdViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

import androidx.lifecycle.Observer
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.FragmentAddToCartViewBinding
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.utils.subutils.successAddToCartSnack
import com.eightpeak.salakafarm.views.home.categories.categoriesbyid.CategoriesByIdActivity
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdModel

class AddToCartView : BottomSheetDialogFragment() {
    private var _binding: FragmentAddToCartViewBinding? = null
    private val binding get() = _binding!!
    private var userPrefManager: UserPrefManager? = null
    private lateinit var viewModel: ProductByIdViewModel
    private var quantity: Int = 1
    private var tokenManager: TokenManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddToCartViewBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userPrefManager = UserPrefManager(context)
        tokenManager = TokenManager.getInstance(
            requireActivity().getSharedPreferences(
                Constants.TOKEN_PREF,
                AppCompatActivity.MODE_PRIVATE
            )
        )
        setupViewModel()
        return root.rootView
    }


    private fun setupViewModel() {

        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(ProductByIdViewModel::class.java)
        addProductToCart()
    }

    private fun addProductToCart() {
        val mArgs = arguments
        val productId = mArgs!!.getString(Constants.PRODUCT_ID)
        if (productId != null) {
            viewModel.getProductById(productId)
        }
        viewModel.productDetailsById.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        binding.productThumbnail.load(BASE_URL + picsResponse.image)
                        if (userPrefManager?.language == "ne") {
                            binding.productName.text = picsResponse?.descriptions[1].main_name
                        } else {
                            binding.productName.text = picsResponse.descriptions[0].main_name
                        }
                        getData(picsResponse)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.addToCartView.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun getData(productDetailsByIdResponse: ProductByIdModel) {
        if (productDetailsByIdResponse.promotion_price!=null) {
            if (userPrefManager?.language.equals("ne")) {
                binding.productPriceDiscount.text =
                    GeneralUtils.getUnicodeNumber(productDetailsByIdResponse.price.toString())
                binding.productPriceDiscount.paintFlags =
                    binding.productPriceDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.productPrice.text =
                    getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(productDetailsByIdResponse.promotion_price.price_promotion.toString())
            } else {
                binding.productPriceDiscount.text = productDetailsByIdResponse.price.toString()
                binding.productPriceDiscount.paintFlags =
                    binding.productPriceDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.productPrice.text =
                    getString(R.string.rs) + " " + productDetailsByIdResponse.promotion_price.price_promotion.toString()

            }
        } else {
            if (userPrefManager?.language.equals("ne")) {
                binding.productPrice.text =
                    getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(productDetailsByIdResponse.price.toString())
            } else {
                binding.productPrice.text =
                    getString(R.string.rs) + " " + productDetailsByIdResponse.price.toString()
            }
        }

        binding.setSKU.text = productDetailsByIdResponse.sku
        binding.productQuantity.text = quantity.toString()
        binding.increaseQuantity.setOnClickListener {
            quantity += 1
            binding.productQuantity.text = quantity.toString()
        }
        binding.decreaseQuantity.setOnClickListener {
            if (quantity > 1) {
                quantity -= 1
                binding.productQuantity.text = quantity.toString()
            }
        }
        if(productDetailsByIdResponse.categories_description.isNotEmpty()){
            binding.categoryThumbnail.load(BASE_URL+productDetailsByIdResponse.categories_description[0].image)
            if(userPrefManager?.language?.equals({"ne"}) == true){
                binding.categoryName.text=productDetailsByIdResponse.categories_description[0].descriptions[1].title
            }else{
                binding.categoryName.text=productDetailsByIdResponse.categories_description[0].descriptions[0].title

            }
            binding.categoryName.setOnClickListener {
                val intent = Intent(requireContext(), CategoriesByIdActivity::class.java)
                intent.putExtra(Constants.CATEGORIES_ID, productDetailsByIdResponse.categories_description[0].id.toString())
                startActivity(intent)
            }
        }
        binding.btAddToCart.setOnClickListener {
            tokenManager?.let { it1 ->
                viewModel.addToCart(
                    it1,
                    productDetailsByIdResponse.id.toString(),
                    quantity.toString(),
                    ""
                )
            }
        }
        viewModel.addToCart.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        binding.addToCartView.successAddToCartSnack(requireContext(),
                            getString(R.string.add_to_cart),Snackbar.LENGTH_LONG)
                        binding.addToCartView.visibility=View.GONE
                        val handler = Handler()
                        handler.postDelayed({
                            dismiss() }, 1000)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.addToCartView.errorSnack(message, Snackbar.LENGTH_LONG)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}