package com.eightpeak.salakafarm.views.order.orderview.viewordercheckoutdetails

import android.content.DialogInterface
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
import com.eightpeak.salakafarm.databinding.ActivityCheckoutDetailsViewBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.subscription.confirmSubscription.ConfirmSubscription
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.addAddressSnack
import com.eightpeak.salakafarm.utils.subutils.errorSnack
import com.eightpeak.salakafarm.viewmodel.CheckOutViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.addresslist.Address_list
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdActivity
import com.eightpeak.salakafarm.views.order.orderview.confirmOrder.ConfirmOrderActivity
import com.google.android.material.snackbar.Snackbar


class CheckoutDetailsView : AppCompatActivity() {
    private  var quantity: Int = 0
    private  var priceTotal: Int = 0
    private lateinit var userPrefManager: UserPrefManager
    private lateinit var viewModel: CheckOutViewModel
    private lateinit var binding: ActivityCheckoutDetailsViewBinding
    private var tokenManager: TokenManager? = null
    private var total:Int=0

    private  var selectedAddressId: String=""
    private  var selectedAddressName: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        userPrefManager= UserPrefManager(this)
        binding = ActivityCheckoutDetailsViewBinding.inflate(layoutInflater)
        binding.returnHome.setOnClickListener {
            finish()
        }
        setupViewModel()
        setContentView(binding.root)


        binding.confirmOrder.setOnClickListener {
            if(total>1000){
                val paymentOptionRatio = findViewById<RadioGroup>(R.id.payment_option).checkedRadioButtonId
                var payment:String =""
//                = findViewById<View>(paymentOptionRatio) as RadioButton
                if(paymentOptionRatio==R.id.paid_by_cash){
                    Log.i("TAG", "onCreate: paid_by_cash")
                    payment="Cash"
                }else if(paymentOptionRatio==R.id.paid_by_esewa){
                    Log.i("TAG", "onCreate: paid_by_esewa")
                    payment="Esewa"
                }
                if(paymentOptionRatio!=null){
                    val intent = Intent(this@CheckoutDetailsView, ConfirmOrderActivity::class.java)
                      intent.putExtra("payment_option", payment)
                      intent.putExtra("selected_shipping", selectedAddressId)
                      intent.putExtra("payment_option", payment)
                      startActivity(intent)
                }else{
                    binding.checkoutView.errorSnack("Please select payment option")

                }

            }else{
                binding.checkoutView.errorSnack("Price needs to be above thousand Rupees for us to delivery...")
            }
        }
        binding.header.text="Your Checkout Details"
    }
    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(CheckOutViewModel::class.java)
        getCheckOutDetails()
        getAddressList()

    }

    private fun getCheckOutDetails() {

        tokenManager?.let { it1 -> viewModel.getCheckOutResponse(it1) }
        viewModel.checkoutResponse.observe(this, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let { picsResponse ->

                        addViewShippingAddress(picsResponse.order_details.shippingAddress)
                         total= addCartDetails(picsResponse.order_details.cartItem)
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

    private fun addCartDetails(cartItem: List<CartItem>) :Int{

        for (i in cartItem.indices) {
            val itemView: View =
                LayoutInflater.from(this)
                    .inflate(R.layout.cart_checkout_item, binding.cartList, false)

            val categorySKU = itemView.findViewById<TextView>(R.id.product_sku)
            val productThumbnail = itemView.findViewById<ImageView>(R.id.product_thumbnail)
            val productName = itemView.findViewById<TextView>(R.id.product_name)
            val productPrice = itemView.findViewById<TextView>(R.id.product_price)
            val productTotalPrice = itemView.findViewById<TextView>(R.id.product_total_price)
            val increaseQuantity = itemView.findViewById<ImageButton>(R.id.increase_quantity)
            val decreaseQuantity = itemView.findViewById<ImageButton>(R.id.decrease_quantity)
            val quantityView = itemView.findViewById<TextView>(R.id.product_quantity)
            val itemSelected = itemView.findViewById<ImageView>(R.id.item_selected)
            quantityView.text =  cartItem[i].qty.toString()

            productName.setOnClickListener {
                val intent = Intent(this@CheckoutDetailsView, ProductByIdActivity::class.java)
                intent.putExtra(Constants.PRODUCT_ID, cartItem[i].product_id.toString())
                startActivity(intent)
            }
            increaseQuantity.setOnClickListener {
                quantity= cartItem[i].qty
                quantity += 1
                quantityView.text = quantity.toString()
                updateCart(cartItem[i].id.toString(),quantity.toString())
            }
            decreaseQuantity.setOnClickListener {
                if(quantity>1){
                    quantity= cartItem[i].qty
                    quantity -= 1
                    quantityView.text = quantity.toString()
                    updateCart(cartItem[i].id.toString(),quantity.toString())
                }
            }
             itemSelected.setOnClickListener {
                tokenManager?.let { it1 -> viewModel.deleteCartById(it1,cartItem[i].id.toString()) }
                observeData()
            }
            categorySKU.text = cartItem[i].products_with_description.sku
            productThumbnail.load(EndPoints.BASE_URL + cartItem[i].products_with_description.image)
            priceTotal += cartItem[i].total_price


            if(cartItem[i].products_with_description.descriptions.isNotEmpty()){
                if(userPrefManager.language.equals("ne")){
                    productName.text = cartItem[i].products_with_description.descriptions[1].name
                    productPrice.text =getString(R.string.rs)+ GeneralUtils.getUnicodeNumber( cartItem[i].final_price.toString())
                    productTotalPrice.text =getString(R.string.rs)+ GeneralUtils.getUnicodeNumber(cartItem[i].total_price.toString())
                }else{
                    productName.text = cartItem[i].products_with_description.descriptions[0].name
                    productPrice.text = getString(R.string.rs)+cartItem[i].final_price.toString()
                    productTotalPrice.text = getString(R.string.rs)+cartItem[i].total_price.toString()

                }
            }
            binding.cartList.addView(itemView)
        }
        return priceTotal
    }
    private fun updateCart(id:String,qty:String) {
        tokenManager?.let { it1 -> viewModel.updateCartResponseView(it1,id,qty) }

        viewModel.updateCartResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        finish()
                        startActivity(intent)
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

    private fun observeData() {
        viewModel.deleteCartById.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let { picsResponse ->
                        finish()
                        startActivity(intent)

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
        binding.customerDelivery.text=selectedAddressName
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


    private fun getAddressList() {
        tokenManager?.let { it1 -> viewModel.getUserAddressList(it1) }
        viewModel.userAddressList.observe(this,  { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        if (picsResponse.address_list.isNotEmpty()) {
                            selectedAddressId = picsResponse.address_list.get(0).id.toString()
                            selectedAddressName = picsResponse.address_list[0].address1 + ", " + picsResponse.address_list[0].address2 + ", " +  picsResponse.address_list[0].phone
                            binding.changeAddress.setOnClickListener {
                                showAddressList(picsResponse.address_list)
                            }
                        } else {
                            binding.checkoutView.addAddressSnack(
                                this@CheckoutDetailsView,
                                "Address List Empty,Please add your address",
                                Snackbar.LENGTH_LONG
                            )
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
    private fun showAddressList(addressList: List<Address_list>) {


            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Selected Delivery Location")
            val addressId = arrayOfNulls<String>(addressList.size)
            val names = arrayOfNulls<String>(addressList.size)
            val checkedItems = BooleanArray(addressList.size)


            var i = 0
            for (key in addressList) {
                addressId[i] = key.id.toString()


                names[i] = key.address1 + ", " + key.address2 + ", " + key.phone
                checkedItems[i] = false
                i += 1
            }
            builder.setMultiChoiceItems(
                names, checkedItems
            ) { _, which, isChecked ->
                checkedItems[which] = isChecked
            }
            builder.setPositiveButton("OK"
            ) { _, _ ->
                for (i in checkedItems.indices) {
                    if (checkedItems[i])
                    {
                        selectedAddressId = addressId[i].toString()
                        selectedAddressName = names[i].toString()
                        binding.customerDelivery.text=names[i].toString()
                    }
                }
            }
        builder.setNegativeButton("Cancel", null)
            val dialog: android.app.AlertDialog? = builder.create()
            dialog?.show()
    }


}