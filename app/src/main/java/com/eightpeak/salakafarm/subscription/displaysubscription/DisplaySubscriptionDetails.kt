package com.eightpeak.salakafarm.subscription.displaysubscription

import DisplaySubscriptionModel
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityDisplaySubscriptionDetailsBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.SubscriptionViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.products.AddToCartView
import com.google.android.material.snackbar.Snackbar

class DisplaySubscriptionDetails : AppCompatActivity() {
    private lateinit var binding: ActivityDisplaySubscriptionDetailsBinding
    private lateinit var viewModel: SubscriptionViewModel
    lateinit var userPrefManager: UserPrefManager
    private var tokenManager: TokenManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        binding = ActivityDisplaySubscriptionDetailsBinding.inflate(layoutInflater)
        binding.headerTitle.text = getString(R.string.track_your_subscription)
        binding.returnHome.setOnClickListener { finish() }
        userPrefManager = UserPrefManager(this)
        setupViewModel()

        setContentView(binding.root)
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
                        binding.subscriptionDetails.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun displaySubscriptionDetails(subscriptionDetails: DisplaySubscriptionModel) {
        val subscription = subscriptionDetails.subscription
        Log.i("TAG", "displaySubscriptionDetails: $subscriptionDetails")
        binding.subscriberName.text = userPrefManager.firstName + " " + userPrefManager.lastName
        binding.subscriptionStarted.text = subscriptionDetails.subscription.starting_date
        binding.subscriptionExpire.text = subscriptionDetails.subscription.expired_at
        binding.subscriberPackageName.text = subscription.sub_package.name.toString()
        binding.subscriptionRemaining.text =
            subscription.remaining_quantity.toString() + "/" + subscription.subscribed_total_amount.toString()
       binding.paymentStatus.text="Unpaid"
       binding.paymentVia.text="By Bank"

        binding.unitPerDay.text = subscription.unit_per_day.toString()
        binding.deliveryTime.text = "Morning"
        binding.subscriberBranch.text = subscription.branch.name
//        binding.subscriptionAddress.text =subscription.address.address1
        binding.alterSubscriptionLayout.subItem.setBackgroundColor(Color.GREEN)
        binding.alterSubscriptionLayoutOne.subItem.setBackgroundColor(Color.RED)
        binding.alterSubscriptionLayoutTwo.subItem.setBackgroundColor(Color.YELLOW)
        binding.alterSubscriptionLayout.alterSubscription.setOnClickListener {
            val args = Bundle()
            args.putString(Constants.SUBSCRIPTION_ID, subscriptionDetails.subscription.id.toString())
            args.putString(Constants.QUANTITY, subscriptionDetails.subscription.unit_per_day.toString())
            args.putString(Constants.ALTER_DAY, "01/09/2022")
            val bottomSheet = AddAlterationDisplay()
            bottomSheet.arguments = args
            bottomSheet.show(
                (this@DisplaySubscriptionDetails as FragmentActivity).supportFragmentManager,
                bottomSheet.tag
            )
        }
        binding.trackYourOrder.setOnClickListener {
            val args = Bundle()
            args.putString(Constants.ORDER_ID, subscriptionDetails.subscription.id.toString())
            args.putString(Constants.TYPE, Constants.SUBSCRIPTION)
            val bottomSheet = TrackSubscriptionView()
            bottomSheet.arguments = args
            bottomSheet.show(
                (this@DisplaySubscriptionDetails as FragmentActivity).supportFragmentManager,
                bottomSheet.tag
            )
        }
        binding.cancelYourOrder.setOnClickListener {

        }

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