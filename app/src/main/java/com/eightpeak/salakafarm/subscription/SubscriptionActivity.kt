package com.eightpeak.salakafarm.subscription

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eightpeak.salakafarm.views.addtocart.CartActivity
import android.app.DatePickerDialog
import android.graphics.Color
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import java.util.*
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.SubscriptionViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar

import androidx.lifecycle.Observer
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.subscription.attributes.SubscriptionItemModel
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import androidx.core.content.ContextCompat
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivitySubscriptionBinding
import com.eightpeak.salakafarm.subscription.attributes.Branches
import com.eightpeak.salakafarm.subscription.attributes.Sub_packages
import com.eightpeak.salakafarm.subscription.confirmSubscription.ConfirmSubscription
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.esewa.android.sdk.payment.ESewaConfiguration
import com.esewa.android.sdk.payment.ESewaPayment
import com.esewa.android.sdk.payment.ESewaPaymentActivity


class SubscriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubscriptionBinding
    private lateinit var viewModel: SubscriptionViewModel
    private var _binding: ActivitySubscriptionBinding? = null
    var dateSelected: Calendar = Calendar.getInstance()
     private lateinit var branchesType:String
    lateinit var userPrefManager: UserPrefManager
    private var tokenManager: TokenManager? = null
    private var datePickerDialog: DatePickerDialog? = null

    private val CONFIG_ENVIRONMENT: String = ESewaConfiguration.ENVIRONMENT_TEST
    private val REQUEST_CODE_PAYMENT = 1
    private var eSewaConfiguration: ESewaConfiguration? = null

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
        binding = ActivitySubscriptionBinding.inflate(layoutInflater)
        userPrefManager = UserPrefManager(this)

        binding.headerTitle.text = "Add Your Subscription Plan"
        binding.returnHome.setOnClickListener {
            finish()
        }
        binding.goToCart.setOnClickListener {
            val mainActivity = Intent(this, CartActivity::class.java)
            startActivity(mainActivity)
            finish()
        }
        setupViewModel()


        binding.chooseSubscriptionDate.setOnClickListener {
            val newCalendar = dateSelected
            val dateFormat: java.text.DateFormat? = DateFormat.getDateFormat(applicationContext)
            datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    dateSelected[year, monthOfYear, dayOfMonth, 0] = 0
                    binding.chooseSubscriptionDate.text = dateFormat?.format(dateSelected.time)
                },
                newCalendar[Calendar.YEAR],
                newCalendar[Calendar.MONTH],
                newCalendar[Calendar.DAY_OF_MONTH]
            )
            datePickerDialog!!.show()
            Log.i("TAG", "onCreate: " + dateSelected.time)

        }

        setContentView(binding.root)

    }


    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(SubscriptionViewModel::class.java)
        getSubscriptionItemList()
        setUpBranchesView()
        getAddressList()
    }



    private fun getAddressList() {
        var addressListModel: List<String> =
            userPrefManager.addressList.split("\n") as List<String>
        Log.i("TAG", "viewAddress: "+addressListModel.size)
        binding.customerAddress.text=addressListModel[0]
        binding.customerDelivery.text=userPrefManager.firstName+" "+userPrefManager.lastName

    }



    private fun setUpBranchesView() {
//        getBranchesList
        tokenManager?.let { it1 -> viewModel.getBranchesList(it1) }

        viewModel.branchesResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let {
                        displayBranchList(response.data.branches)
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

    private fun displayBranchList(branches: List<Branches>) {
        binding.getBranchesList.removeAllViews()

        for (i in branches.indices) {
            val itemView: View =
                LayoutInflater.from(this)
                    .inflate(R.layout.branch_items, binding.getBranchesList, false)

            val branchName = itemView.findViewById<TextView>(R.id.branch_name)
            val branchLocation = itemView.findViewById<TextView>(R.id.branch_location)
            val branchContact = itemView.findViewById<TextView>(R.id.branch_contact)
            val branchCard = itemView.findViewById<CardView>(R.id.branch_cart)
            val branchType = itemView.findViewById<TextView>(R.id.branch_type)
            val milk_cartoon = itemView.findViewById<RadioGroup>(R.id.milk_cartoon)
            milk_cartoon.visibility=View.GONE
            branchName.text = branches[i].name
            branchLocation.text = branches[i].address
            branchContact.text = branches[i].contact
            branchCard.setOnClickListener {
                binding.branchSelected.text = getString(R.string.selected_branch) + branches[i].name

                getSubscriptionPackageList(branches[i].id)
                binding.layoutSubItem.visibility=View.GONE
                binding.getBranchesList.visibility = View.GONE
                binding.subPackageView.visibility = View.VISIBLE
                binding.deliverToLayout.visibility = View.VISIBLE
                binding.paymentLayout.visibility = View.VISIBLE
                binding.proceedSubscription.visibility = View.VISIBLE
            }
            if(branches[i].branch_status == 0){
                branchType.text=getString(R.string.main_branch)
                branchesType="0"
                binding.proceedWithCheckout.setOnClickListener {
                    makePayment("100")
                }
            }else{
                branchesType="1"
                branchType.text=getString(R.string.partner_branch)
                binding.proceedWithCheckout.setOnClickListener {
                    val mainActivity = Intent(this@SubscriptionActivity, ConfirmSubscription::class.java)
                    startActivity(mainActivity)
                }
            }
            binding.getBranchesList.addView(itemView)
        }
    }

    private fun getSubscriptionPackageList(s: Int) {

        tokenManager?.let { it1 -> viewModel.getSubPackageList(it1, s) }

        viewModel.subPackageList.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let {
                        displayPackageList(response.data.sub_packages)

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

    private fun displayPackageList(subItem: List<Sub_packages>) {
        binding.layoutSubPackage.removeAllViews()
        Log.i("TAG", "displayPackageList: $subItem")
        for (i in subItem.indices) {
            val itemView: View =
                LayoutInflater.from(this)
                    .inflate(R.layout.layout_sub_package_item, binding.layoutSubPackage, false)

            val subPackageTitle = itemView.findViewById<TextView>(R.id.tv_Subscription_Name)
            val subPackagePrice = itemView.findViewById<TextView>(R.id.tv_Subscription_Price)
            val subPackageDays = itemView.findViewById<TextView>(R.id.tv_Subscription_Days)
            val selectSubPackage = itemView.findViewById<CardView>(R.id.select_sub_package)
            val subPackageDiscount = itemView.findViewById<TextView>(R.id.tv_Subscription_Discount)
            val subItemThumbnail = itemView.findViewById<ImageView>(R.id.sub_item_thumbnail)

            val subItemSelected = itemView.findViewById<TextView>(R.id.sub_item_selected)

            subItemThumbnail.load(BASE_URL + subItem[i].sub_item.image)
            subPackageTitle.text = subItem[i].name
            subPackagePrice.text = getString(R.string.rs) + " " + subItem[i].unit_price.toString()
            subPackageDays.text = subItem[i].number_of_days.toString() + " days"
            subPackageDiscount.text = subItem[i].discount_price_per_unit.toString() + "%"
            selectSubPackage.setOnClickListener {
                userPrefManager.packageSelected = i
            }

            if (userPrefManager.language.equals("ne")) {
                subItemSelected.text =subItem[i].sub_item.descriptions[1].title
            } else {
                subItemSelected.text = subItem[i].sub_item.descriptions[0].title
            }

            binding.layoutSubPackage.addView(itemView)
        }
    }

    private fun getSubscriptionItemList() {

        binding.layoutSubItem.removeAllViews()
        tokenManager?.let { it1 -> viewModel.getSubItemList(it1) }

        viewModel.subItemList.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let {
                        displaySubItemList(response.data)
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

    private fun displaySubItemList(data: SubscriptionItemModel) {
        for (i in data.sub_item.indices) {
            val itemView: View =
                LayoutInflater.from(this)
                    .inflate(R.layout.sub_items, binding.layoutSubItem, false)
            val subItemTitle = itemView.findViewById<TextView>(R.id.sub_item_title)
            val subItemPrice = itemView.findViewById<TextView>(R.id.sub_item_price)
            val subItemThumbnail = itemView.findViewById<ImageView>(R.id.sub_item_thumbnail)
            val subItemDescription = itemView.findViewById<TextView>(R.id.sub_item_description)
            val subItemView = itemView.findViewById<CardView>(R.id.sub_item_view)
            binding.subItemSelected.text =
                "Selected Subscription Item :- " + data.sub_item[i].descriptions[0].title

            subItemView.setOnClickListener {
                userPrefManager.subSelected = i
                getSubscriptionPackageList(data.sub_item[i].descriptions[0].sub_item_id)
                 binding.subItemSlide.visibility = View.GONE
                binding.subPackageView.visibility = View.VISIBLE
                binding.proceedSubscription.visibility = View.VISIBLE
            }

            if (i == userPrefManager.subSelected) {
                subItemView.setBackgroundColor(ContextCompat.getColor(this, R.color.sub_color))
            } else {
                subItemView.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            }
            if (data.sub_item.isNotEmpty()) {
                if (userPrefManager.language.equals("ne")) {
                    subItemTitle.text = data.sub_item[i].descriptions[1].title
                    subItemDescription.text = data.sub_item[i].descriptions[1].description
                    subItemPrice.text =
                        getString(R.string.rs) + GeneralUtils.getUnicodeNumber(data.sub_item[i].price.toString()) + " per litre"
                } else {
                    subItemTitle.text = data.sub_item[i].descriptions[0].title
                    subItemDescription.text = data.sub_item[i].descriptions[0].description
                    subItemPrice.text =
                        getString(R.string.rs) + data.sub_item[i].price.toString() + " per litre"
                }
            }
            subItemThumbnail.load(BASE_URL + data.sub_item[i].image)
            binding.layoutSubItem.addView(itemView)
        }

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

    private fun makePayment(amount: String) {
        val eSewaPayment = ESewaPayment(
            amount,
            "someProductName",
            "someUniqueId_" + System.nanoTime(),
            "https://somecallbackurl.com"
        )
        val intent = Intent(this@SubscriptionActivity, ESewaPaymentActivity::class.java)
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
                startActivity(Intent(this@SubscriptionActivity, ConfirmSubscription::class.java))

                Toast.makeText(this, "SUCCESSFUL PAYMENT", Toast.LENGTH_SHORT).show()
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled By User", Toast.LENGTH_SHORT).show()
            } else if (resultCode == ESewaPayment.RESULT_EXTRAS_INVALID) {
                val s = data?.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE)
                Log.i("Proof of Payment", s!!)
            }
        }
    }

}