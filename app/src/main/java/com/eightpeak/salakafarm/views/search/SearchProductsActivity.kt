package com.eightpeak.salakafarm.views.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivitySearchBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.SearchViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
import com.eightpeak.salakafarm.utils.subutils.errorSnack

class SearchProductsActivity :AppCompatActivity(){

    private var _binding: ActivitySearchBinding? = null
    private var userPrefManager: UserPrefManager? = null

    lateinit var searchProductAdapter: SearchAdapter
    private lateinit var binding: ActivitySearchBinding

    private lateinit var viewModel: SearchViewModel
    private var layoutManager: GridLayoutManager? = null

    private var tokenManager: TokenManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        tokenManager = TokenManager.getInstance(getSharedPreferences(Constants.TOKEN_PREF, MODE_PRIVATE))

        setContentView(binding.root)
        userPrefManager = UserPrefManager(this)
         init()
    }

    private fun init() {

        setupViewModel()

        searchProductAdapter = SearchAdapter()
        layoutManager = GridLayoutManager(this, 2)
        binding.searchRecyclerView.layoutManager = layoutManager
        binding.searchRecyclerView.setHasFixedSize(true)
        binding.searchRecyclerView.isFocusable = false
        binding.searchRecyclerView.adapter = searchProductAdapter

        binding.returnHome.setOnClickListener {
           hideKeyboard(this@SearchProductsActivity)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)
        getPictures()
    }

    private fun getPictures() {

        binding.searchButton.setOnClickListener {
            binding.shimmerLayout.startShimmer()
            binding.shimmerLayout.visibility = View.VISIBLE

            Log.i("TAG", "getPictures: "+ (tokenManager?.token) +binding.tvSearchInput.text.toString())
            tokenManager?.let { it1 -> viewModel.getSearchData(it1,binding.tvSearchInput.text.toString(),"price_asc") }
        }

        viewModel.searchData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                         if(picsResponse.data.isNotEmpty())
                             binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE

                        searchProductAdapter.differ.submitList(picsResponse.data)
                        binding.searchRecyclerView.adapter = searchProductAdapter
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                       binding.searchView.errorSnack(message, Snackbar.LENGTH_LONG)
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
    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager? =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        assert(imm != null)
        imm!!.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}