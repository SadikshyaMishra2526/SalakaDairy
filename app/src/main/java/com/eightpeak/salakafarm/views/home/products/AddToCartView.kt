package com.eightpeak.salakafarm.views.home.products

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdModel
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.utils.subutils.notLoginWarningSnack
import com.eightpeak.salakafarm.utils.subutils.successAddToCartSnack
import kotlinx.android.synthetic.main.fragment_add_to_cart_view.view.*

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
        return root.add_to_cart_view
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

                            binding.productName.text = picsResponse.descriptions?.get(1)?.name ?: "not found"
                        } else {

                            binding.productName.text = picsResponse.descriptions?.get(0)?.name?: "not found"
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

    private fun getData(picsResponse: ProductByIdModel) {
        binding.setSKU.text = picsResponse.sku
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
        if (picsResponse.attributes!!?.isNotEmpty() == true) {
            Log.i("TAG", "getData: " + picsResponse.attributes[0].name)
            var attribute = ""
            for (i in picsResponse.attributes.indices) {
                attribute = attribute + " " + picsResponse.attributes[i].name + " , "

            }
            binding.attribute.text = "Attributes :-" + attribute
        }

        binding.btAddToCart.setOnClickListener {
            tokenManager?.let { it1 ->
                viewModel.addToCart(
                    it1,
                    picsResponse.id.toString(),
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
                        handler.postDelayed({ dismiss() }, 1500)
//                        Toast.makeText(
//                            requireContext(),
//                            "Successfully added to cart!!!",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        dismiss()
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