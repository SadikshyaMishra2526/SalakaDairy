package com.eightpeak.salakafarm.views.home.products

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.eightpeak.salakafarm.databinding.FragmentProductListBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.viewmodel.ProductListViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.GridLayoutManager

import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.subutils.*

class ProductFragment : Fragment() {
    private lateinit var viewModel: ProductListViewModel
    lateinit var productAdapter: ProductAdapter
    private var layoutManager: GridLayoutManager? = null

    private var tokenManager: TokenManager? = null

    private var _binding: FragmentProductListBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentProductListBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductListBinding.inflate(layoutInflater, parent, false)
        tokenManager = TokenManager.getInstance(requireActivity().getSharedPreferences(
            Constants.TOKEN_PREF,
            AppCompatActivity.MODE_PRIVATE
        ))

        init()
        return binding.productLayout
    }


    var mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val wishlist = intent.getBooleanExtra("wishlist",false)
            val compareList = intent.getBooleanExtra("compare_list",false)
            val productId = intent.getStringExtra("product_id")
            val removeFromCart= intent.getBooleanExtra("remove",false)
            if(wishlist){
                if(!removeFromCart){
                    if (productId != null) {
                        tokenManager?.let { viewModel.addtowishlist(it,productId)}
                        addToWishListResponse()
                    }
                }else{
                    if (productId != null) {
                        tokenManager?.let { viewModel.removeFromWishlist(it,productId)}
                        removeWishListResponse()
                    }
                }

            }else if(compareList){
                App.addItem(productId)
                binding.productLayout.successCompareSnack(requireContext(),"Add to Compare List",Snackbar.LENGTH_LONG)

            }

            }
    }

    private fun addToWishListResponse() {
        viewModel.wishlist.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                          val serverResponse:ServerResponse = picsResponse
                        Log.i("TAG", "getPictures: $serverResponse")
                        binding.productLayout.successWishListSnack(requireContext(),getString(R.string.add_to_wishlist),Snackbar.LENGTH_LONG)

                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.productRecyclerView.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun removeWishListResponse() {
        viewModel.removeFromWishlist.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                          val serverResponse:ServerResponse = picsResponse

                        binding.productLayout.showSnack("Removed from Wishlist!!!",Snackbar.LENGTH_LONG)

                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.productRecyclerView.errorSnack(message, Snackbar.LENGTH_LONG)
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
        layoutManager = GridLayoutManager(context, 2)
        binding.productRecyclerView.layoutManager = layoutManager
        binding.productRecyclerView.setHasFixedSize(true)
        binding.productRecyclerView.isFocusable = false
        binding.productRecyclerView.adapter = productAdapter

        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(ProductListViewModel::class.java)
        getProductList()
        initializeWishCompare()
    }

    private fun initializeWishCompare() {
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mMessageReceiver,
        IntentFilter("custom-message")
    )
    }

    private fun getProductList() {

        viewModel.picsData.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                        val productModel:ProductModel = picsResponse
                        productAdapter.differ.submitList(productModel.data)
                        binding.productRecyclerView.adapter = productAdapter
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.productLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
//        progress.visibility = View.GONE
    }

    private fun showProgressBar() {
//        progress.visibility = View.VISIBLE
    }


    fun onProgressClick(view: View) {
        //Preventing Click during loading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

