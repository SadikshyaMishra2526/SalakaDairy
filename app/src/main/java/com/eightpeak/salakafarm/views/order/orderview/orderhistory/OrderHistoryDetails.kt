package com.eightpeak.salakafarm.views.order.orderview.orderhistory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.FragmentOrderHistoryDetailsBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.OrderViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.order.orderview.viewordercheckoutdetails.DataTotal

class OrderHistoryDetails : Fragment() {

    private var tokenManager: TokenManager? = null
    private lateinit var viewModel: OrderViewModel

    private var _binding: FragmentOrderHistoryDetailsBinding? = null
    lateinit var userPrefManager: UserPrefManager

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userPrefManager = UserPrefManager(requireContext())
        _binding = FragmentOrderHistoryDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        tokenManager = TokenManager.getInstance(
            requireActivity().getSharedPreferences(
                Constants.TOKEN_PREF,
                AppCompatActivity.MODE_PRIVATE
            )
        )
        binding.header.text = "Your Order Details"
        setupViewModel()
        return root
    }



    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(OrderViewModel::class.java)
        getOrderHistoryDetails()
    }


    private fun getOrderHistoryDetails() {
        tokenManager?.let { it1 -> viewModel.getOrderHistoryDetails(it1, "4") }

        viewModel.getOrderListByDetails.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        Log.i("TAG", "getOrderHistoryDetails: i m here"+picsResponse.order_details)
                        viewCustomerDetails(picsResponse.order_details)
                        viewProductDetails(picsResponse.order_details.details)
                        viewProductStatus(picsResponse.order_details.history)
                       viewTotalPrice(picsResponse.order_details.order_total)

                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
//                        binding.orderHistory.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun viewTotalPrice(dataTotal: List<Order_total>) {

        for (i in dataTotal.indices) {
            val itemView: View =
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.total_price_item, binding.orderTotal, false)

            val priceKey = itemView.findViewById<TextView>(R.id.price_key_name)
            val priceValue = itemView.findViewById<TextView>(R.id.price_value)
            val totalPriceCard = itemView.findViewById<CardView>(R.id.total_price_card)

            val price: String = dataTotal[i].text.replace("Rs.", "")
            Log.i("TAG", "addTotalPrice: $price")
            if(price.length>1){
                priceValue.text = dataTotal[i].text
                priceKey.text = dataTotal[i].title

            }else{
                totalPriceCard.visibility=View.GONE
            }
            binding.orderTotal.addView(itemView)
        }
    }

    private fun viewProductStatus(history: List<History>) {

    }

    private fun viewCustomerDetails(orderDetails: Order_details) {
        binding.customerName.text=orderDetails.first_name+" "+orderDetails.last_name
       binding.customerEmail.text=orderDetails.email
       binding.customerPhone.text=orderDetails.phone
       binding.customerAddress.text=orderDetails.address1+" "+orderDetails.address2

        binding.paymentStatus.text="Payment Status \n"+orderDetails.payment_status.name
        binding.orderStatus.text="Order Status \n"+orderDetails.order_status.name
        binding.paymentType.text="Shipping Status \n"+orderDetails.shipping_method
        binding.orderDate.text="Order At \n"+orderDetails.created_at




    }

    private fun viewProductDetails(details: List<Details>) {
        for (i in details.indices) {
            val itemView: View =
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.order_history_details_item, binding.productListView, false)
            binding.productListView.addView(itemView)
            val categorySKU = itemView.findViewById<TextView>(R.id.product_sku)
            val productThumbnail = itemView.findViewById<ImageView>(R.id.product_thumbnail)
            val productName = itemView.findViewById<TextView>(R.id.product_name)
            val productPrice = itemView.findViewById<TextView>(R.id.product_price)
            val productUnit = itemView.findViewById<TextView>(R.id.product_unit)
            val quantityTotal = itemView.findViewById<TextView>(R.id.product_price_total)
            categorySKU.text = details[i].sku
            productName.text = details[i].sku
            productPrice.text = getString(R.string.rs)+ details[i].price.toString()+" ("+details[i].qty.toString()+")"
            quantityTotal.text = getString(R.string.rs)+ details[i].total_price.toString()

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