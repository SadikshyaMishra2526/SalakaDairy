package com.eightpeak.salakafarm.subscription

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivitySubscriptionBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.subscription.attributes.Branches
import com.eightpeak.salakafarm.subscription.attributes.Sub_packages
import com.eightpeak.salakafarm.subscription.attributes.SubscriptionItemModel
import com.eightpeak.salakafarm.subscription.confirmSubscription.ConfirmSubscription
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.addAddressSnack
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.SubscriptionViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.addresslist.Address_list
import com.eightpeak.salakafarm.views.addtocart.CartActivity
import com.google.android.material.snackbar.Snackbar
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig
import uk.co.deanwild.materialshowcaseview.shape.RectangleShape
import uk.co.deanwild.materialshowcaseview.shape.Shape
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SubscriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubscriptionBinding
    private lateinit var viewModel: SubscriptionViewModel
    private var _binding: ActivitySubscriptionBinding? = null
    var dateSelected: Calendar = Calendar.getInstance()
    private lateinit var branchesType: String
    lateinit var userPrefManager: UserPrefManager
    private var tokenManager: TokenManager? = null
    private var datePickerDialog: DatePickerDialog? = null
    private var addressList: List<Address_list>? = null


    private lateinit var selectedBranchId: String
    private lateinit var selectedAddressId: String
    private  var selectedSubscribedTotalAmount: String="0"
    private  var selectedSubscribedDiscount: String="0"
    private  var selectedSubscribedPrice: String="0"
    private lateinit var selectedUnitPerDay: String
    private  var selectedStartingDate: String="0"
    private  var selectedDeliveryPeroid: String="0"
    private  var selectedSubPackageId: String="0"
    private  var selectedTotalQuantity: String="0"

    private  var selectedPaymentMethod: String="0"
    private  var selectedSubscriptionName: String="0"
    private lateinit var selectedAddressName: String

    private lateinit var deliveryPeriodRadio: RadioButton
    private lateinit var paymentOptionRadio: RadioButton

    private  val TAG: String="SubscriptionActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        binding = ActivitySubscriptionBinding.inflate(layoutInflater)
        userPrefManager = UserPrefManager(this)

        binding.headerTitle.text = getString(R.string.add_subscription_plan_header)
        binding.returnHome.setOnClickListener {
            finish()
        }
        binding.goToCart.setOnClickListener {
            val mainActivity = Intent(this@SubscriptionActivity, CartActivity::class.java)
            startActivity(mainActivity)
            finish()
        }
        setupViewModel()

        binding.chooseSubscriptionDate.setOnClickListener {
            val newCalendar = dateSelected
            val dateFormat: java.text.DateFormat? = DateFormat.getDateFormat(applicationContext)
            datePickerDialog = DatePickerDialog(
                this,
                {
                        view, year, monthOfYear, dayOfMonth ->
                    dateSelected[year, monthOfYear, dayOfMonth, 0] = 0
                    binding.chooseSubscriptionDate.text = dateFormat?.format(dateSelected.time)
                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                    selectedStartingDate = formatter.format(Date.parse(dateSelected.time.toString()))
                },
                newCalendar[Calendar.YEAR],
                newCalendar[Calendar.MONTH],
                newCalendar[Calendar.DAY_OF_MONTH]
            )
            datePickerDialog!!.show()
            Log.i("TAG", "onCreate: " + dateSelected.time)

        }
        binding.customerDelivery.text = userPrefManager.firstName + " " + userPrefManager.lastName
        selectedUnitPerDay = binding.unitPerDay.text.toString()



        binding.proceedWithCheckout.setOnClickListener {

            val deliveryPeriodId: Int = binding.deliveryPeriod.checkedRadioButtonId
            val paymentMethodId: Int = binding.paymentMethod.checkedRadioButtonId

            if (binding.morningShift.isChecked || binding.eveningShift.isChecked|| binding.bothShift.isChecked) {
                deliveryPeriodRadio = findViewById<View>(deliveryPeriodId) as RadioButton
                selectedDeliveryPeroid = deliveryPeriodRadio.text.toString()
            } else {
                binding.addSubscriptionLayout.errorSnack(getString(R.string.please_select_delivery_shift), Snackbar.LENGTH_LONG)
             }

            if (binding.byBank.isChecked || binding.byEsewa.isChecked|| binding.byQr.isChecked|| binding.cashOnDelivery.isChecked) {
                paymentOptionRadio = findViewById<View>(paymentMethodId) as RadioButton
                selectedPaymentMethod = paymentOptionRadio.text.toString()
            } else {
                binding.addSubscriptionLayout.errorSnack(getString(R.string.please_payment_method), Snackbar.LENGTH_LONG)
             }


            if (!validateSubscription()) {
                val intent = Intent(this@SubscriptionActivity, ConfirmSubscription::class.java)
                intent.putExtra("selectedBranchId", selectedBranchId)
                intent.putExtra("selectedAddressId", selectedAddressId)
                intent.putExtra("selectedSubscribedTotalAmount", selectedSubscribedTotalAmount)
                intent.putExtra("selectedSubscribedDiscount", selectedSubscribedDiscount)
                intent.putExtra("selectedSubscribedPrice", selectedSubscribedPrice)
                intent.putExtra("selectedUnitPerDay", binding.unitPerDay.text.toString())
                intent.putExtra("selectedStartingDate", selectedStartingDate)
                intent.putExtra("selectedDeliveryPeroid", selectedDeliveryPeroid)
                intent.putExtra("selectedSubPackageId", selectedSubPackageId)
                intent.putExtra("selectedTotalQuantity", selectedTotalQuantity)
                intent.putExtra("selectedSubscriptionName", selectedSubscriptionName)
                intent.putExtra("selectedAddressName", selectedAddressName)
                intent.putExtra("selectedPaymentMethod", selectedPaymentMethod)
                startActivity(intent)
            }
        }

        setContentView(binding.root)

        showCase()
    }

    private fun validateSubscription(): Boolean {
        var validate = false
        if (selectedBranchId.isEmpty()) {
            binding.addSubscriptionLayout.errorSnack(getString(R.string.please_select_branch), Snackbar.LENGTH_LONG)
            validate = true
        }
        if (selectedAddressId.isEmpty()) {
            binding.addSubscriptionLayout.errorSnack("Please select Address", Snackbar.LENGTH_LONG)
            validate = true
        }
        if (selectedSubscribedTotalAmount=="0") {
            binding.addSubscriptionLayout.errorSnack("Please Add Subscription Total", Snackbar.LENGTH_LONG)
            validate = true
        }
        if (selectedSubscribedPrice=="0") {
            binding.addSubscriptionLayout.errorSnack("Please Add Subscription Price", Snackbar.LENGTH_LONG)
            validate = true
        }
        if ( binding.unitPerDay.text.toString().isEmpty()) {
            binding.addSubscriptionLayout.errorSnack("Please add Unit per day !!!", Snackbar.LENGTH_LONG)
            validate = true
        }
        if (selectedStartingDate=="0") {
            binding.addSubscriptionLayout.errorSnack("Please select starting date !!!", Snackbar.LENGTH_LONG)
            validate = true
        }
        if (selectedDeliveryPeroid=="0") {
            binding.addSubscriptionLayout.errorSnack("Please select delivery period!!!", Snackbar.LENGTH_LONG)
            validate = true
        }
        if (selectedSubPackageId=="0") {
            binding.addSubscriptionLayout.errorSnack("Please Add Subscription", Snackbar.LENGTH_LONG)
            validate = true
        }
        if (selectedPaymentMethod=="0") {
            binding.addSubscriptionLayout.errorSnack("Please Payment Method", Snackbar.LENGTH_LONG)
            validate = true
        }
        if (selectedSubscriptionName=="0") {
            binding.addSubscriptionLayout.errorSnack("Please Add Subscription Name", Snackbar.LENGTH_LONG)
            validate = true
        }
        if (selectedTotalQuantity=="0") {
            binding.addSubscriptionLayout.errorSnack("Please Add total", Snackbar.LENGTH_LONG)
            validate = true
        }

        return validate
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(SubscriptionViewModel::class.java)
        getAddressList()
    }

    private fun getAddressList() {
        tokenManager?.let { it1 -> viewModel.getUserAddressList(it1) }
        var addressListString = ""
        viewModel.userAddressList.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        if (picsResponse.address_list.isNotEmpty()) {
                            addressList = picsResponse.address_list
                            showAddressList(picsResponse.address_list)
                        } else {
                            binding.addSubscriptionLayout.addAddressSnack(
                                this@SubscriptionActivity,
                                "Address List Empty,Please add your address",
                                Snackbar.LENGTH_LONG
                            )
                        }
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


    private fun setUpBranchesView(customerLat: Double, customerLng: Double) {
//        getBranchesList
        tokenManager?.let { it1 -> viewModel.getBranchesList(it1) }

        viewModel.branchesResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let {
                        displayBranchList(response.data.branches, customerLat, customerLng)
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

    private fun displayBranchList(
        branches: List<Branches>,
        customerLat: Double,
        customerLng: Double
    ) {
        var branchesIndex=ArrayList<Double>(branches.size)
        var selectBranch=ArrayList<Branches>(branches.size)

        if(branches.isNotEmpty()){
          for(branch in branches){
              if(branch.sub_packages_count>0){
                  val distance: Double = GeneralUtils.calculateDistance(
                      customerLat,
                      customerLng,
                      branch.latitude,
                      branch.longitude)
                  branchesIndex.add(distance)
                  selectBranch.add(branch)
                  Log.i(
                      "TAG", "displayBranchList: " + distance + "   " + customerLat + "   " +
                              customerLng + "   " +
                              branch.latitude+" "+
                              branch.longitude+" "+branch.name
                  )

              }
          }

        }

        if(selectBranch[findMin(branchesIndex)]!=null){
            binding.mainView.visibility=View.VISIBLE
            binding.proceedWithCheckout.visibility=View.VISIBLE
           val branchSelected: Branches=selectBranch[findMin(branchesIndex)]
            binding.branchSelected.text = branchSelected.name
            selectedBranchId = branchSelected.id.toString()
            userPrefManager.bankAccountNo = branchSelected.account_number
            userPrefManager.qrPath = branchSelected.qrcode
            userPrefManager.accountName = branchSelected.name
            userPrefManager.accountHolderName = branchSelected.account_holder
            userPrefManager.bankName = branchSelected.bank
            getSubscriptionPackageList(branchSelected.id)

            binding.layoutSubItem.visibility = View.GONE
            binding.getBranchesList.visibility = View.GONE
            binding.subPackageView.visibility = View.VISIBLE
            binding.deliverToLayout.visibility = View.VISIBLE
            binding.paymentLayout.visibility = View.VISIBLE
            binding.proceedSubscription.visibility = View.VISIBLE
        }

        binding.changeBranch.setOnClickListener {
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
                 milk_cartoon.visibility = View.GONE
                 branchName.text = branches[i].name
                 branchLocation.text = branches[i].address
                 branchContact.text = branches[i].contact
                 branchCard.setOnClickListener {

                     binding.branchSelected.text = branches[i].name
                     selectedBranchId = branches[i].id.toString()
                     userPrefManager.bankAccountNo = branches[i].account_number
                     userPrefManager.qrPath = branches[i].qrcode
                     userPrefManager.accountName = branches[i].name
                     userPrefManager.accountHolderName = branches[i].account_holder
                     userPrefManager.bankName = branches[i].bank
                     getSubscriptionPackageList(branches[i].id)

                     binding.layoutSubItem.visibility = View.GONE
                     binding.getBranchesList.visibility = View.GONE
                     binding.subPackageView.visibility = View.VISIBLE
                     binding.deliverToLayout.visibility = View.VISIBLE
                     binding.paymentLayout.visibility = View.VISIBLE
                     binding.proceedSubscription.visibility = View.VISIBLE
                 }




                 if (branches[i].branch_status == 0) {
                     branchType.text = getString(R.string.main_branch)
                     branchesType = "0"

                 } else {
                     branchesType = "1"
                     branchType.text = getString(R.string.partner_branch)

                 }

                 binding.getBranchesList.addView(itemView)
             }
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
        binding.changeLocation.setOnClickListener {
            addressList?.let { it1 ->
                showAddressList(it1)
            }
        }
        Log.i("TAG", "displayPackageList: $subItem")
        if (subItem.isNotEmpty()) {
            for (i in subItem.indices) {
                var mLastView: View? = null
                val itemView: View =
                    LayoutInflater.from(this)
                        .inflate(R.layout.layout_sub_package_item, binding.layoutSubPackage, false)

                val subPackageTitle = itemView.findViewById<TextView>(R.id.tv_Subscription_Name)
                val subPackagePrice = itemView.findViewById<TextView>(R.id.tv_Subscription_Price)
                val subPackageDays = itemView.findViewById<TextView>(R.id.tv_Subscription_Days)
                val selectSubPackage = itemView.findViewById<CardView>(R.id.select_sub_package)
                val subPackageDiscount =
                    itemView.findViewById<TextView>(R.id.tv_Subscription_Discount)
                val subItemThumbnail = itemView.findViewById<ImageView>(R.id.sub_item_thumbnail)

                val tvSubscriptionTotalPrice =
                    itemView.findViewById<TextView>(R.id.tv_Subscription_total_price)
                val totalPrice = subItem[i].unit_price * subItem[i].number_of_days
                tvSubscriptionTotalPrice.text = getString(R.string.rs) + totalPrice.toString()
                subPackageTitle.text = subItem[i].name
                subPackagePrice.text =
                    getString(R.string.rs) + " " + subItem[i].unit_price.toString()
                subPackageDays.text = subItem[i].number_of_days.toString() + " days"
                subPackageDiscount.text =
                    "Dis. Range:- " + subItem[i].range.toString() + " ( " + getString(R.string.rs)+subItem[i].discount_price_per_unit.toString() + " per unit" + ")"
                subItemThumbnail.load(BASE_URL + subItem[i].sub_item.image.toString())


                selectSubPackage.setOnClickListener {
                    displaySelectedDetails(subItem[i],selectSubPackage)
                    userPrefManager.selectedPackage = subItem[i].id

                    binding.selectCardView.visibility = View.VISIBLE
                    selectedSubPackageId = subItem[i].id.toString()
                    selectedSubscriptionName = subItem[i].name
                    GeneralUtils.hideKeyboard(this)
                }



                binding.layoutSubPackage.addView(itemView)
            }
        } else {
            binding.addSubscriptionLayout.errorSnack(
                "This branch doesn't have subscription option..please choose another...",
                Snackbar.LENGTH_LONG
            )
        }
    }

    private fun displaySelectedDetails(subPackages: Sub_packages, selectSubPackage: CardView) {
        var totalPrice = subPackages.unit_price * subPackages.number_of_days
        binding.selectedPackage.text = subPackages.name
        if (binding.unitPerDay.text.toString().isNotEmpty()) {


            Log.i("TAG", "displayPackageList: " + userPrefManager.selectedPackage)

            if (subPackages.id==userPrefManager.packageSelected) {
                selectSubPackage.setCardBackgroundColor(getColor(R.color.sub_color))
            } else {
                selectSubPackage.setCardBackgroundColor(getColor(R.color.white))
            }

           val requiredQuantity= binding.unitPerDay.text.toString()
            selectedTotalQuantity = (Integer.parseInt(requiredQuantity)* subPackages.number_of_days).toString()

            selectedSubscribedTotalAmount = totalPrice.toString()
            binding.totalPackageCost.text = getString(R.string.rs) + totalPrice.toString()
            val unitPerDay = Integer.valueOf(binding.unitPerDay.text.toString())
            val str: String = subPackages.range;
            val arrOfStr: List<String> = str.split("-")
            for (range in arrOfStr)
            if (unitPerDay >= Integer.valueOf(arrOfStr[0])) {
                selectedSubscribedDiscount =
                    ((unitPerDay * subPackages.discount_price_per_unit)).toString()
                totalPrice -= Integer.parseInt(selectedSubscribedDiscount)
                selectedSubscribedPrice = totalPrice.toString()
                binding.totalCostWithDiscount.text = getString(R.string.rs) + totalPrice.toString()
                binding.packageDiscount.text =getString(R.string.rs)+ selectedSubscribedDiscount
            } else {
                binding.packageDiscount.text = "Rs 0"
                selectedSubscribedDiscount ="0"
                    binding.totalCostWithDiscount.text = getString(R.string.rs) + totalPrice.toString()
                selectedSubscribedPrice = totalPrice.toString()
            }
        } else {
            binding.addSubscriptionLayout.errorSnack(
                "Please add quantity first...",
                Snackbar.LENGTH_LONG
            )

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
                data.sub_item[i].descriptions[0].title

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


    private fun showAddressList(addressList: List<Address_list>) {
        if (addressList.size == 1) {

            val customerLat: Double = addressList[0].lat!!
            val customerLng: Double =addressList[0].lng!!
            selectedAddressId = addressList[0].id.toString()
            selectedAddressName = addressList[0].address1+" "+ addressList[0].address2+" "+ addressList[0].address3+" "+ addressList[0].phone.toString()
            binding.customerLocation.text =  addressList[0].address1+" "+ addressList[0].address2+" "+ addressList[0].address3+" "+ addressList[0].phone.toString()


            getSubscriptionItemList()
            setUpBranchesView(customerLat, customerLng)

        } else {

            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Selected Delivery Location")
            val addressId = arrayOfNulls<String>(addressList.size)
            val names = arrayOfNulls<String>(addressList.size)
            val checkedItems = BooleanArray(addressList.size)
            val latList = arrayOfNulls<Double>(addressList.size)
            val lngList = arrayOfNulls<Double>(addressList.size)

            var i = 0
            for (key in addressList) {
                addressId[i] = key.id.toString()

                latList[i]=key.lat
                lngList[i]=key.lng

                names[i] = key.address1 + ", " + key.address2 + ", " + "\n" + key.phone
                checkedItems[i] = false
                i += 1
            }
            builder.setMultiChoiceItems(
                names, checkedItems
            ) { _, which, isChecked ->
                checkedItems[which] = isChecked
            }
            builder.setPositiveButton("OK",
                DialogInterface.OnClickListener { _, _ ->
                    for (i in checkedItems.indices) {
                        if (checkedItems[i]) {
                            binding.customerLocation.text = names[i]
                            val customerLat:Double= latList[i]!!
                            val customerLng: Double =lngList[i]!!
                            selectedAddressId = addressId[i].toString()
                            selectedAddressName = names[i].toString()
                            getSubscriptionItemList()
                            setUpBranchesView(customerLat, customerLng)
                        }
                    }
                })
            builder.setNegativeButton("Cancel", null)

            // create and show the alert dialog
            val dialog: android.app.AlertDialog? = builder.create()
            dialog?.show()
        }
    }


    private fun findMin(array: ArrayList<Double>): Int {
        var min = array[0]
        var index = 0
        for (i in 1 until array.size) {
            if (min > array[i]) {
                min = array[i]
                index=i
            }
        }
        return index
    }

    private fun showCase() {
        val DEFAULT_SHAPE: Shape = RectangleShape(20, 20)
        val config = ShowcaseConfig()
        config.shape = DEFAULT_SHAPE
        config.delay = 400
        val sequence = MaterialShowcaseSequence(this, "102")
        sequence.setConfig(config)

        sequence.addSequenceItem(
            binding.chooseSubscriptionDate,
            getString(R.string.sc_subdate), getString(R.string.got_it)
        )
        sequence.addSequenceItem(
            binding.layoutSubPackage,
            getString(R.string.sc_package),  getString(R.string.got_it)
        )
        sequence.addSequenceItem(
            binding.deliveryPeriod,
            getString(R.string.sc_period),  getString(R.string.got_it)
        )
        sequence.addSequenceItem(
            binding.paymentMethod,
            getString(R.string.sc_payment),  getString(R.string.got_it)
        )
        sequence.addSequenceItem(
            binding.quantityView,
            getString(R.string.sc_unitPerDay),  getString(R.string.got_it)
        )
        sequence.addSequenceItem(
            binding.proceedWithCheckout,
            getString(R.string.sc_proceed),  getString(R.string.got_it)
        )
        sequence.start()
    }


}