package com.eightpeak.salakafarm.views.order.orderview.orderhistory

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.FragmentOrderHistoryBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.OrderViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import com.hadi.retrofitmvvm.util.errorSnack

class OrderHistory : AppCompatActivity() {

    private lateinit var viewModel: OrderViewModel
    private lateinit var binding: FragmentOrderHistoryBinding


    private var tokenManager: TokenManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.headerTitle.text = "Order History"

        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(OrderViewModel::class.java)
        getOrderHistory()
    }

    private fun getOrderHistory() {
        tokenManager?.let { it1 -> viewModel.getOrderProductById(it1) }

        viewModel.getOrderList.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        populateHistoryView(picsResponse)
//                        binding.shimmerLayout.stopShimmer()
//                        binding.shimmerLayout.visibility = View.GONE
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

    private fun populateHistoryView(orderHistory: OrderHistoryModel) {
        for (i in orderHistory.orderlist.indices) {
            val itemView: View =
                LayoutInflater.from(this)
                    .inflate(R.layout.layout_customer_order_view, binding.orderHistoryList, false)

            val snId = itemView.findViewById<TextView>(R.id.id)
            val productId = itemView.findViewById<TextView>(R.id.product_id)
            val productTotal = itemView.findViewById<TextView>(R.id.product_total)
            val productCreated = itemView.findViewById<TextView>(R.id.product_created)
            val orderHistoryDetails = itemView.findViewById<TextView>(R.id.order_history_details)
           val createdAt:String=orderHistory.orderlist[i].created_at.toString()
             snId.text= (i+1).toString()
            productId.text=" # "+orderHistory.orderlist[i].id.toString()
            productTotal.text=getString(R.string.rs)+orderHistory.orderlist[i].total.toString()
            val created: String = createdAt.substring(0, createdAt.length.coerceAtMost(10))
            productCreated.text=created
            orderHistoryDetails.setOnClickListener {
                 var fragment:Fragment=OrderHistoryDetails()
                if (fragment == null) return@setOnClickListener
                val fm = supportFragmentManager
                val tr = fm.beginTransaction()
                tr.add(R.id.order_history, fragment)
                tr.commitAllowingStateLoss()
            }
                binding.orderHistoryList.addView(itemView)
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