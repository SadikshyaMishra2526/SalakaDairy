package com.eightpeak.salakafarm.views.order.orderview.confirmOrder

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.ActivityOrderTrackingBinding
import com.eightpeak.salakafarm.mapfunctions.MapsFragment
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.Constants.Companion.ORDER_ID
import com.eightpeak.salakafarm.utils.Constants.Companion.ORDER_STATUS
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.GetResponseViewModel
import com.eightpeak.salakafarm.viewmodel.OrderViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.products.ProductAdapter
import com.google.android.material.snackbar.Snackbar

class OrderTracking : AppCompatActivity() {

    private lateinit var viewModel: OrderViewModel
    private lateinit var binding: ActivityOrderTrackingBinding


    private val NEW_HEX_CODE_LISTENER_INTERVAL: Long = 10000

    private var tokenManager: TokenManager? = null
    private var orderStatus: String? = null
    private var orderId: String? = null

    private val handler: Handler = Handler()
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            sendEmployeeCurrentPosition()
            handler.postDelayed(this, NEW_HEX_CODE_LISTENER_INTERVAL)
        }
    }

    private fun sendEmployeeCurrentPosition() {
        val body= orderId?.let { RequestBodies.EmpLatlng(it,"order") }
        tokenManager?.let {
            if (body != null) {
                viewModel.empLatLng(it,body)
            }
        }
        viewModel.empLatLng.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let {

                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.trackOrderLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderTrackingBinding.inflate(layoutInflater)
        binding.returnHome.setOnClickListener { finish() }
        binding.header.text = getString(R.string.track_your_order)
        setContentView(binding.root)
        setupViewModel()
        orderStatus = intent.getStringExtra(ORDER_STATUS)
        orderId = intent.getStringExtra(ORDER_ID)
        binding.orderId.text = "#$orderId"
        setTrackOrder(orderStatus)
        setUpBranchesView()
        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(OrderViewModel::class.java)

    }


    private fun setTrackOrder(orderStatus: String?) {
        if (orderStatus.equals("1")) {
            binding.newOrder.load(R.drawable.shape_status_current)
            binding.newOrderLine.setBackgroundColor(Color.parseColor("#3F3FE6"))

        } else if (orderStatus.equals("2")) {
            binding.newOrder.load(R.drawable.shape_staus_complete)
            binding.newOrderLine.setBackgroundColor(Color.parseColor("#5FA95F"))

            binding.processingOrder.load(R.drawable.shape_status_current)
            binding.processedOrderLine.setBackgroundColor(Color.parseColor("#3F3FE6"))

        } else if (orderStatus.equals("3")) {
            binding.newOrder.load(R.drawable.shape_status_remaining)
            binding.newOrderLine.setBackgroundColor(Color.parseColor("#c0c0c0"))

            binding.processingOrder.load(R.drawable.shape_status_remaining)
            binding.processingOrderLine.setBackgroundColor(Color.parseColor("#c0c0c0"))

            binding.processedOrder.load(R.drawable.shape_status_remaining)
            binding.processedOrderLine.setBackgroundColor(Color.parseColor("#c0c0c0"))

        } else if (orderStatus.equals("4") || orderStatus.equals("6")) {
            binding.orderCancel.visibility = View.VISIBLE
            binding.processedOrderLine.visibility = View.VISIBLE
            binding.newOrder.load(R.drawable.shape_staus_cancelled)
            binding.newOrderLine.setBackgroundColor(Color.parseColor("#8B0000"))

            binding.processingOrder.load(R.drawable.shape_staus_cancelled)
            binding.processingOrderLine.setBackgroundColor(Color.parseColor("#8B0000"))

            binding.processedOrder.load(R.drawable.shape_staus_cancelled)
            binding.processedOrderLine.setBackgroundColor(Color.parseColor("#8B0000"))

            binding.cancelledOrder.load(R.drawable.shape_staus_cancelled)
        } else if (orderStatus.equals("5")) {

            binding.newOrder.load(R.drawable.shape_staus_complete)
            binding.newOrderLine.setBackgroundColor(Color.parseColor("#5FA95F"))

            binding.processingOrder.load(R.drawable.shape_staus_complete)
            binding.processingOrderLine.setBackgroundColor(Color.parseColor("#5FA95F"))

            binding.processedOrder.load(R.drawable.shape_staus_complete)
            binding.processedOrderLine.setBackgroundColor(Color.parseColor("#5FA95F"))
        }

    }

    private fun setUpBranchesView() {
        val fm: FragmentManager = supportFragmentManager
        var fragment = fm.findFragmentByTag("myFragmentTag")
        if (fragment == null) {
            val ft: FragmentTransaction = fm.beginTransaction()
            fragment = MapsFragment()
            ft.add(R.id.branchesList, fragment, "myFragmentTag")
            ft.commit()
        }
    }

    private fun hideProgressBar() {
        binding.progress.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progress.visibility = View.VISIBLE
    }


    fun onProgressClick(view: View) {
    }

    override fun onDestroy() {
        super.onDestroy()
//        _binding = null
    }    @Override
    override fun onResume()
    {
        super.onResume();
        startIntervalHandler();

    }
    override fun onPause() {
        super.onPause()
        stopIntervalHandler()
    }

    private fun startIntervalHandler() {
        stopIntervalHandler()
        handler.post(runnable)
    }

    private fun stopIntervalHandler() {
        handler.removeCallbacks(runnable)
    }

}