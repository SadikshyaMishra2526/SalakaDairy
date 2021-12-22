package com.eightpeak.salakafarm.subscription

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eightpeak.salakafarm.views.addtocart.CartActivity
import android.app.DatePickerDialog
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
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
import com.eightpeak.salakafarm.subscription.attributes.Branches
import com.eightpeak.salakafarm.subscription.attributes.Sub_packages
import com.eightpeak.salakafarm.utils.GeneralUtils
import android.content.DialogInterface
import com.eightpeak.salakafarm.databinding.ActivitySubscriptionBinding
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.utils.subutils.addAddressSnack
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.views.addresslist.Address_list

class SubscriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubscriptionBinding
    private lateinit var viewModel: SubscriptionViewModel
    private var _binding: ActivitySubscriptionBinding? = null
    var dateSelected: Calendar = Calendar.getInstance()
     private lateinit var branchesType:String
    lateinit var userPrefManager: UserPrefManager
    private var tokenManager: TokenManager? = null
    private var datePickerDialog: DatePickerDialog? = null
    private var  addressList: List<Address_list>? = null


    private lateinit var selectedBranchId:String
    private lateinit var selectedAddressId:String
    private lateinit var selectedSubscribedTotalAmount:String
    private lateinit var selectedSubscribedDiscount:String
    private lateinit var selectedSubscribedPrice:String
    private lateinit var selectedUnitPerDay:String
    private lateinit var selectedStartingDate:String
    private lateinit var selectedDeliveryPeroid:String
    private lateinit var selectedSubPackageId:String
    private lateinit var selectedTotalQuantity:String

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
                    selectedStartingDate= dateFormat?.format(dateSelected.time).toString()
                },
                newCalendar[Calendar.YEAR],
                newCalendar[Calendar.MONTH],
                newCalendar[Calendar.DAY_OF_MONTH]
            )
            datePickerDialog!!.show()
            Log.i("TAG", "onCreate: " + dateSelected.time)

        }
           binding.customerDelivery.text=userPrefManager.firstName+" "+userPrefManager.lastName
        selectedUnitPerDay=binding.unitPerDay.toString()

        setContentView(binding.root)


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
                        if(picsResponse.address_list.isNotEmpty()){
                            addressList=picsResponse.address_list
                          showAddressList(picsResponse.address_list)
                        }else{
                            binding.addSubscriptionLayout.addAddressSnack(this@SubscriptionActivity,"Address List Empty,Please add your address", Snackbar.LENGTH_LONG)
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
                        displayBranchList(response.data.branches,customerLat,customerLng)
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
                binding.branchSelected.text = getString(R.string.selected_branch)+":-" + branches[i].name
                 selectedBranchId= branches[i].id.toString()
                getSubscriptionPackageList(branches[i].id)
                binding.layoutSubItem.visibility=View.GONE
                binding.getBranchesList.visibility = View.GONE
                binding.subPackageView.visibility = View.VISIBLE
                binding.deliverToLayout.visibility = View.VISIBLE
                binding.paymentLayout.visibility = View.VISIBLE
                binding.proceedSubscription.visibility = View.VISIBLE
            }

                 var distance: Float = GeneralUtils.calculateDistance(
                     customerLat,
                     customerLng,
                     branches[i].latitude,
                     branches[i].longitude
                 )
                 Log.i("TAG", "displayBranchList: "+distance+"   "+   customerLat+"   "+
                     customerLng+"   "+
                     branches[i].latitude+"   "+
                     branches[i].longitude)

            if(branches[i].branch_status == 0){
                branchType.text=getString(R.string.main_branch)
                branchesType="0"

            }else{
                branchesType="1"
                branchType.text=getString(R.string.partner_branch)

            }

//            val selectedId: Int = binding.paymentMethod.checkedRadioButtonId
//
//            val radioButton = findViewById<View>(selectedId) as RadioButton
//
//            Toast.makeText(
//                this@SubscriptionActivity,
//                radioButton.text, Toast.LENGTH_SHORT
//            ).show()

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
        binding.changeLocation.setOnClickListener {
            addressList?.let { it1 ->
                showAddressList(it1) }
        }
        Log.i("TAG", "displayPackageList: $subItem")
        if(subItem.isNotEmpty()){
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

                subPackageTitle.text = subItem[i].name
                subPackagePrice.text = getString(R.string.rs) + " " + subItem[i].unit_price.toString()
                subPackageDays.text = subItem[i].number_of_days.toString() + " days"
                subPackageDiscount.text ="Dis. Range:- "+ subItem[i].range.toString()+ " ("+subItem[i].discount_price_per_unit.toString() + "%"+")"
                subItemThumbnail.load(BASE_URL+subItem[i].sub_item.image.toString() )
                selectSubPackage.setOnClickListener {
                Toast.makeText(this@SubscriptionActivity,"cccccccccccccccccccccccccc",Toast.LENGTH_SHORT)
                    userPrefManager.packageSelected = subItem[i].sub_item_id
                    selectedSubPackageId=subItem[i].id.toString()
                    if(binding.unitPerDay.text.isNotEmpty()){
                        val quantity=Integer.parseInt(binding.unitPerDay.text.toString())
                        if(quantity in 2..4){
                           val totalValue: Int =quantity*subItem[i].unit_price
                            val discounted: Int =totalValue-(totalValue*(subItem[i].discount_price_per_unit/100))
                            Log.i("TAG", "displayPackageList: "+totalValue+" "+discounted)
                            selectedSubscribedTotalAmount= discounted.toString()
                            selectedSubscribedDiscount=subItem[i].discount_price_per_unit.toString()
                            selectedSubscribedPrice=subItem[i].unit_price.toString()
                        }else{
                            val totalValue: Int =quantity*subItem[i].unit_price
                            val discounted: Int=0
                            selectedSubscribedTotalAmount= subItem[i].unit_price.toString()
                            selectedSubscribedDiscount=subItem[i].discount_price_per_unit.toString()
                            selectedSubscribedPrice=subItem[i].unit_price.toString()

                            Log.i("TAG", "displayPackageList: "+totalValue+" "+discounted)

                        }
                    }
                    binding.proceedWithCheckout.setOnClickListener {
//            val intent = Intent(this, ConfirmSubscription::class.java)
//            intent.putExtra("selectedBranchId",selectedBranchId)
//            intent.putExtra("selectedAddressId",selectedAddressId)
//            intent.putExtra("selectedSubscribedTotalAmount",selectedSubscribedTotalAmount)
//            intent.putExtra("selectedSubscribedDiscount",selectedSubscribedDiscount)
//            intent.putExtra("selectedSubscribedPrice",selectedSubscribedPrice)
//            intent.putExtra("selectedUnitPerDay",selectedUnitPerDay)
//            intent.putExtra("selectedStartingDate",selectedStartingDate)
//            intent.putExtra("selectedDeliveryPeriod",selectedDeliveryPeroid)
//            intent.putExtra("selectedSubPackageId",selectedSubPackageId)
//            intent.putExtra("selectedTotalQuantity",selectedTotalQuantity)
//            startActivity(intent)
//            finish()
                        val body= RequestBodies.AddSubscription(selectedBranchId,selectedAddressId,selectedSubscribedTotalAmount,selectedSubscribedDiscount,selectedSubscribedPrice,selectedUnitPerDay,selectedStartingDate,selectedDeliveryPeroid,selectedSubPackageId,selectedTotalQuantity)

                        Log.i("TAG", "onCreate: $body")
                    }
                }
                binding.layoutSubPackage.addView(itemView)
            }
        }else{
            binding.addSubscriptionLayout.errorSnack("This branch doesn't have subscription option..please choose another...", Snackbar.LENGTH_LONG)
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


    private fun showAddressList(addressList: List<Address_list>) {
        if (addressList.size==1) {
            val customerLat : Double = 27.699972326072345
            val customerLng : Double = 85.36797715904281
            selectedAddressId=addressList[0].id.toString()
            getSubscriptionItemList()
            setUpBranchesView(customerLat,customerLng)

        }else{

        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Selected Delivery Location")
        val addressId = arrayOfNulls<String>(addressList.size)
        val names = arrayOfNulls<String>(addressList.size)
        val checkedItems = BooleanArray(addressList.size)
        val latList = BooleanArray(addressList.size)
        val lngList = BooleanArray(addressList.size)

            var i = 0
            for (key in addressList) {
                addressId[i]=key.id.toString()

//                latList[i]=key.lat.toString()
//                lngList[i]=key.lng.toString()

                names[i] = key.address1+", "+key.address2+", "+"\n"+key.phone
                checkedItems[i] = false
                i += 1
            }
            builder.setMultiChoiceItems(names, checkedItems
            ) { _, which, isChecked ->
                checkedItems[which] = isChecked
            }
            builder.setPositiveButton("OK",
                DialogInterface.OnClickListener { _, _ ->
                    for (i in checkedItems.indices) {
                        if (checkedItems[i]) {
                           binding.customerLocation.text=names[i]
                            val customerLat : Double = 27.699972326072345
                            val customerLng : Double = 85.36797715904281
                            selectedAddressId= addressId[i].toString()
                            getSubscriptionItemList()
                            setUpBranchesView(customerLat,customerLng)
                        }
                    }
                })
            builder.setNegativeButton("Cancel", null)

            // create and show the alert dialog
            val dialog: android.app.AlertDialog? = builder.create()
            dialog?.show()
        }
    }


}