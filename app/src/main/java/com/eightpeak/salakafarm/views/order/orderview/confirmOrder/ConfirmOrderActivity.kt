package com.eightpeak.salakafarm.views.order.orderview.confirmOrder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityConfirmOrderBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.CheckOutViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.order.orderview.orderhistory.OrderHistory
import com.eightpeak.salakafarm.views.order.orderview.viewordercheckoutdetails.CartItem
import com.eightpeak.salakafarm.views.order.orderview.viewordercheckoutdetails.DataTotal
import com.eightpeak.salakafarm.views.order.orderview.viewordercheckoutdetails.ShippingAddress
import com.esewa.android.sdk.payment.ESewaConfiguration
import com.esewa.android.sdk.payment.ESewaPayment
import com.esewa.android.sdk.payment.ESewaPaymentActivity
import com.google.android.material.snackbar.Snackbar
import java.util.*

class ConfirmOrderActivity : AppCompatActivity() {

    private lateinit var userPrefManager: UserPrefManager
    private var tokenManager: TokenManager? = null
    private lateinit var viewModel: CheckOutViewModel
    private lateinit var binding: ActivityConfirmOrderBinding

    private val CONFIG_ENVIRONMENT: String = ESewaConfiguration.ENVIRONMENT_TEST
    private val REQUEST_CODE_PAYMENT = 1
    private var eSewaConfiguration: ESewaConfiguration? = null


    private var shippingId: String? = null
    private var totalCost: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        eSewaConfiguration = ESewaConfiguration()
            .clientId(Constants.MERCHANT_ID)
            .secretKey(Constants.MERCHANT_SECRET_KEY)
            .environment(CONFIG_ENVIRONMENT)

        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        userPrefManager = UserPrefManager(this)
        binding = ActivityConfirmOrderBinding.inflate(layoutInflater)


        setupViewModel()
        setContentView(binding.root)
        binding.header.text = getString(R.string.your_order_details)


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
//                        shippingId=picsResponse.order_details.shippingAddress

                        val selectedPaymentMethod = intent.getStringExtra("payment_option").toString()
                        if(selectedPaymentMethod=="Cash"){
                            var totalPrice=""
                            binding.placeOrderEsewa.visibility=View.GONE
                            binding.paymentStatus.load(R.drawable.cod)
                            binding.placeOrder.setOnClickListener {
                              for(i in picsResponse.order_details.dataTotal) {

                                  if(i.title=="total"){
                                      totalPrice=i.text
                                  }
                              }
                                val body = RequestBodies.AddOrder("null", "null", "137", "137")
                                tokenManager?.let { it1 -> viewModel.addOrder(it1,body) }
                                getOrderResponse()
                            }
                        }else if(selectedPaymentMethod=="Esewa"){
                            binding.placeOrder.visibility=View.GONE
                            binding.placeOrderEsewa.visibility=View.VISIBLE
                            binding.paymentStatus.load(R.drawable.esewa)
                            binding.placeOrderEsewa.setOnClickListener {
                                makePayment("232")

                            }
                        }
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

    private fun getOrderResponse() {
        viewModel.addOrder.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        Log.i("TAG", "getOrderResponse: "+picsResponse)
//                        Toast.makeText(this@ConfirmOrderActivity," Order has successfully placed!!! ",Toast.LENGTH_SHORT).show()
//                        startActivity(Intent(this@ConfirmOrderActivity,OrderHistory::class.java))
//                        finish()
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
            if (price.length > 1) {
                priceValue.text = dataTotal[i].text
                priceKey.text = dataTotal[i].title

            } else {
                totalPriceCard.visibility = View.GONE
            }

            binding.priceTotal.addView(itemView)
        }
    }

    private fun addCartDetails(cartItem: List<CartItem>) {
        for (i in cartItem.indices) {
            val itemView: View =
                LayoutInflater.from(this)
                    .inflate(R.layout.cart_checkout_item, binding.cartList, false)

            val updateCart = itemView.findViewById<LinearLayout>(R.id.update_cart)
            val categorySKU = itemView.findViewById<TextView>(R.id.product_sku)
            val productThumbnail = itemView.findViewById<ImageView>(R.id.product_thumbnail)
            val productName = itemView.findViewById<TextView>(R.id.product_name)
            val productPrice = itemView.findViewById<TextView>(R.id.product_price)
            val productTotalPrice = itemView.findViewById<TextView>(R.id.product_total_price)
            val quantityView = itemView.findViewById<TextView>(R.id.product_quantity)
            val itemSelected = itemView.findViewById<ImageView>(R.id.item_selected)
            quantityView.text = cartItem[i].qty.toString()

            updateCart.visibility = View.GONE
            itemSelected.visibility = View.GONE

            categorySKU.text = cartItem[i].products_with_description.sku
            productThumbnail.load(EndPoints.BASE_URL + cartItem[i].products_with_description.image)



            if (cartItem[i].products_with_description.descriptions.isNotEmpty()) {
                if (userPrefManager.language.equals("ne")) {
                    productName.text = cartItem[i].products_with_description.descriptions[1].name
                    productPrice.text =
                        getString(R.string.rs) + GeneralUtils.getUnicodeNumber(cartItem[i].final_price.toString())
                    productTotalPrice.text =
                        getString(R.string.rs) + GeneralUtils.getUnicodeNumber(cartItem[i].total_price.toString())
                } else {
                    productName.text = cartItem[i].products_with_description.descriptions[0].name
                    productPrice.text = getString(R.string.rs) + cartItem[i].final_price.toString()
                    productTotalPrice.text =
                        getString(R.string.rs) + cartItem[i].total_price.toString()

                }
            }

            binding.cartList.addView(itemView)
        }
    }

    private fun addViewShippingAddress(shippingAddress: ShippingAddress) {
        binding.customerName.text = shippingAddress.first_name + " " + shippingAddress.last_name
        binding.customerAddress.text = shippingAddress.address1 + ", " + shippingAddress.address2

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
        val intent = Intent(this@ConfirmOrderActivity, ESewaPaymentActivity::class.java)
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

                getCheckOutDetails()
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled By User", Toast.LENGTH_SHORT).show()
            } else if (resultCode == ESewaPayment.RESULT_EXTRAS_INVALID) {
                val s = data?.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE)
                Log.i("Proof of Payment", s!!)
            }
        }
    }
}