package com.eightpeak.salakafarm.views.home.products.productbyid

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.eightpeak.salakafarm.databinding.FragmentProductListBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.ProductListViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.products.ProductAdapter
import com.eightpeak.salakafarm.views.home.products.ProductModel
import com.google.android.material.snackbar.Snackbar
import com.hadi.retrofitmvvm.util.errorSnack
import kotlinx.android.synthetic.main.fragment_categories.*

class ProductByIdFragment  : Fragment() {
    private lateinit var viewModel: ProductListViewModel
    lateinit var productAdapter: ProductAdapter


    private var _binding: FragmentProductListBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var layoutManager: GridLayoutManager? = null

    private lateinit var binding: FragmentProductListBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductListBinding.inflate(layoutInflater, parent, false)

        Log.i("TAG", "onCreateView: i reached here 1")
        init()
        return binding.categoriesLayout
    }


    private fun init() {
//        categoriesRecyclerView = requireView().findViewById(R.id.recyclerView)
        productAdapter = ProductAdapter()
        layoutManager = GridLayoutManager(context, 2)
        binding.categoriesRecyclerView.layoutManager = layoutManager
        binding.categoriesRecyclerView.setHasFixedSize(true)
        binding.categoriesRecyclerView.isFocusable = false
        binding.categoriesRecyclerView.adapter = productAdapter

        setupViewModel()
    }

    private fun setupViewModel() {
        Log.i("TAG", "onCreateView: i reached here 2")

        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(ProductListViewModel::class.java)
        getPictures()
    }

    private fun getPictures() {

        viewModel.picsData.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        Log.i("TAG", "onCreateView: i reached here 3$picsResponse")
                        val productModel: ProductModel = picsResponse
                        Log.i("TAG", "getPictures: ,,,,,,,,," +productModel.data[0].image)
                        productAdapter.differ.submitList(productModel.data)
                        binding.categoriesRecyclerView.adapter = productAdapter
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        categoriesRecyclerView.errorSnack(message, Snackbar.LENGTH_LONG)
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

