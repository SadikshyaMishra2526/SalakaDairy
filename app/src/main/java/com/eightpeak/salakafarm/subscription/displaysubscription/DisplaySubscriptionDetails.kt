package com.eightpeak.salakafarm.subscription.displaysubscription

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.date.AD
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.SubscriptionViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import android.app.ProgressDialog
import android.graphics.Color
import coil.api.load
import com.eightpeak.salakafarm.databinding.ActivityDisplaySubscriptionDetailsBinding
import com.eightpeak.salakafarm.subscription.displaysubscription.models.DeliveryHistoryDisplay
import com.eightpeak.salakafarm.subscription.displaysubscription.models.DisplaySubscriptionModel
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig
import uk.co.deanwild.materialshowcaseview.shape.RectangleShape
import uk.co.deanwild.materialshowcaseview.shape.Shape


class DisplaySubscriptionDetails : AppCompatActivity() {
    private lateinit var binding: ActivityDisplaySubscriptionDetailsBinding
    private lateinit var viewModel: SubscriptionViewModel
    lateinit var userPrefManager: UserPrefManager
    private var tokenManager: TokenManager? = null
    private var subscriptionAdapter: SubscriptionAdapter? = null
    private var layoutManager: GridLayoutManager? = null
    private var subscriptionDetailsModel: DisplaySubscriptionModel? = null
    private val ad = AD()

    private var remainingLitre: String? = null

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

        // TODO Auto-generated method stub
        val todayNepaliDate = ad.convertDate(GeneralUtils.getTodayDate())
        binding.todayDate.text = todayNepaliDate
        showCase()
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
        layoutManager = GridLayoutManager(this, 5)
        binding.displaySubscriptionDates.layoutManager = layoutManager
        binding.displaySubscriptionDates.setHasFixedSize(true)
        binding.displaySubscriptionDates.isFocusable = false
        binding.displaySubscriptionDates.adapter = subscriptionAdapter

        setupViewModel()
    }

    private fun openActivity(view: View, category: DeliveryHistoryDisplay) {
        val args = Bundle()
        args.putString(
            Constants.SUBSCRIPTION_ID,
            subscriptionDetailsModel?.subscription?.id.toString()
        )
        args.putString(
            Constants.QUANTITY,
            subscriptionDetailsModel?.subscription?.unit_per_day.toString()
        )
        args.putString(Constants.ALTER_DAY, category.date_eng)
        args.putString(Constants.REMAINING_LITRE, remainingLitre)
        args.putString(Constants.ALTERATION_STATUS, category.alter_status.toString())

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
                        Log.i("TAG", "getCustomerSubscription: " + picsResponse.message)


                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                        if (picsResponse.error != null && picsResponse.error == "1") {
                            userPrefManager.subscriptionStatus = false
                            binding.subscriptionDetails.errorSnack(
                                picsResponse.message,
                                Snackbar.LENGTH_LONG
                            )
                            binding.subscriptionView.visibility = View.GONE
                            val handler = Handler()
                            handler.postDelayed({
                                finish()
                            }, 1000)
                        } else {

                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.visibility = View.GONE
                            binding.subscriptionView.visibility = View.VISIBLE
                            displaySubscriptionDetails(picsResponse)

                        }
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
        binding.subscriptionStarted.text =
            ad.convertDate(subscriptionDetails.subscription.starting_date)
        binding.subscriptionExpire.text =
            ad.convertDate(subscriptionDetails.subscription.expiration_time)
        binding.subscriberPackageName.text = subscription.sub_package.name.toString()
        binding.subscriptionRemaining.text =
            subscription.remaining_quantity + "/" + subscription.total_quantity.toString()
        binding.paymentVia.text = subscription.mode
        binding.remainingDays.text = subscription.remaining_quantity
        remainingLitre = subscription.remaining_quantity
        binding.unitPerDay.text = subscription.unit_per_day.toString() + getString(R.string.litre)
        binding.subscriberBranch.text = subscription.branch.name

        binding.bankName.text = subscription.branch.bank
        binding.bankAcc.text = subscription.branch.account_number
        binding.bankHolder.text = subscription.branch.account_holder
        binding.bankQr.load(BASE_URL + subscription.branch.qrcode)

        binding.subscriptionAddress.text = subscription.address.address1
//for notification topic
        pushNotificationService(subscription.branch_id)


        if (subscription.payment_at != null) {
            binding.unpaid4.visibility = View.GONE
            binding.unpaid5.visibility = View.GONE
            binding.unpaid6.visibility = View.GONE
            binding.unpaid7.visibility = View.GONE
            binding.unpaid8.visibility = View.GONE
            binding.warningMessage.text = getString(R.string.paid_warning)
            binding.warningMessage.setTextColor(Color.BLUE)
            binding.paymentStatus.text = getString(R.string.pending)
            binding.paymentStatus.setTextColor(Color.BLUE)

            if (subscription.approved_at != null) {
                binding.paymentStatus.text = getString(R.string.paid)
                binding.paymentStatus.setTextColor(Color.GREEN)
                binding.unpaid1.visibility = View.VISIBLE
                binding.unpaid2.visibility = View.VISIBLE
                binding.unpaid4.visibility = View.GONE
                binding.warningCard.visibility = View.GONE
                binding.functionView.visibility = View.VISIBLE
                binding.cancelYourUnpaymetSubscription.visibility=View.GONE
                showAfterPaymentCase()
            }

        } else {

            binding.warningCard.visibility = View.VISIBLE
            binding.unpaid1.visibility = View.GONE
            binding.unpaid2.visibility = View.GONE
            binding.unpaid4.visibility = View.VISIBLE
            binding.paymentStatus.text = getString(R.string.unpaid)
            binding.paymentStatus.setTextColor(Color.RED)
            binding.functionView.visibility = View.GONE
            binding.cancelYourUnpaymetSubscription.visibility = View.VISIBLE
            binding.unpaid4.visibility = View.VISIBLE
            binding.unpaid5.visibility = View.VISIBLE
            binding.unpaid6.visibility = View.VISIBLE
            binding.unpaid7.visibility = View.VISIBLE
            binding.unpaid8.visibility = View.VISIBLE
        }




        if (subscription.delivery_peroid == 0) {
            userPrefManager.packageSelected = 0
            binding.deliveryTime.text = getString(R.string.morning_shift)

        } else if (subscription.delivery_peroid == 1) {
            userPrefManager.packageSelected = 1
            binding.deliveryTime.text = getString(R.string.evening_shift)

        } else if (subscription.delivery_peroid == 2) {
            userPrefManager.packageSelected = 2
            binding.deliveryTime.text = getString(R.string.both_shift)
        }

        if (subscription.mode == "bank" || subscription.mode == "qr") {
            binding.warningMessage.visibility = View.VISIBLE
        } else if (subscription.mode == "esewa") {

        } else if (subscription.mode == "Cash on Delivery") {
            binding.warningMessage.text =
                getString(R.string.wait_for_payment)
        }

        binding.addComplainToSubscription.setOnClickListener {
            val args = Bundle()
            args.putString(Constants.ORDER_ID, subscriptionDetails.subscription.id.toString())
            args.putString(Constants.TYPE, Constants.SUBSCRIPTION)
            val bottomSheet = SubscriptionComplain()
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

        binding.cancelYourSubscription.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.unsubscribe_cancel_title))
            builder.setIcon(R.drawable.cancel)
            builder.setMessage(getString(R.string.cancel_sub_content))
            builder.setPositiveButton(getString(R.string.unsubscribe),
                DialogInterface.OnClickListener { _, _ ->
                    tokenManager?.let { it1 ->
                        viewModel.postCancelSubscription(
                            it1,
                            subscriptionDetailsModel!!.subscription.id.toString()
                        )
                    }
                    getCancelSubscription()
                })
            builder.setNegativeButton(R.string.cancel, null)

            val dialog = builder.create()
            dialog.show()

        }
        binding.cancelYourUnpaymetSubscription.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.unsubscribe_cancel_title))
            builder.setIcon(R.drawable.cancel)
            builder.setMessage(getString(R.string.cancel_sub_content))
            builder.setPositiveButton(getString(R.string.unsubscribe),
                DialogInterface.OnClickListener { _, _ ->
                    tokenManager?.let { it1 ->
                        viewModel.postCancelSubscription(
                            it1,
                            subscriptionDetailsModel!!.subscription.id.toString()
                        )
                    }
                    getCancelSubscription()
                })
            builder.setNegativeButton(R.string.cancel, null)

            val dialog = builder.create()
            dialog.show()

        }
        binding.viewSubscriptionHistory.setOnClickListener {
            val intent =
                Intent(this@DisplaySubscriptionDetails, ViewSubscriptionOrderHistory::class.java)
            intent.putExtra("start", subscriptionDetailsModel!!.subscription.starting_date)
            intent.putExtra("end", subscriptionDetailsModel!!.subscription.expiration_time)
            startActivity(intent)
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

    private fun addSubscriptionDate(subscriptionDetails: List<DeliveryHistoryDisplay>) {
        val deliveryHistory: List<DeliveryHistoryDisplay> = subscriptionDetails
        subscriptionAdapter!!.differ.submitList(deliveryHistory)
        binding.displaySubscriptionDates.adapter = subscriptionAdapter
        binding.displaySubscriptionDates.setItemViewCacheSize(subscriptionDetails.size)
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

    private fun pushNotificationService(branchId: Int?) {
        var topic = branchId.toString() + "_customers"
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnSuccessListener {
            }
    }


    private fun showCase() {
        val DEFAULT_SHAPE: Shape = RectangleShape(20, 20)
        val config = ShowcaseConfig()
        config.shape = DEFAULT_SHAPE
        config.delay = 500
        val sequence = MaterialShowcaseSequence(this, "103")
        sequence.setConfig(config)
        sequence.addSequenceItem(
            binding.displaySubscriptionDates,
            "On long press add/cancel additional quantity to subscription here!!! ",
            getString(R.string.got_it)
        )
        sequence.start()
    }

    private fun showAfterPaymentCase() {
        val DEFAULT_SHAPE: Shape = RectangleShape(20, 20)
        val config = ShowcaseConfig()
        config.shape = DEFAULT_SHAPE
        config.delay = 500
        val sequence = MaterialShowcaseSequence(this, "104")
        sequence.setConfig(config)
        sequence.addSequenceItem(
            binding.displaySubscriptionDates,
            "On long press add/cancel additional quantity or cancel that day's subscription here!!! ",
            getString(R.string.got_it)
        )
        sequence.addSequenceItem(
            binding.addComplainToSubscription,
            "Add Complain if you are not satisfied with out services", getString(R.string.got_it)
        )
        sequence.addSequenceItem(
            binding.trackYourOrder,
            "Track your subscription location and contact the Employee here!!!",
            getString(R.string.got_it)
        )
        sequence.addSequenceItem(
            binding.viewSubscriptionHistory,
            "Track your subscription history here !!!", getString(R.string.got_it)
        )
        sequence.addSequenceItem(
            binding.cancelYourSubscription,
            "Unsubscribe our service if you are not satisfied !!!", getString(R.string.got_it)
        )
        sequence.start()
    }
}