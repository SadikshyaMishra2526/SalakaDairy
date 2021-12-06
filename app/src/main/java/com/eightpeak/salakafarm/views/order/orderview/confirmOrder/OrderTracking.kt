package com.eightpeak.salakafarm.views.order.orderview.confirmOrder

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.ActivityOrderTrackingBinding
import com.eightpeak.salakafarm.mapfunctions.MapsFragment
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.Constants.Companion.ORDER_ID
import com.eightpeak.salakafarm.utils.Constants.Companion.ORDER_STATUS
import com.eightpeak.salakafarm.viewmodel.OrderViewModel
import com.eightpeak.salakafarm.views.home.products.ProductAdapter

class OrderTracking : AppCompatActivity() {

    private lateinit var viewModel: OrderViewModel
    private lateinit var binding: ActivityOrderTrackingBinding


    lateinit var productAdapter: ProductAdapter
    private var layoutManager: GridLayoutManager? = null
    private var tokenManager: TokenManager? = null
private var orderStatus: String? = null
private var orderId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderTrackingBinding.inflate(layoutInflater)
        binding.returnHome.setOnClickListener { finish() }
        binding.header.text=getString(R.string.track_your_order)
        setContentView(binding.root)
        orderStatus=intent.getStringExtra(ORDER_STATUS)
        orderId=intent.getStringExtra(ORDER_ID)
        binding.orderId.text= "#$orderId"
        setTrackOrder(orderStatus)
        setUpBranchesView()
        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
    }

    private fun setTrackOrder(orderStatus: String?) {
        if(orderStatus.equals("1")){
           binding.newOrder.load(R.drawable.shape_status_current)
           binding.newOrderLine.setBackgroundColor(Color.parseColor("#3F3FE6"))

        }else if(orderStatus.equals("2")){
            binding.newOrder.load(R.drawable.shape_staus_complete)
            binding.newOrderLine.setBackgroundColor(Color.parseColor("#5FA95F"))

            binding.processingOrder.load(R.drawable.shape_status_current)
            binding.processedOrderLine.setBackgroundColor(Color.parseColor("#3F3FE6"))

        }else if(orderStatus.equals("3")){
            binding.newOrder.load(R.drawable.shape_status_remaining)
            binding.newOrderLine.setBackgroundColor(Color.parseColor("#c0c0c0"))

            binding.processingOrder.load(R.drawable.shape_status_remaining)
            binding.processingOrderLine.setBackgroundColor(Color.parseColor("#c0c0c0"))

            binding.processedOrder.load(R.drawable.shape_status_remaining)
            binding.processedOrderLine.setBackgroundColor(Color.parseColor("#c0c0c0"))

        }else if(orderStatus.equals("4")||orderStatus.equals("6")){
            binding.orderCancel.visibility= View.VISIBLE
            binding.processedOrderLine.visibility=View.VISIBLE
            binding.newOrder.load(R.drawable.shape_staus_cancelled)
            binding.newOrderLine.setBackgroundColor(Color.parseColor("#8B0000"))

            binding.processingOrder.load(R.drawable.shape_staus_cancelled)
            binding.processingOrderLine.setBackgroundColor(Color.parseColor("#8B0000"))

            binding.processedOrder.load(R.drawable.shape_staus_cancelled)
            binding.processedOrderLine.setBackgroundColor(Color.parseColor("#8B0000"))

            binding.cancelledOrder.load(R.drawable.shape_staus_cancelled)
        }else if(orderStatus.equals("5")){

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

}