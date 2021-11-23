package com.eightpeak.salakafarm.views.order.orderview.orderhistory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.databinding.FragmentOrderHistoryBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.OrderViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.products.Data
import com.eightpeak.salakafarm.views.home.products.ProductAdapter
import com.google.android.material.snackbar.Snackbar
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdActivity
import com.eightpeak.salakafarm.views.order.orderview.confirmOrder.OrderTracking
import kotlinx.android.synthetic.main.fragment_add_to_cart.*

class OrderHistory : AppCompatActivity() {

    private lateinit var viewModel: OrderViewModel
    private lateinit var binding: FragmentOrderHistoryBinding


    lateinit var productAdapter: ProductAdapter
    private var layoutManager: GridLayoutManager? = null
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
        init()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(OrderViewModel::class.java)
        getOrderHistory()
        getRandomProducts()
    }

    private fun getOrderHistory() {
        tokenManager?.let { it1 -> viewModel.getOrderProductById(it1) }

        viewModel.getOrderList.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        if(picsResponse.orderlist.isNotEmpty()){
                            populateHistoryView(picsResponse)
//
                            binding.ifEmpty.visibility=View.GONE
                        }else{
                            binding.ifEmpty.visibility=View.VISIBLE
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

    private fun populateHistoryView(orderHistory: OrderHistoryModel) {
        for (i in orderHistory.orderlist.indices) {
            val itemView: View =
                LayoutInflater.from(this)
                    .inflate(R.layout.layout_customer_order_view, binding.orderHistoryList, false)

            val snId = itemView.findViewById<TextView>(R.id.id)
            val productId = itemView.findViewById<TextView>(R.id.product_id)
            val productTotal = itemView.findViewById<TextView>(R.id.product_total)
            val productCreated = itemView.findViewById<TextView>(R.id.product_created)
            val orderStatus = itemView.findViewById<TextView>(R.id.order_status)
            val orderHistoryDetails = itemView.findViewById<TextView>(R.id.order_history_details)
            val orderTracking = itemView.findViewById<TextView>(R.id.order_history_track)
           val createdAt:String=orderHistory.orderlist[i].created_at.toString()
             snId.text= (i+1).toString()
            productId.text=" # "+orderHistory.orderlist[i].id.toString()
            productTotal.text=getString(R.string.rs)+orderHistory.orderlist[i].total.toString()


            if(orderHistory.orderlist[i].status == 1){
                orderStatus.text="New"

                orderStatus.setTextColor(getColor(R.color.blue))
            }else if(orderHistory.orderlist[i].status == 2){
                orderStatus.text="Processing"
                orderStatus.setTextColor(getColor(R.color.green1))
            }else if(orderHistory.orderlist[i].status == 3){
                orderStatus.text="Hold"
            }else if(orderHistory.orderlist[i].status == 4){
                orderStatus.text="Cancelled"
                orderStatus.setTextColor(getColor(R.color.gray_1))
            }else if(orderHistory.orderlist[i].status == 5){
                orderStatus.text="Completed"
                orderStatus.setTextColor(getColor(R.color.green2))
            }else if(orderHistory.orderlist[i].status == 6){
                orderStatus.text="Failed"
                orderStatus.setTextColor(getColor(R.color.red))


            }

            val created: String = createdAt.substring(0, createdAt.length.coerceAtMost(10))

            orderTracking.setOnClickListener {
                val intent = Intent(this@OrderHistory,OrderTracking::class.java)
                intent.putExtra(Constants.ORDER_STATUS, orderHistory.orderlist[i].status.toString())
                startActivity(intent)
            }


            productCreated.text=created
            val bundle = Bundle()
            bundle.putString("order_id", orderHistory.orderlist[i].id.toString())
            orderHistoryDetails.setOnClickListener {
                 val fragment:Fragment=OrderHistoryDetails()
                val fm = supportFragmentManager
                val tr = fm.beginTransaction()
                fragment.arguments = bundle
                Log.i("TAG", "populateHistoryView: "+ orderHistory.orderlist[i].id.toString())
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

    private fun init() {
        productAdapter = ProductAdapter()
        layoutManager = GridLayoutManager(this, 2)
        binding.productRecyclerView.layoutManager = layoutManager
        binding.productRecyclerView.setHasFixedSize(true)
        binding.productRecyclerView.isFocusable = false
        binding.productRecyclerView.adapter = productAdapter

    }
    private fun getRandomProducts() {
        viewModel.getRandomListResponseView()

        viewModel.getRandomListResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        getRandomProductsDetails(picsResponse)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        addToCart.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun getRandomProductsDetails(data: List<Data>) {
        productAdapter.differ.submitList(data)
        binding.productRecyclerView.adapter = productAdapter

    }

}