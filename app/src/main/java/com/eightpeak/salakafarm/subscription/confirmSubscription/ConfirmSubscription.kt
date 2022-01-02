package com.eightpeak.salakafarm.subscription.confirmSubscription

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import androidx.lifecycle.Observer

import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityConfirmSubscriptionBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.SubscriptionViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.addtocart.CartActivity
import com.esewa.android.sdk.payment.ESewaConfiguration
import com.esewa.android.sdk.payment.ESewaPayment
import com.esewa.android.sdk.payment.ESewaPaymentActivity
import com.google.android.material.snackbar.Snackbar
import java.util.*

class ConfirmSubscription : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmSubscriptionBinding
    private lateinit var viewModel: SubscriptionViewModel
    private var _binding: ActivityConfirmSubscriptionBinding? = null

    lateinit var userPrefManager: UserPrefManager
    private var tokenManager: TokenManager? = null


    private val CONFIG_ENVIRONMENT: String = ESewaConfiguration.ENVIRONMENT_TEST
    private val REQUEST_CODE_PAYMENT = 1
    private var eSewaConfiguration: ESewaConfiguration? = null

    private lateinit var selectedBranchId: String
    private lateinit var selectedAddressId: String
    private lateinit var selectedSubscribedTotalAmount: String
    private lateinit var selectedSubscribedDiscount: String
    private lateinit var selectedSubscribedPrice: String
    private lateinit var selectedUnitPerDay: String
    private lateinit var selectedStartingDate: String
    private lateinit var selectedDeliveryPeroid: String
    private lateinit var selectedDeliveryPeroidResponse: String
    private lateinit var selectedSubPackageId: String
    private lateinit var selectedTotalQuantity: String


    private lateinit var selectedPaymentMethod: String
    private lateinit var selectedPaymentMethodResponse: String
    private lateinit var selectedSubscriptionName: String
    private lateinit var selectedAddressName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        eSewaConfiguration = ESewaConfiguration()
            .clientId(Constants.MERCHANT_ID)
            .secretKey(Constants.MERCHANT_SECRET_KEY)
            .environment(CONFIG_ENVIRONMENT)


        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        binding = ActivityConfirmSubscriptionBinding.inflate(layoutInflater)
        userPrefManager = UserPrefManager(this)

        binding.headerTitle.text = "Confirm your Subscription"
        binding.returnHome.setOnClickListener {
            finish()
        }


        selectedBranchId = intent.getStringExtra("selectedBranchId").toString()
        selectedAddressId = intent.getStringExtra("selectedAddressId").toString()
        selectedSubscribedTotalAmount =
            intent.getStringExtra("selectedSubscribedTotalAmount").toString()
        selectedSubscribedDiscount = intent.getStringExtra("selectedSubscribedDiscount").toString()
        selectedSubscribedPrice = intent.getStringExtra("selectedSubscribedPrice").toString()
        selectedUnitPerDay = intent.getStringExtra("selectedUnitPerDay").toString()
        selectedStartingDate = intent.getStringExtra("selectedStartingDate").toString()
        selectedDeliveryPeroid = intent.getStringExtra("selectedDeliveryPeroid").toString()
        selectedSubPackageId = intent.getStringExtra("selectedSubPackageId").toString()
        selectedTotalQuantity = intent.getStringExtra("selectedTotalQuantity").toString()
        selectedSubscriptionName = intent.getStringExtra("selectedSubscriptionName").toString()
        selectedAddressName = intent.getStringExtra("selectedAddressName").toString()
        selectedPaymentMethod = intent.getStringExtra("selectedPaymentMethod").toString()

        if (selectedDeliveryPeroid == getString(R.string.morning_shift)) {
            selectedDeliveryPeroidResponse = "0"
        } else if (selectedDeliveryPeroid == getString(R.string.evening_shift)) {
            selectedDeliveryPeroidResponse = "1"
        } else if (selectedDeliveryPeroid == getString(R.string.both_shift)) {
            selectedDeliveryPeroidResponse = "2"
        }



        Log.i(
            "TAG", "onCreate: |$selectedAddressName $selectedDeliveryPeroid " +
                    "$selectedSubscribedDiscount $selectedBranchId $selectedAddressId $selectedSubscribedTotalAmount " +
                    "$selectedSubscribedPrice $selectedUnitPerDay $selectedStartingDate  $selectedSubPackageId $selectedTotalQuantity"
        )


        val body = RequestBodies.AddSubscription(
            selectedBranchId,
            selectedAddressId,
            selectedSubscribedTotalAmount,
            selectedSubscribedDiscount,
            selectedSubscribedPrice,
            selectedUnitPerDay,
            selectedStartingDate,
            selectedDeliveryPeroidResponse,
            selectedSubPackageId,
            selectedTotalQuantity
        )

        binding.customerAddress.text = selectedAddressName
        binding.packageName.text = selectedSubscriptionName

        binding.packageTotalCost.text = getString(R.string.rs) + selectedSubscribedTotalAmount
        binding.totalQuantityRequired.text = selectedUnitPerDay + " litre"
        binding.totalDiscountReceived.text = getString(R.string.rs) + selectedSubscribedDiscount
        binding.totalPriceAfterDiscountReceived.text =
            getString(R.string.rs) + selectedSubscribedPrice

        binding.paymentMethod.text = selectedPaymentMethod
        binding.paymentTo.text = userPrefManager.accountName



        binding.paymentQr.load(BASE_URL + userPrefManager.qrPath)

        binding.holderName.text=userPrefManager.accountHolderName

        binding.bankName.text=userPrefManager.bankName
        binding.accountNumber.text=userPrefManager.bankAccountNo

        Log.i("TAG", "onCreate: "+selectedPaymentMethod)
        if (selectedPaymentMethod == getString(R.string.by_bank_account)) {
           binding.bankLayout.visibility=View.VISIBLE
            binding.proceedWithPayment.visibility=View.VISIBLE
        } else if (selectedPaymentMethod == getString(R.string.by_qr)) {
            binding.qrLayout.visibility=View.VISIBLE

            binding.proceedWithPayment.visibility=View.VISIBLE
        } else if (selectedPaymentMethod == getString(R.string.by_esewa)) {
         binding.payByEsewa.visibility=View.VISIBLE
        } else if (selectedPaymentMethod == getString(R.string.cash_on_delivery)) {
            binding.proceedWithPayment.visibility=View.VISIBLE
        }


      binding.proceedWithPayment.setOnClickListener {
          tokenManager?.let { it1 -> viewModel.addSubscription(it1,body) }
          viewModel.addSubscription.observe(this, Observer { response ->
              when (response) {
                  is Resource.Success -> {
                      hideProgressBar()
                      response.data?.let {
                          userPrefManager.subscriptionStatus = true



                      }
                  }

                  is Resource.Error -> {
                      hideProgressBar()
                      response.message?.let { message ->
                          binding.addSubscriptionLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                      }
                  }
                  is Resource.Loading -> {
                      showProgressBar()
                  }
              }
          })
      }


        setupViewModel()
        setContentView(binding.root)

    }

    private fun makePayment(amount: String) {
        val eSewaPayment = ESewaPayment(
            amount,
            "someProductName",
            "someUniqueId_" + System.nanoTime(),
            "https://somecallbackurl.com"
        )
        val intent = Intent(this@ConfirmSubscription, ESewaPaymentActivity::class.java)
        intent.putExtra(ESewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration)
        intent.putExtra(ESewaPayment.ESEWA_PAYMENT, eSewaPayment)
        startActivityForResult(
            intent,
            REQUEST_CODE_PAYMENT
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                val s = data?.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE)
                Log.i("Proof of Payment", s!!)
                startActivity(Intent(this@ConfirmSubscription, ConfirmSubscription::class.java))

                Toast.makeText(this, "SUCCESSFUL PAYMENT", Toast.LENGTH_SHORT).show()
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled By User", Toast.LENGTH_SHORT).show()
            } else if (resultCode == ESewaPayment.RESULT_EXTRAS_INVALID) {
                val s = data?.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE)
                Log.i("Proof of Payment", s!!)
            }
        }
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(SubscriptionViewModel::class.java)
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