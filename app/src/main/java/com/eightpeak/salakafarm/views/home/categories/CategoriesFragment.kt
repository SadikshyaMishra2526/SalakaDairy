package com.eightpeak.salakafarm.views.home.categories

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.CategoriesViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.github.rubensousa.gravitysnaphelper.GravitySnapRecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hadi.retrofitmvvm.util.errorSnack

import kotlinx.android.synthetic.main.fragment_categories.*
import android.view.Gravity

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper

import androidx.recyclerview.widget.SnapHelper
import com.eightpeak.salakafarm.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {
    private lateinit var viewModel: CategoriesViewModel
    lateinit var categoriesAdapter: CategoriesAdapter
    private var _binding: FragmentCategoriesBinding? = null

    var layoutManager: LinearLayoutManager? = null

    private lateinit var binding: FragmentCategoriesBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(layoutInflater, parent, false)


        init()
        return binding.categoriesLayout
    }


    private fun init() {
//        categoriesRecyclerView = requireView().findViewById(R.id.recyclerView)
        categoriesAdapter = CategoriesAdapter()
        val snapHelper: SnapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(binding.categoriesRecyclerView)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesRecyclerView.layoutManager = layoutManager
        binding.categoriesRecyclerView.setHasFixedSize(true)
        binding.categoriesRecyclerView.isFocusable = false
        binding.categoriesRecyclerView.adapter = categoriesAdapter
        binding.seeAllCategories.setOnClickListener {
            val intent = Intent(context, CategoriesSeeAllActivity::class.java)
            startActivity(intent)
        }
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(CategoriesViewModel::class.java)
        getPictures()
    }

    private fun getPictures() {
        viewModel.picsData.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        Log.i("TAG", "getPictures: " + picsResponse.data.size)

                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE

                        categoriesAdapter.differ.submitList(picsResponse.data)
                        binding.categoriesRecyclerView.adapter = categoriesAdapter
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
