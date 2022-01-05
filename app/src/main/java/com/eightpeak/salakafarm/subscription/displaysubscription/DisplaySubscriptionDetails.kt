package com.eightpeak.salakafarm.subscription.displaysubscription

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityDisplaySubscriptionDetailsBinding
import com.eightpeak.salakafarm.mapfunctions.MapsFragment
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.Constants.Companion.SUBSCRIPTION
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
        binding.headerTitle.text="Track your subscription"
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


        binding.trackYourOrder.setOnClickListener {
            val fm: FragmentManager = supportFragmentManager
            var fragment = fm.findFragmentByTag("myFragmentTag")
            if (fragment == null) {
                val ft: FragmentTransaction = fm.beginTransaction()
                fragment = MapsFragment()
                ft.add(R.id.subscriptionView, fragment, "myFragmentTag")
                ft.commit()
            }
////            Log.i("TAG", "displaySubscriptionDetails: ")
//            val args = Bundle()
//            args.putString(Constants.PRODUCT_ID, "9")
//            args.putString(Constants.TYPE, SUBSCRIPTION)
//            val bottomSheet = TrackSubscriptionView()
//            bottomSheet.arguments = args
//            bottomSheet.show(
//                (this as FragmentActivity).supportFragmentManager,
//                bottomSheet.tag
//            )


        }
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
        val subscription = subscriptionDetails.subscriptions
        Log.i("TAG", "displaySubscriptionDetails: $subscriptionDetails")
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