package com.eightpeak.salakafarm.views.home.products

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.FragmentAddToCartViewBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.ProductByIdViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

import androidx.lifecycle.Observer
import coil.api.load
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import com.hadi.retrofitmvvm.util.errorSnack
import kotlinx.android.synthetic.main.fragment_add_to_cart_view.view.*
import kotlinx.android.synthetic.main.fragment_product_view_by_id.*
import java.util.*

class AddToCartView : BottomSheetDialogFragment() {
    private var _binding: FragmentAddToCartViewBinding? = null
    private val binding get() = _binding!!
    private var userPrefManager: UserPrefManager? = null

    private lateinit var viewModel: ProductByIdViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddToCartViewBinding.inflate(inflater, container, false)
        val root: View = binding.root
       userPrefManager = UserPrefManager(context)
        setupViewModel()
        return root.add_to_cart_view
    }

    private fun setupViewModel() {
        Log.i("TAG", "onCreateView: i reached here 2")

        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(ProductByIdViewModel::class.java)
        getPictures()
    }

    private fun getPictures() {
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
                            binding.productThumbnail.load(BASE_URL+picsResponse.image)
                        binding.productName.setText(picsResponse.descriptions[0].name)

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