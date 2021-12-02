package com.eightpeak.salakafarm.views.order.orderview.orderhistory

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.FragmentOrderHistoryDetailsBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.utils.subutils.successCompareSnack
import com.eightpeak.salakafarm.utils.subutils.successWishListSnack
import com.eightpeak.salakafarm.viewmodel.OrderViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.products.ServerResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.product_item.view.*

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
         val intent=Intent()
        val orderId = intent.getStringExtra("order_id")
        Log.i("TAG", "setupViewModel: "+orderId)
        getOrderHistoryDetails("4")
    }


    private fun getOrderHistoryDetails(orderId: String?) {
        tokenManager?.let { it1 ->
            if (orderId != null) {
                viewModel.getOrderHistoryDetails(it1, orderId)
            }
        }

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
                        binding.orderHistoryDetails.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun viewTotalPrice(dataTotal: List<Order_total>) {
        binding.orderTotal.removeAllViews()
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

        binding.paymentStatus.text=getString(R.string.payment_status)+"\n"+orderDetails.payment_status.name
        binding.orderStatus.text=getString(R.string.order_status)+" \n"+orderDetails.order_status.name
        binding.paymentType.text=getString(R.string.shipping_status)+" \n"+orderDetails.shipping_method
        binding.orderDate.text=getString(R.string.order_at)+" \n"+orderDetails.created_at




    }

    private fun viewProductDetails(details: List<Details>) {
        binding.productListView.removeAllViews()
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


            val productWishList = itemView.findViewById<ImageButton>(R.id.product_add_to_wishlist)
            val productCart = itemView.findViewById<ImageButton>(R.id.product_add_to_cart)
            val productCompare = itemView.findViewById<ImageButton>(R.id.product_add_to_compare_list)
            categorySKU.text = details[i].sku
            productPrice.text = "( "+getString(R.string.rs)+ details[i].price.toString()+" )"
            productUnit.text=details[i].qty.toString()+" units"
            quantityTotal.text = getString(R.string.rs)+ details[i].total_price.toString()



            if (userPrefManager.language.equals("ne")) {
                productName.text = details[i].product.descriptions[1].name

                if(!details[i].product.cost.equals("0")){
                   productPrice.text =
                       getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(details[i].product.cost.toString())
                }else{
                    productPrice.text =
                        getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(details[i].product.price.toString())
                }

            } else {
                productName.text = details[i].product.descriptions[0].name

                if(!details[i].product.cost.equals("0")){
                    productPrice.text =
                        getString(R.string.rs) + details[i].product.cost.toString()
                }else{
                    productPrice.text =
                        getString(R.string.rs) + details[i].product.price.toString()
                }
            }







            productWishList.setOnClickListener {
                tokenManager?.let { viewModel.addtowishlist(it,details[i].id.toString())}
                addToWishListResponse()
            }

            productThumbnail.load(BASE_URL+details[i].product.image)
            productCart.setOnClickListener {

            }
            productCompare.setOnClickListener {
                App.addItem(details[i].id.toString())
                binding.orderHistoryDetails.successCompareSnack(requireContext(),"Add to Compare List",
                    Snackbar.LENGTH_LONG)

            }
        }

    }
    private fun addToWishListResponse() {
        viewModel.wishlist.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        val serverResponse: ServerResponse = picsResponse
                        Log.i("TAG", "getPictures: $serverResponse")
                        binding.orderHistoryDetails.successWishListSnack(requireContext(),getString(R.string.add_to_wishlist),Snackbar.LENGTH_LONG)

                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.orderHistoryDetails.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
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