package com.eightpeak.salakafarm.views.order.orderview.viewordercheckoutdetails

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityCheckoutDetailsViewBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.viewmodel.CheckOutViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.esewa.android.sdk.payment.ESewaConfiguration
import com.esewa.android.sdk.payment.ESewaPayment
import com.esewa.android.sdk.payment.ESewaPaymentActivity
import com.google.android.material.snackbar.Snackbar
import com.hadi.retrofitmvvm.util.errorSnack
import kotlinx.android.synthetic.main.fragment_home_slider.*

class CheckoutDetailsView : AppCompatActivity() {
    private val CONFIG_ENVIRONMENT: String = ESewaConfiguration.ENVIRONMENT_TEST
    private val REQUEST_CODE_PAYMENT = 1
    private var eSewaConfiguration: ESewaConfiguration? = null

    private val MERCHANT_ID = "JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R"
    private val MERCHANT_SECRET_KEY = "BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ=="


    private lateinit var userPrefManager: UserPrefManager

    private lateinit var viewModel: CheckOutViewModel
    private lateinit var binding: ActivityCheckoutDetailsViewBinding

    private var tokenManager: TokenManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eSewaConfiguration = ESewaConfiguration()
            .clientId(MERCHANT_ID)
            .secretKey(MERCHANT_SECRET_KEY)
            .environment(CONFIG_ENVIRONMENT)



        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        );
        binding = ActivityCheckoutDetailsViewBinding.inflate(layoutInflater)
        setupViewModel()
        setContentView(binding.root)
        binding.payByEsewa.setOnClickListener {
            makePayment("10")
        }
    }
    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(CheckOutViewModel::class.java)
        getCheckOutDetails()
    }

    private fun getCheckOutDetails() {

        tokenManager?.let { it1 -> viewModel.getCheckOutResponse(it1) }

        viewModel.checkoutResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let { picsResponse ->

                        addViewShippingAddress(picsResponse.order_details.shippingAddress)
                        addCartDetails(picsResponse.order_details.cartItem)
                        addTotalPrice(picsResponse.order_details.dataTotal)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.checkoutView.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun addTotalPrice(dataTotal: List<DataTotal>) {

        for (i in dataTotal.indices) {
            val itemView: View =
                LayoutInflater.from(this)
                    .inflate(R.layout.total_price_item, binding.priceTotal, false)

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
              binding.priceTotal.addView(itemView)
        }
        }

    private fun addCartDetails(cartItem: List<CartItem>) {
//        cart_list

        for (i in cartItem.indices) {
            val itemView: View =
                LayoutInflater.from(this)
                    .inflate(R.layout.cart_checkout_item, binding.cartList, false)

            val categorySKU = itemView.findViewById<TextView>(R.id.product_sku)
            val productThumbnail = itemView.findViewById<ImageView>(R.id.product_thumbnail)
            val productName = itemView.findViewById<TextView>(R.id.product_name)
            val productPrice = itemView.findViewById<TextView>(R.id.product_price)
            val productAttribute = itemView.findViewById<TextView>(R.id.category_name)
            val increaseQuantity = itemView.findViewById<ImageButton>(R.id.increase_quantity)
            val decreaseQuantity = itemView.findViewById<ImageButton>(R.id.decrease_quantity)
            val quantityView = itemView.findViewById<TextView>(R.id.product_quantity)
            val itemSelected = itemView.findViewById<ImageView>(R.id.item_selected)
            quantityView.text =  cartItem[i].qty.toString()


//            increaseQuantity.setOnClickListener {
//                quantity= cartItem[i].qty
//                quantity += 1
//                quantityView.text = quantity.toString()
//            }
//            decreaseQuantity.setOnClickListener { if(quantity>1){
//                quantity= cartItem[i].qty
//                quantity -= 1
//                quantityView.text = quantity.toString()
//            }
//            }
            var isClicked=true
            itemSelected.setOnClickListener {
                tokenManager?.let { it1 -> viewModel.deleteCartById(it1,cartItem[i].id.toString()) }
                observeData()
            }
            categorySKU.text = cartItem[i].products_with_description.sku
            productName.text = cartItem[i].products_with_description.descriptions[0].name
            productPrice.text = cartItem[i].products_with_description.price.toString()
            productThumbnail.load(EndPoints.BASE_URL + cartItem[i].products_with_description.image)
            binding.cartList.addView(itemView)
        }
    }

    private fun observeData() {
        viewModel.deleteCartById.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let { picsResponse ->
                        Log.i("TAG", "observeData: m here")

                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.checkoutView.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun addViewShippingAddress(shippingAddress: ShippingAddress) {
        binding.customerName.text=shippingAddress.first_name+" "+shippingAddress.last_name
        binding.customerAddress.text=shippingAddress.address1+", "+shippingAddress.address2

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


    private fun makePayment(amount: String) {
        val eSewaPayment = ESewaPayment(
            amount,
            "someProductName",
            "someUniqueId_" + System.nanoTime(),
            "https://somecallbackurl.com"
        )
        val intent = Intent(this@CheckoutDetailsView, ESewaPaymentActivity::class.java)
        intent.putExtra(ESewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration)
        intent.putExtra(ESewaPayment.ESEWA_PAYMENT, eSewaPayment)
        startActivityForResult(
            intent,
            REQUEST_CODE_PAYMENT
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                val s = data?.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE)
                Log.i("Proof of Payment", s!!)
                Toast.makeText(this, "SUCCESSFUL PAYMENT", Toast.LENGTH_SHORT).show()
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled By User", Toast.LENGTH_SHORT).show()
            } else if (resultCode == ESewaPayment.RESULT_EXTRAS_INVALID) {
                val s = data?.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE)
                Log.i("Proof of Payment", s!!)
            }
        }
    }


//    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CODE_PAYMENT) {
//            if (resultCode == RESULT_OK) {
//                val s = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE)
//                Log.i("Proof of Payment", s!!)
//                Toast.makeText(this, "SUCCESSFUL PAYMENT", Toast.LENGTH_SHORT).show()
//            } else if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(this, "Canceled By User", Toast.LENGTH_SHORT).show()
//            } else if (resultCode == ESewaPayment.RESULT_EXTRAS_INVALID) {
//                val s = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE)
//                Log.i("Proof of Payment", s!!)
//            }
//        }
//    }
}