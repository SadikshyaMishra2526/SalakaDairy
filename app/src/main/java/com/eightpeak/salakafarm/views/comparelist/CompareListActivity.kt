package com.eightpeak.salakafarm.views.comparelist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityCompareListBinding
import com.eightpeak.salakafarm.databinding.ActivityWishlistBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.CompareViewModel
import com.eightpeak.salakafarm.viewmodel.GetResponseViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.slider.SliderAdapter
import com.google.android.material.snackbar.Snackbar
import com.hadi.retrofitmvvm.util.errorSnack
import kotlinx.android.synthetic.main.fragment_home_slider.*

class CompareListActivity :AppCompatActivity(){

    private lateinit var viewModel: CompareViewModel
    private var _binding: ActivityCompareListBinding? = null
    private var tokenManager: TokenManager? = null
    private lateinit var binding: ActivityCompareListBinding
    lateinit var userPrefManager: UserPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompareListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPrefManager= UserPrefManager(this)

        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        setupViewModel()


    }
    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(CompareViewModel::class.java)
        getCompareList()
    }
    private fun getCompareList() {
  var ids=""
            for(i in  App.getData()){
                ids += "$i,"
            }
        tokenManager?.let { it1 -> viewModel.getCompareResponse(it1,ids) }

        viewModel.compareResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let { picsResponse ->
                        Log.i("TAG", "getCompareList: gggggggggggggggggggggg"+picsResponse.products)
//                        binding.shimmerLayout.stopShimmer()
//                        binding.shimmerLayout.visibility = View.GONE
//
//                        sliderAdapter.notifyDataSetChanged()
//                        sliderAdapter.addItem(picsResponse)
//                        sliderAdapter = SliderAdapter(context,picsResponse)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        rootLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }
    private fun hideProgressBar() {
       binding. progress.visibility = View.GONE
    }

    private fun showProgressBar() {
//        progress.visibility = View.VISIBLE
    }


    fun onProgressClick(view: View) {
        //Preventing Click during loading
    }
}