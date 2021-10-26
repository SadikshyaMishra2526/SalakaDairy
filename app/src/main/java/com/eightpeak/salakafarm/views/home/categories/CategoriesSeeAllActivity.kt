package com.eightpeak.salakafarm.views.home.categories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityCategoriesSeeAllBinding
import com.eightpeak.salakafarm.databinding.FragmentAddToCartViewBinding
import com.eightpeak.salakafarm.databinding.FragmentCategoriesByIdBinding
import com.eightpeak.salakafarm.databinding.FragmentProductViewByIdBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.CategoriesByIdViewModel
import com.eightpeak.salakafarm.viewmodel.CategoriesViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.categories.categoriesbyid.CategoriesByIdModel
import com.eightpeak.salakafarm.views.home.categories.categoriesbyid.Products
import com.google.android.material.snackbar.Snackbar
import com.hadi.retrofitmvvm.util.errorSnack
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_categories_by_id.*

class CategoriesSeeAllActivity : AppCompatActivity() {

    private var _binding: ActivityCategoriesSeeAllBinding? = null

    private lateinit var binding: ActivityCategoriesSeeAllBinding

    private lateinit var viewModel: CategoriesViewModel
    private lateinit var viewModelById: CategoriesByIdViewModel

    private var userPrefManager: UserPrefManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesSeeAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPrefManager = UserPrefManager(this)
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(CategoriesViewModel::class.java)
        setupViewModelById()
        getCategories()
    }

    private fun getCategories() {
        viewModel.picsData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        Log.i("TAG", "getPictures: " + picsResponse.data.size)
                        getSelectedCategoryProducts(picsResponse)
//                        binding.shimmerLayout.stopShimmer()
//                        binding.shimmerLayout.visibility = View.GONE
//
//                        categoriesAdapter.differ.submitList(picsResponse.data)
//                        binding.categoriesRecyclerView.adapter = categoriesAdapter
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

    private fun getSelectedCategoryProducts(picsResponse: CategoriesModel) {
        for (i in picsResponse.data.indices) {
            val itemView: View =
                LayoutInflater.from(this)
                    .inflate(R.layout.categories_see_add_title, binding.categoriesListView, false)

            val categoryName = itemView.findViewById<TextView>(R.id.category_name)
            categoryName.text = picsResponse.data[i].descriptions[0].title


            val categoryThumbnail = itemView.findViewById<ImageView>(R.id.category_thumbnail)
            categoryThumbnail.load(BASE_URL+picsResponse.data[i].image)
            binding.categoriesListView.addView(itemView)
        }
    }

    private fun setupViewModelById() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModelById = ViewModelProvider(this, factory).get(CategoriesByIdViewModel::class.java)
//        getPictures("0")
    }

    private fun getPictures(categories_id: String) {

        viewModelById.getCategoriesByIdDetails(categories_id)
        viewModelById.categoriesbyIdData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
//                        if(picsResponse.products.isNotEmpty()){
//                            binding.categoriesNotFound.visibility=View.GONE
//
//                            val categoryDetails: CategoriesByIdModel =picsResponse
//                            if(categoryDetails.descriptions.isNotEmpty()){
//
//                                binding.edtSearchInput.visibility=View.VISIBLE
//                                binding.categoriesByIdTitle.visibility=View.VISIBLE
//                                binding.categoriesThumbnail.load(EndPoints.BASE_URL +categoryDetails.image)
//
//                                if(userPrefManager.language.equals("ne")){
//                                    binding.categoryName.setText(categoryDetails.descriptions[1].title)
//                                }else{
//                                    binding.categoryName.setText(categoryDetails.descriptions[0].title)
//                                }
//                            }
//                            val categoriesByIdModel: List<Products> = picsResponse.products
//                            categoriesByIdAdapter.differ.submitList(categoriesByIdModel)
//                            binding.categoriesByIdRecyclerView.adapter = categoriesByIdAdapter
//
//
//                        }else{
//                            binding.categoriesNotFound.visibility=View.VISIBLE
//                        }
//                        binding.shimmerLayout.stopShimmer()
//                        binding.shimmerLayout.visibility = View.GONE

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
        _binding = null
    }
}