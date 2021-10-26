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
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.FragmentProductViewByIdBinding
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import java.io.File


class CategoriesByIdActivity  : AppCompatActivity() {
    private lateinit var viewModel: CategoriesByIdViewModel
    lateinit var categoriesByIdAdapter: CategoriesByIdAdapter


    private var _binding: FragmentCategoriesByIdBinding? = null


    private var layoutManager: GridLayoutManager? = null

    private lateinit var binding: FragmentCategoriesByIdBinding

    lateinit var userPrefManager: UserPrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCategoriesByIdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPrefManager= UserPrefManager(this)

        init()
    }



    private fun init() {
        categoriesByIdAdapter = CategoriesByIdAdapter()
        layoutManager = GridLayoutManager(this, 2)
        binding.categoriesByIdRecyclerView.layoutManager = layoutManager
        binding.categoriesByIdRecyclerView.setHasFixedSize(true)
        binding.categoriesByIdRecyclerView.isFocusable = false
        binding.categoriesByIdRecyclerView.adapter = categoriesByIdAdapter

        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(CategoriesByIdViewModel::class.java)
        getPictures()
    }

    private fun getPictures() {
        val categories_id = intent.getStringExtra(Constants.CATEGORIES_ID)

        if (categories_id != null) {
            viewModel.getCategoriesByIdDetails(categories_id)
        }
        viewModel.categoriesbyIdData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        if(picsResponse.products.isNotEmpty()){
                            binding.categoriesNotFound.visibility=View.GONE

                            val categoryDetails:CategoriesByIdModel=picsResponse
                            if(categoryDetails.descriptions.isNotEmpty()){

                                binding.edtSearchInput.visibility=View.VISIBLE
                                binding.categoriesByIdTitle.visibility=View.VISIBLE
                                binding.categoriesThumbnail.load(BASE_URL+categoryDetails.image)

                                if(userPrefManager.language.equals("ne")){
                                    binding.categoryName.setText(categoryDetails.descriptions[1].title)
                                }else{
                                    binding.categoryName.setText(categoryDetails.descriptions[0].title)
                                }
                            }
                            val categoriesByIdModel: List<Products> = picsResponse.products
                            categoriesByIdAdapter.differ.submitList(categoriesByIdModel)
                            binding.categoriesByIdRecyclerView.adapter = categoriesByIdAdapter


                        }else{
                            binding.categoriesNotFound.visibility=View.VISIBLE
                        }
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE

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

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}