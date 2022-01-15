package com.eightpeak.salakafarm.views.gallery

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityGalleryBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.GetResponseViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.eightpeak.salakafarm.views.home.products.ProductAdapter
import com.eightpeak.salakafarm.views.home.products.ProductModel
import com.google.android.material.snackbar.Snackbar

class GalleryActivity :AppCompatActivity() {

    private var tokenManager: TokenManager? = null
    private lateinit var viewModel: GetResponseViewModel

    private var _binding: ActivityGalleryBinding? = null
    lateinit var userPrefManager: UserPrefManager

    lateinit var galleryAdapter: GalleryAdapter

    private var layoutManager: GridLayoutManager? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.text=getString(R.string.our_gallery)
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
        viewModel = ViewModelProvider(this, factory).get(GetResponseViewModel::class.java)
        getPageDetails()
        init()
    }

    private fun init() {
        galleryAdapter = GalleryAdapter()
        layoutManager = GridLayoutManager(this, 2)
        binding.galleryImagesRecycler.layoutManager = layoutManager
        binding.galleryImagesRecycler.setHasFixedSize(true)
        binding.galleryImagesRecycler.isFocusable = false
        binding.galleryImagesRecycler.adapter = galleryAdapter
    }
    private fun getPageDetails() {
        viewModel.getGalleryDetailsBanner()
        viewModel.getGalleryDetails.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { gallery ->
                        if(gallery.popup.data.isNotEmpty()){
                            galleryAdapter.differ.submitList(gallery.popup.data)
                            binding.galleryImagesRecycler.adapter = galleryAdapter
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.galleryView.errorSnack(message, Snackbar.LENGTH_LONG)
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
}