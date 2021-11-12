package com.eightpeak.salakafarm.subscription

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eightpeak.salakafarm.databinding.ActivitySubscriptionBinding
import com.eightpeak.salakafarm.views.addtocart.CartActivity
import android.app.DatePickerDialog
import android.util.Log
import android.view.View
import java.util.*
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.SubscriptionViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar

import androidx.lifecycle.Observer
import com.hadi.retrofitmvvm.util.errorSnack

class SubscriptionActivity:AppCompatActivity() {

    private lateinit var binding: ActivitySubscriptionBinding
    private lateinit var viewModel: SubscriptionViewModel
    private var _binding: ActivitySubscriptionBinding? = null
    var dateSelected: Calendar = Calendar.getInstance()

    private var datePickerDialog: DatePickerDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySubscriptionBinding.inflate(layoutInflater)

        binding.headerTitle.text="Add Your Subscription Plan"
        binding.returnHome.setOnClickListener {
            val mainActivity = Intent(this, SubscriptionActivity::class.java)
            startActivity(mainActivity)
            finish()
        }
        binding.goToCart.setOnClickListener {
            val mainActivity = Intent(this, CartActivity::class.java)
            startActivity(mainActivity)
            finish()
        }
       setupViewModel()
       getSubscriptionPackageList("1")






        binding.chooseSubscriptionDate.setOnClickListener {
            val newCalendar = dateSelected
            datePickerDialog = DatePickerDialog(this,
                { view, year, monthOfYear, dayOfMonth ->
                    dateSelected[year, monthOfYear, dayOfMonth, 0] = 0
//                    dateEditText.setText(dateFormatter.format(dateSelected.time))
                },
                newCalendar[Calendar.YEAR],
                newCalendar[Calendar.MONTH],
                newCalendar[Calendar.DAY_OF_MONTH]
            )
            datePickerDialog!!.show()
            Log.i("TAG", "onCreate: "+dateSelected.time)

        }
        setContentView(binding.root)

    }


    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(SubscriptionViewModel::class.java)
        getSubscriptionItemList()

    }
    private fun getSubscriptionPackageList(s: String) {


    }

    private fun getSubscriptionItemList() {
             viewModel.branchesResponse.observe(this, Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()

                        response.data?.let { picsResponse ->

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

    private fun hideProgressBar() {
//        progress.visibility = View.GONE
    }

    private fun showProgressBar() {
//        progress.visibility = View.VISIBLE
    }


    fun onProgressClick(view: View) {
        //Preventing Click during loading
    }


}