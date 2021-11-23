package com.eightpeak.salakafarm.views.home.categories.categoriesbyid

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.eightpeak.salakafarm.databinding.FragmentCategoriesByIdBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.CategoriesByIdViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import kotlinx.android.synthetic.main.fragment_categories_by_id.*

import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL


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
                        val categoryDetails:CategoriesByIdModel=picsResponse
                        Log.i("TAG", "getPictures: $categoryDetails")
                        if(categoryDetails.descriptions.isNotEmpty()){
                            binding.edtSearchInput.visibility=View.VISIBLE
                            binding.categoriesByIdTitle.visibility=View.VISIBLE

                            if(userPrefManager.language.equals("ne")){
                                binding.categoryName.text = categoryDetails.descriptions[1].name
                            }else{
                                binding.categoryName.text = categoryDetails.descriptions[0].name
                            }
                        }

                        if(picsResponse.products_with_description.isNotEmpty()){
                            binding.categoriesNotFound.visibility=View.GONE

                            binding.categoriesThumbnail.load(BASE_URL+categoryDetails.image)

                            val categoriesByIdModel: List<Products_with_description> = picsResponse.products_with_description
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