package com.eightpeak.salakafarm.subscription.displaysubscription

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.DisplaySubscriptionDetailsItemBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.SubscriptionViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar

class DisplaySubscription : AppCompatActivity() {
    private lateinit var binding: DisplaySubscriptionDetailsItemBinding
    private lateinit var viewModel: SubscriptionViewModel
    private var _binding: DisplaySubscriptionDetailsItemBinding? = null

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
        binding = DisplaySubscriptionDetailsItemBinding.inflate(layoutInflater)
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
        val subscription: List<Subscriptions> = subscriptionDetails.subscriptions
        binding.subscriberName.text = userPrefManager.firstName + " " + userPrefManager.lastName
                  binding.subscriberPackageName.text = subscription[0].sub_package_id.toString()
                  binding.subscriptionRemaining.text = subscription[0].remaining_quantity.toString()
                  binding.subscriptionTotal.text =  subscription[0].subscribed_total_amount.toString()
                  binding.unitPerDay.text = subscription[0].unit_per_day.toString()
                  binding.deliveryTime.text ="Morning"
                  binding.subscriberBranch.text =subscription[0].branch_id.toString()
//                subscriptionExpire.text =subscription[0].expired_at.toString()
                  binding.subscriptionAddress.text = subscription[0].address_id.toString()
//
//            for (i in subscription) {
//                val itemView: View =
//                    LayoutInflater.from(this)
//                        .inflate(
//                            R.layout.display_subscription_details_item,
//                            binding.getSubscriptionList,
//                            false
//                        )

//                val subscriberName = itemView.findViewById<TextView>(R.id.subscriber_name)
//                val subscriberPackageName =
//                    itemView.findViewById<TextView>(R.id.subscriber_package_name)
//                val subscriptionRemaining =
//                    itemView.findViewById<TextView>(R.id.subscription_remaining)
//                val subscriptionTotal = itemView.findViewById<TextView>(R.id.subscription_total)
//                val deliveryTime = itemView.findViewById<TextView>(R.id.delivery_time)
//                val unitPerDay = itemView.findViewById<TextView>(R.id.unit_per_day)
//                val subscriberBranch = itemView.findViewById<TextView>(R.id.subscriber_branch)
//                val subscriptionExpire = itemView.findViewById<TextView>(R.id.subscription_expire)
//                val subscriptionAddress = itemView.findViewById<TextView>(R.id.subscription_address)
//                val displaySubscriptionDates =
//                    itemView.findViewById<RecyclerView>(R.id.display_subscription_dates)
//Toast.makeText(this@DisplaySubscription,i.sub_package_id.toString(),Toast.LENGTH_SHORT).show()
//                subscriberName.text = userPrefManager.firstName + " " + userPrefManager.lastName
//                subscriberPackageName.text = i.sub_package_id.toString()
//                subscriptionRemaining.text = i.remaining_quantity.toString()
//                subscriptionTotal.text =  i.subscribed_total_amount.toString()
//                unitPerDay.text = i.unit_per_day.toString()
//                deliveryTime.text ="Morning"
//                subscriberBranch.text =i.branch_id.toString()
////                subscriptionExpire.text =i.expired_at.toString()
//                subscriptionAddress.text = i.address_id.toString()
//
//
//
//                subscriptionAdapter = SubscriptionAdapter()
//                layoutManager = GridLayoutManager(this, 2)
//                displaySubscriptionDates.layoutManager = layoutManager
//                displaySubscriptionDates.setHasFixedSize(true)
//                displaySubscriptionDates.isFocusable = false
//                displaySubscriptionDates.adapter = subscriptionAdapter
//
//                binding.getSubscriptionList.addView(itemView)


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


