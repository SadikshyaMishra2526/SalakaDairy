package com.eightpeak.salakafarm.views.home.categories.categoriesbyid

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.eightpeak.salakafarm.databinding.FragmentCategoriesByIdBinding
import com.eightpeak.salakafarm.databinding.FragmentProductListBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.CategoriesByIdViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.products.ProductModel
import com.google.android.material.snackbar.Snackbar
import com.hadi.retrofitmvvm.util.errorSnack
import kotlinx.android.synthetic.main.fragment_categories_by_id.*
import kotlinx.android.synthetic.main.fragment_product_list.*
import android.widget.Toast

import android.content.ActivityNotFoundException

import android.content.Intent
import android.net.Uri
import java.io.File


class CategoriesByIdFragment  : Fragment() {
    private lateinit var viewModel: CategoriesByIdViewModel
    lateinit var categoriesByIdAdapter: CategoriesByIdAdapter


    private var _binding: FragmentCategoriesByIdBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var layoutManager: GridLayoutManager? = null

    private lateinit var binding: FragmentCategoriesByIdBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentCategoriesByIdBinding.inflate(layoutInflater, parent, false)

        Log.i("TAG", "onCreateView: i reached here 1")
        init()
        return binding.categoriesLayout
    }


    private fun init() {
        categoriesByIdAdapter = CategoriesByIdAdapter()
        layoutManager = GridLayoutManager(context, 2)
        binding.categoriesByIdRecyclerView.layoutManager = layoutManager
        binding.categoriesByIdRecyclerView.setHasFixedSize(true)
        binding.categoriesByIdRecyclerView.isFocusable = false
        binding.categoriesByIdRecyclerView.adapter = categoriesByIdAdapter

        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(CategoriesByIdViewModel::class.java)
        getPictures()
    }

    private fun getPictures() {
        viewModel.getCategoriesByIdDetails("id")
        viewModel.categoriesbyIdData.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        val categoriesByIdModel: CategoriesByIdModel = picsResponse
//                        categoriesByIdAdapter.differ.submitList(categoriesByIdModel.descriptions)
//                        binding.categoriesByIdRecyclerView.adapter = categoriesByIdAdapter
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        categoriesByIdRecyclerView.errorSnack(message, Snackbar.LENGTH_LONG)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}