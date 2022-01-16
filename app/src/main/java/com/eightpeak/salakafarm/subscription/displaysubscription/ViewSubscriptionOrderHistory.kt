package com.eightpeak.salakafarm.subscription.displaysubscription

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.LayoutSubscriptionOrderHistoryBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.SubscriptionViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar

class ViewSubscriptionOrderHistory  : AppCompatActivity() {
    private lateinit var binding: LayoutSubscriptionOrderHistoryBinding
    private lateinit var viewModel: SubscriptionViewModel
    lateinit var userPrefManager: UserPrefManager
    private var tokenManager: TokenManager? = null
    private var historyAdapter: SubscriptionHistoryAdapter? = null

    private var layoutManager: GridLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        binding = LayoutSubscriptionOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
      binding.header.text="Subscription History"
        binding.returnHome.setOnClickListener { finish() }
        userPrefManager = UserPrefManager(this)

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
        viewModel = ViewModelProvider(this, factory).get(SubscriptionViewModel::class.java)
        getPageDetails()
        init()
    }

    private fun init() {
        historyAdapter = SubscriptionHistoryAdapter()
        layoutManager = GridLayoutManager(this, 1)
        binding.subOrderHistoryRecycler.layoutManager = layoutManager
        binding.subOrderHistoryRecycler.setHasFixedSize(true)
        binding.subOrderHistoryRecycler.isFocusable = false
        binding.subOrderHistoryRecycler.adapter = historyAdapter
    }
    private fun getPageDetails() {
        val historyDate= RequestBodies.SubHistoryList("2021-12-25","2022-01-30")

        tokenManager?.let { viewModel.getSubHistory(it,historyDate) }
        viewModel.subHistory.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { history ->
                        if(history.moreInfo!!.isNotEmpty()){
                            historyAdapter?.differ?.submitList(history.moreInfo)
                            binding.subOrderHistoryRecycler.adapter = historyAdapter
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.orderHistory.errorSnack(message, Snackbar.LENGTH_LONG)
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