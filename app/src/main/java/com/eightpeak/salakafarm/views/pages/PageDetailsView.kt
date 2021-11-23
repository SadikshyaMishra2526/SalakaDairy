package com.eightpeak.salakafarm.views.pages

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.PageDetailViewBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.GetResponseViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.order.orderview.viewordercheckoutdetails.CheckoutDetailsView
import com.google.android.material.snackbar.Snackbar
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.views.home.HomeActivity

class PageDetailsView : AppCompatActivity() {

    private var tokenManager: TokenManager? = null
    private lateinit var viewModel: GetResponseViewModel

    private var _binding: PageDetailViewBinding? = null
    lateinit var userPrefManager: UserPrefManager

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = PageDetailViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
     binding.returnHome.setOnClickListener { finish() }
        userPrefManager = UserPrefManager(this)
        binding.returnHome.setOnClickListener {
            startActivity(Intent(this@PageDetailsView, HomeActivity::class.java))
            finish()
        }

        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(GetResponseViewModel::class.java)
        getPageDetails()
    }

    private fun getPageDetails() {
        val mIntent = intent
        val intValue = mIntent.getStringExtra("page_id")
        if (intValue != null) {
            viewModel.getPageDetailsView(intValue)
        }

        viewModel.getPageDetails.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->


                        if (picsResponse.page.descriptions.isNotEmpty()) {
                            if(userPrefManager.language.equals("ne")){
                                binding.header.text = picsResponse.page.descriptions[1].title
                                binding.pageDescription.text =
                                    picsResponse.page.descriptions[1].description
                                binding.pageContent.text =    Html.fromHtml(
                                    picsResponse.page.descriptions[1].content,
                                    Html.FROM_HTML_MODE_COMPACT
                                )
                                binding.pageThumbnail.load(BASE_URL + picsResponse.page.image)

                            }else{
                                binding.pageContent.text =    Html.fromHtml(
                                    picsResponse.page.descriptions[0].content,
                                    Html.FROM_HTML_MODE_COMPACT
                                )


                                binding.header.text = picsResponse.page.descriptions[0].title
                                binding.pageDescription.text =
                                    picsResponse.page.descriptions[0].description
                                binding.pageThumbnail.load(BASE_URL + picsResponse.page.image)
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.pageDetail.errorSnack(message, Snackbar.LENGTH_LONG)
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
