package com.eightpeak.salakafarm.subscription.displaysubscription

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.DisplaySubscriptionDetailsBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.SubscriptionViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar

class SubscriptionDetails : AppCompatActivity() {
    private lateinit var binding: DisplaySubscriptionDetailsBinding
    private lateinit var viewModel: SubscriptionViewModel
    private var _binding: DisplaySubscriptionDetailsBinding? = null

    lateinit var subscriptionAdapter: SubscriptionAdapter
    lateinit var userPrefManager: UserPrefManager
    private var tokenManager: TokenManager? = null

    private var layoutManager: GridLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        binding = DisplaySubscriptionDetailsBinding.inflate(layoutInflater)
        userPrefManager = UserPrefManager(this)
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(SubscriptionViewModel::class.java)


        getCustomerSubscription()
    }

    private fun getCustomerSubscription() {
        tokenManager?.let { it1 -> viewModel.fetchCustomerSubscription(it1) }
        viewModel.getCustomerSubscription.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        displaySubscriptionDetails(picsResponse)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.layout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun displaySubscriptionDetails(subscriptionDetails: DisplaySubscriptionModel) {
        val subscription = subscriptionDetails.subscriptions
//        binding.subscriberName.text = userPrefManager.firstName + " " + userPrefManager.lastName
////        binding.subscriberPackageName.text =subscription.sub_package.name.toString()
//        binding.subscriptionRemaining.text =subscription.remaining_quantity.toString()
//        binding.subscriptionTotal.text = subscription.subscribed_total_amount.toString()
//        binding.unitPerDay.text =subscription.unit_per_day.toString()
//        binding.deliveryTime.text ="Morning"
//        binding.subscriberBranch.text =subscription.branch.name
//        binding.subscriptionAddress.text =subscription.address.address1

    }

    private fun hideProgressBar() {
//        binding.progress.visibility = View.GONE
    }

    private fun showProgressBar() {
//        binding.progress.visibility = View.VISIBLE
    }


    fun onProgressClick(view: View) {
        //Preventing Click during loading
    }


}
