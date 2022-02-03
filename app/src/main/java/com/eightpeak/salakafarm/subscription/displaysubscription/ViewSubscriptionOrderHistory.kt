package com.eightpeak.salakafarm.subscription.displaysubscription

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.LayoutSubscriptionOrderHistoryBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.subscription.displaysubscription.models.MoreInfo1
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.GeneralUtils.getFormattedDate
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.SubscriptionViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class ViewSubscriptionOrderHistory : AppCompatActivity() {
    private lateinit var binding: LayoutSubscriptionOrderHistoryBinding
    private lateinit var viewModel: SubscriptionViewModel
    lateinit var userPrefManager: UserPrefManager
    private var tokenManager: TokenManager? = null

    private var datePickerDialog: DatePickerDialog? = null
    var dateSelected: Calendar = Calendar.getInstance()

    private  var selectedStartingDate: String="0"
    private  var selectedToDate: String="0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        binding = LayoutSubscriptionOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.header.text = "Subscription History"
        binding.returnHome.setOnClickListener { finish() }
        userPrefManager = UserPrefManager(this)

       tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        binding.searchHistory.setOnClickListener {
            getChooseDate()

        }
        setupViewModel()
    }

    private fun getChooseDate() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.choose_date)
        val chooseStartDate = dialog.findViewById<Button>(R.id.choose_start_date)
        val chooseToDate = dialog.findViewById<Button>(R.id.choose_to_date)
        val searchHistory = dialog.findViewById<Button>(R.id.search_history)

       chooseStartDate.setOnClickListener {
           val newCalendar = dateSelected
           val dateFormat: java.text.DateFormat? = DateFormat.getDateFormat(applicationContext)
           datePickerDialog = DatePickerDialog(
               this,
               {
                       view, year, monthOfYear, dayOfMonth ->
                   dateSelected[year, monthOfYear, dayOfMonth, 0] = 0
                   chooseStartDate.text = dateFormat?.format(dateSelected.time)
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
        chooseToDate.setOnClickListener {
            val newCalendar = dateSelected
            val dateFormat: java.text.DateFormat? = DateFormat.getDateFormat(applicationContext)
            datePickerDialog = DatePickerDialog(
                this,
                {
                        view, year, monthOfYear, dayOfMonth ->
                    dateSelected[year, monthOfYear, dayOfMonth, 0] = 0
                    chooseToDate.text = dateFormat?.format(dateSelected.time)
                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                    selectedToDate = formatter.format(Date.parse(dateSelected.time.toString()))
                },
                newCalendar[Calendar.YEAR],
                newCalendar[Calendar.MONTH],
                newCalendar[Calendar.DAY_OF_MONTH]
            )
            datePickerDialog!!.show()
            Log.i("TAG", "onCreate: " + dateSelected.time)
       }


        searchHistory.setOnClickListener {
           if(selectedStartingDate.isNotEmpty() && selectedToDate.isNotEmpty()){
               getPageDetails(selectedStartingDate,selectedToDate)
               dialog.dismiss()
           }
       }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

    }


    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(SubscriptionViewModel::class.java)
        val startDate = intent.getStringExtra("start")
        val endDate = intent.getStringExtra("end")
        getPageDetails(startDate,endDate)

    }



    private fun getPageDetails(startDate: String?, endDate: String?) {
        val historyDate =  RequestBodies.SubHistoryList(startDate!!, endDate!!)
        tokenManager?.let { viewModel.getSubHistory(it, historyDate) }
        viewModel.subHistory.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { history ->
                        if (history.moreInfo!!.isNotEmpty()) {
                       viewSubscriptionHistory(history.moreInfo)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.orderHistory.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun viewSubscriptionHistory(subscriptionHistory: List<MoreInfo1>) {
        binding.subOrderHistoryLayout.removeAllViews()
        for (i in subscriptionHistory.indices) {
            val itemView: View =
                LayoutInflater.from(this)
                    .inflate(R.layout.subscription_history_item, binding.subOrderHistoryLayout, false)
            val alterStatusHistory = itemView.findViewById<TextView>(R.id.alter_status_history)
            val subHistoryLayout = itemView.findViewById<CardView>(R.id.sub_history_layout)
            val historyDate = itemView.findViewById<TextView>(R.id.history_date)
            val alterQuantity = itemView.findViewById<TextView>(R.id.alter_quantity)
            val alterPeriod = itemView.findViewById<TextView>(R.id.alter_period)
            val orderDeliveredBy = itemView.findViewById<TextView>(R.id.order_delivered_by)
            val deliveredQuantity = itemView.findViewById<TextView>(R.id.delivered_quantity)
            val deliveredAt = itemView.findViewById<TextView>(R.id.delivered_at)
            val quantityView = itemView.findViewById<LinearLayout>(R.id.quantity_view)
            val deliveryAlterView = itemView.findViewById<LinearLayout>(R.id.delivery_alter_view)
            val deliveryHistoryView = itemView.findViewById<LinearLayout>(R.id.delivery_history_view)
            val alterDate = itemView.findViewById<LinearLayout>(R.id.alterDate)

            if(subscriptionHistory[i].deliveryAlter?.size==0&&subscriptionHistory[i].deliveryHistory?.size==0){
                subHistoryLayout.visibility=View.GONE
            }else{
                historyDate.text=subscriptionHistory[i].date+" ("+GeneralUtils.getUnicodeNumber(subscriptionHistory[i].date_nep)+")"

                if(subscriptionHistory[i].deliveryAlter?.size==0){
                    deliveryAlterView.visibility=View.GONE
                }else{
                    for (alterDelivery in subscriptionHistory[i].deliveryAlter!!){
                        if(alterDelivery.alter_status==1){
                            alterStatusHistory.text=getString(R.string.subscription_cancelled)
                            alterStatusHistory.setTextColor(Color.RED)
                            alterStatusHistory.textSize=15F
                            quantityView.visibility=View.GONE
                            alterDate.visibility=View.GONE
                        }else if(alterDelivery.alter_status==2){
                            alterStatusHistory.text=getString(R.string.milk_added)
                            alterStatusHistory.setTextColor(Color.GREEN)
                            if(subscriptionHistory[i].deliveryAlter?.size!=0)
                                alterPeriod.text= getFormattedDate(subscriptionHistory[i].deliveryAlter?.get(0)?.created_at)
                        }
                        alterQuantity.text=alterDelivery.qty.toString() +" "+ getString(R.string.litre)
                    }
                }

                if(subscriptionHistory[i].deliveryHistory?.size==0){
                    deliveryHistoryView.visibility=View.GONE
                }else {
                    if (subscriptionHistory[i].deliveryHistory?.size != 0) {
                        for (delivery in subscriptionHistory[i].deliveryHistory!!) {
                            orderDeliveredBy.text = delivery.employee?.name
                            deliveredQuantity.text =
                                delivery.qty.toString() +" "+ getString(R.string.litre)
                            if (subscriptionHistory[i].deliveryHistory?.size != 0)
                                deliveredAt.text =
                                    getFormattedDate(subscriptionHistory[i].deliveryHistory?.get(0)?.created_at)
                        }
                    }
                }
            }
            binding.subOrderHistoryLayout.addView(itemView)
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




}