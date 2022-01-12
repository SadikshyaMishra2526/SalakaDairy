package com.eightpeak.salakafarm.subscription.displaysubscription

import DeliveryHistory
import DisplaySubscriptionModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityDisplaySubscriptionDetailsBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.SubscriptionViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.eightpeak.salakafarm.views.order.orderview.orderhistory.OrderHistory
import com.google.android.material.snackbar.Snackbar

class DisplaySubscriptionDetails : AppCompatActivity() {
    private lateinit var binding: ActivityDisplaySubscriptionDetailsBinding
    private lateinit var viewModel: SubscriptionViewModel
    lateinit var userPrefManager: UserPrefManager
    private var tokenManager: TokenManager? = null
    private var subscriptionAdapter: SubscriptionAdapter? = null
    private var layoutManager: GridLayoutManager? = null
    private var subscriptionDetailsModel: DisplaySubscriptionModel? = null


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

        init()
        setContentView(binding.root)

        Log.i("TAG", "onCreate: "+ GeneralUtils.calculateNepaliDate(11,1,2022))
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(SubscriptionViewModel::class.java)
        getCustomerSubscription()


    }

    private fun init() {
        subscriptionAdapter = SubscriptionAdapter(onClickListener = { view, category ->
            openActivity(
                view,
                category
            )
        })
        layoutManager = GridLayoutManager(this, 4)
        binding.displaySubscriptionDates.layoutManager = layoutManager
        binding.displaySubscriptionDates.setHasFixedSize(true)
        binding.displaySubscriptionDates.isFocusable = false
        binding.displaySubscriptionDates.adapter = subscriptionAdapter
        setupViewModel()
    }

    private fun openActivity(view: View, category: DeliveryHistory) {
        Log.i("TAG", "openActivity: " + category)
        val args = Bundle()
        args.putString(
            Constants.SUBSCRIPTION_ID,
            subscriptionDetailsModel?.subscription?.id.toString()
        )
        args.putString(
            Constants.QUANTITY,
            subscriptionDetailsModel?.subscription?.unit_per_day.toString()
        )
        args.putString(Constants.ALTER_DAY, "01/${category.date}/2022")
        val bottomSheet = AddAlterationDisplay()
        bottomSheet.arguments = args
        bottomSheet.show(
            (this as FragmentActivity).supportFragmentManager,
            bottomSheet.tag
        )
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
        subscriptionDetailsModel = subscriptionDetails
        val subscription = subscriptionDetails.subscription
        addSubscriptionDate(subscription.deliveryHistory)
        Log.i("TAG", "displaySubscriptionDetails: $subscriptionDetails")
        binding.subscriberName.text = userPrefManager.firstName + " " + userPrefManager.lastName
        binding.subscriptionStarted.text = subscriptionDetails.subscription.starting_date
        binding.subscriptionExpire.text = subscriptionDetails.subscription.expired_at
        binding.subscriberPackageName.text = subscription.sub_package.name.toString()
        binding.subscriptionRemaining.text =
            subscription.remaining_quantity.toString() + "/" + subscription.subscribed_total_amount.toString()
        binding.paymentVia.text = subscription.mode
        binding.unitPerDay.text = subscription.unit_per_day.toString()
        binding.deliveryTime.text = "Morning"
        binding.subscriberBranch.text = subscription.branch.name
        binding.subscriptionAddress.text = subscription.address.address1


        binding.paymentStatus.text = "Unpaid"
        if (subscription.mode == "bank" || subscription.mode == "qr") {

            binding.warningMessage.visibility = View.VISIBLE
        } else if (subscription.mode == "esewa" ){

        }else if( subscription.mode == "cashondelivery"){
            binding.warningMessage.text ="Subscription needs to be validated to start. You can pay our employee when he/she delivers your first subscription..."
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

        binding.cancelYourSubscription.setOnClickListener {
            tokenManager?.let { it1 ->
                viewModel.postCancelSubscription(
                    it1,
                    subscriptionDetailsModel!!.subscription.id.toString()
                )
            }
            getCancelSubscription()
        }
        binding.viewOrderHistory.setOnClickListener {
            Intent(this@DisplaySubscriptionDetails, OrderHistory::class.java)
            finish()

        }
        binding.addEvidence.setOnClickListener {
            val args = Bundle()
            args.putString(
                Constants.SUBSCRIPTION_ID,
                subscriptionDetailsModel?.subscription?.id.toString()
            )
            val bottomSheet = PaymentEvidenceFragment()
            bottomSheet.arguments = args
            bottomSheet.show(
                (this as FragmentActivity).supportFragmentManager,
                bottomSheet.tag
            )
        }

    }

    private fun getCancelSubscription() {
        viewModel.cancelSubscription.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        userPrefManager.subscriptionStatus = false;
                        val mainActivity =
                            Intent(this@DisplaySubscriptionDetails, HomeActivity::class.java)
                        startActivity(mainActivity)
                        finish()
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

    private fun addSubscriptionDate(subscriptionDetails: List<DeliveryHistory>) {
        val deliveryHistory: List<DeliveryHistory> = subscriptionDetails
        subscriptionAdapter!!.differ.submitList(deliveryHistory)
        binding.displaySubscriptionDates.adapter = subscriptionAdapter


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

    override fun onBackPressed() {
        super.onBackPressed()
        val mainActivity = Intent(this@DisplaySubscriptionDetails, HomeActivity::class.java)
        startActivity(mainActivity)
        finish()
    }
}