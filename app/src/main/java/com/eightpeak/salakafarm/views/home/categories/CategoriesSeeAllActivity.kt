package com.eightpeak.salakafarm.views.home.categories

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityCategoriesSeeAllBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.CategoriesByIdViewModel
import com.eightpeak.salakafarm.viewmodel.CategoriesViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.categories.categoriesbyid.CategoriesByIdAdapter
import com.eightpeak.salakafarm.views.home.categories.categoriesbyid.Products_with_description
import com.google.android.material.snackbar.Snackbar
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.views.search.SearchProductsActivity

class CategoriesSeeAllActivity : AppCompatActivity() {

    private var _binding: ActivityCategoriesSeeAllBinding? = null
    private lateinit var binding: ActivityCategoriesSeeAllBinding

    private lateinit var viewModel: CategoriesViewModel
    private lateinit var viewModelById: CategoriesByIdViewModel

    private var layoutManager: GridLayoutManager? = null

    private var getOnClickProduct: Boolean? = true

    lateinit var categoriesByIdAdapter: CategoriesByIdAdapter
    private var userPrefManager: UserPrefManager? = null


    private  var  categoriesModel: CategoriesModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesSeeAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPrefManager = UserPrefManager(this)
        setupViewModel()
        init()
    }

    private fun init() {
        categoriesByIdAdapter = CategoriesByIdAdapter()
        layoutManager = GridLayoutManager(this, 2)
        binding.categoriesListByIdRecyclerView.layoutManager = layoutManager
        binding.categoriesListByIdRecyclerView.setHasFixedSize(true)
        binding.categoriesListByIdRecyclerView.isFocusable = false
        binding.categoriesListByIdRecyclerView.adapter = categoriesByIdAdapter

        setupViewModel()
    }
    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(CategoriesViewModel::class.java)
        setupViewModelById()
      getCategories()
        getPictures("1")
        binding.btBackpressed.setOnClickListener { finish() }
        binding.headerName.text="See All your Categories"
        binding.searchProduct.setOnClickListener {
            val mainActivity = Intent(this@CategoriesSeeAllActivity, SearchProductsActivity::class.java)
            startActivity(mainActivity)
        }
    }

    private fun getCategories() :String{
        viewModel.picsData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        getSelectedCategoryProducts(picsResponse)
                        categoriesModel=picsResponse
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                       binding.categoriesByIdRecyclerView.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        return  categoriesModel?.data?.get(0)?.id.toString( )
    }

    private fun getSelectedCategoryProducts(picsResponse: CategoriesModel) {
        binding.categoriesListView.removeAllViews()
        for (i in picsResponse.data.indices) {
            val itemView: View =
                LayoutInflater.from(this)
                    .inflate(R.layout.categories_see_add_title, binding.categoriesListView, false)

            val categoryName = itemView.findViewById<TextView>(R.id.category_name)
            categoryName.text = picsResponse.data[i].descriptions[0].title

            val categoriesCard = itemView.findViewById<CardView>(R.id.categoriesCard)


            binding. categoryThumbnail.load((BASE_URL+ categoriesModel?.data?.get(0)?.image))
            if(userPrefManager?.language.equals("ne")){
                binding.selectedCategory.text= categoriesModel?.data?.get(i)?.descriptions?.get(1)?.title ?: "Not Found"
            }else{
                binding.selectedCategory.text= categoriesModel?.data?.get(i)?.descriptions?.get(0)?.title ?: "Not Found"
            }


            categoriesCard.setOnClickListener {
               binding. categoryThumbnail.load((BASE_URL+ categoriesModel?.data?.get(i)?.image))
                if(userPrefManager?.language.equals("ne")){
                    binding.selectedCategory.text= categoriesModel?.data?.get(i)?.descriptions?.get(1)?.title ?: "Not Found"
                }else{
                    binding.selectedCategory.text= categoriesModel?.data?.get(i)?.descriptions?.get(0)?.title ?: "Not Found"

                }

                getPictures(picsResponse.data[i].id.toString())
                getOnClickProduct=false
            }


            val categoryThumbnail = itemView.findViewById<ImageView>(R.id.category_thumbnail)
            categoryThumbnail.load(BASE_URL + picsResponse.data[i].image)
            binding.categoriesListView.addView(itemView)
        }
    }

    private fun setupViewModelById() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModelById = ViewModelProvider(this, factory).get(CategoriesByIdViewModel::class.java)
    }

    private fun getPictures(categories_id: String) {

        viewModelById.getCategoriesByIdDetails(categories_id)
        viewModelById.categoriesbyIdData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        if (picsResponse.products_with_description.isNotEmpty()) {
                            binding.categoriesNotFound.visibility = View.GONE

                            binding.categoriesListByIdRecyclerView.visibility=View.VISIBLE
                            val categoriesByIdModel: List<Products_with_description> = picsResponse.products_with_description
                            categoriesByIdAdapter.differ.submitList(categoriesByIdModel)
                            binding.categoriesListByIdRecyclerView.adapter = categoriesByIdAdapter


                        } else {
                            binding.categoriesListByIdRecyclerView.visibility=View.GONE
                            binding.categoriesNotFound.visibility = View.VISIBLE
                        }

                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.categoriesByIdRecyclerView.errorSnack(message, Snackbar.LENGTH_LONG)
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