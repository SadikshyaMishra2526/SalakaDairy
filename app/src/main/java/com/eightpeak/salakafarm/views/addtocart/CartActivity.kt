package com.eightpeak.salakafarm.views.addtocart

import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.databinding.ActivityCartBinding
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.*
import com.eightpeak.salakafarm.viewmodel.GetResponseViewModel
import com.eightpeak.salakafarm.viewmodel.ViewModelProviderFactory
import com.eightpeak.salakafarm.views.home.products.Data
import com.eightpeak.salakafarm.views.home.products.ProductAdapter
import com.eightpeak.salakafarm.views.order.orderview.viewordercheckoutdetails.CheckoutDetailsView
import com.eightpeak.salakafarm.views.addtocart.addtocartfragment.Cart
import com.eightpeak.salakafarm.views.home.HomeActivity
import com.eightpeak.salakafarm.views.home.products.ServerResponse
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdActivity

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var viewModel: GetResponseViewModel
    private var _binding: ActivityCartBinding? = null
    private var tokenManager: TokenManager? = null
    private  var quantity: Int = 0

    lateinit var productAdapter: ProductAdapter
    private var layoutManager: GridLayoutManager? = null

    private lateinit var userPrefManager: UserPrefManager

    var mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val wishlist = intent.getBooleanExtra("wishlist",false)
            val compareList = intent.getBooleanExtra("compare_list",false)
            val productId = intent.getStringExtra("product_id")
            val removeFromCart= intent.getBooleanExtra("remove",false)
            if(wishlist){
                if(!removeFromCart){
                    if (productId != null) {
                        tokenManager?.let { viewModel.addtowishlist(it,productId)}
                        addToWishListResponse()
                    }
                }else{
                    if (productId != null) {
                        tokenManager?.let { viewModel.deleteWishlistById(it,productId)}
                        removeWishListResponse()
                    }
                }

            }else if(compareList){
                App.addItem(productId)
                binding.addToCart.successCompareSnack(this@CartActivity,"Add to Compare List",Snackbar.LENGTH_LONG)

            }

        }
    }
    private fun addToWishListResponse() {
        viewModel.wishlist.observe(this@CartActivity, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        val serverResponse: ServerResponse = picsResponse
                        Log.i("TAG", "getPictures: $serverResponse")
                        binding.addToCart.successWishListSnack(this@CartActivity,getString(R.string.add_to_wishlist),Snackbar.LENGTH_LONG)

                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.productRecyclerView.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }
    private fun removeWishListResponse() {
        viewModel.deleteWishlistById.observe(this@CartActivity, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        val serverResponse: ServerResponse = picsResponse

                        binding.addToCart.showSnack("Removed from Wishlist!!!",Snackbar.LENGTH_LONG)

                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.productRecyclerView.errorSnack(message, Snackbar.LENGTH_LONG)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }


    private fun initializeWishCompare() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
            IntentFilter("related-products")
        )
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)


        tokenManager = TokenManager.getInstance(
            getSharedPreferences(
                Constants.TOKEN_PREF,
                MODE_PRIVATE
            )
        )
        binding.proceedWithCheckout.setOnClickListener {
            startActivity(Intent(this@CartActivity, CheckoutDetailsView::class.java))

        }

        userPrefManager=UserPrefManager(this)
        binding.header.text=getString(R.string.shopping_cart)
        binding.returnHome.setOnClickListener {
            finish()
        }
        binding.continueShoppingEmpty.setOnClickListener {
            startActivity(Intent(this@CartActivity, HomeActivity::class.java))
            finish()  }
        init()
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(GetResponseViewModel::class.java)
        getPictures()
        getRandomProducts()
        initializeWishCompare()
    }
    private fun init() {
        productAdapter = ProductAdapter()
        layoutManager = GridLayoutManager(this, 2)
        binding.productRecyclerView.layoutManager = layoutManager
        binding.productRecyclerView.setHasFixedSize(true)
        binding.productRecyclerView.isFocusable = false
        binding.productRecyclerView.adapter = productAdapter

    }
    private fun getPictures() {
        tokenManager?.let { it1 -> viewModel.getCartResponse(it1) }

        viewModel.cartResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
                        getSelectedProducts(picsResponse.cart)
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        binding.addToCart.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
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
                           binding.addToCart.errorSnack(message, Snackbar.LENGTH_LONG)
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

    private fun getSelectedProducts(cartResponse: List<Cart>) {
        binding.viewCartList.removeAllViews()
        if(cartResponse.isNotEmpty()){
            binding.ifEmpty.visibility=View.GONE
            for (i in cartResponse.indices) {
                val itemView: View =
                    LayoutInflater.from(this)
                        .inflate(R.layout.cart_item, binding.viewCartList, false)

                val categorySKU = itemView.findViewById<TextView>(R.id.product_sku)
                val productThumbnail = itemView.findViewById<ImageView>(R.id.product_thumbnail)
                val productName = itemView.findViewById<TextView>(R.id.product_name)
                val productPrice = itemView.findViewById<TextView>(R.id.product_price)
                val productTotalPrice = itemView.findViewById<TextView>(R.id.product_total_price)
                val increaseQuantity = itemView.findViewById<ImageButton>(R.id.increase_quantity)
                val decreaseQuantity = itemView.findViewById<ImageButton>(R.id.decrease_quantity)
                val quantityView = itemView.findViewById<TextView>(R.id.product_quantity)
                val itemSelected = itemView.findViewById<ImageView>(R.id.item_selected)
                val outOfStock = itemView.findViewById<ImageView>(R.id.out_of_stock)
                quantityView.text =  cartResponse[i].qty.toString()
                if(cartResponse[i].products_with_description.stock>0){
                    outOfStock.visibility=View.GONE
                }else{
                    outOfStock.visibility=View.VISIBLE
                }
                productName.setOnClickListener {
                    val intent = Intent(this@CartActivity, ProductByIdActivity::class.java)
                    intent.putExtra(Constants.PRODUCT_ID, cartResponse[i].product_id.toString())
                    startActivity(intent)
                }

                binding.deleteCartItems.setOnClickListener {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Delete Cart's all Item")
                    builder.setMessage("Are you sure you want to delete this item?")
                    builder.setPositiveButton("Confirm",
                        DialogInterface.OnClickListener { dialog, which ->
                            tokenManager?.let { it1 -> viewModel.deleteCart(it1) }
                            getDeleteResponse()
                            dialog.dismiss()
                        })
                    builder.setNegativeButton(R.string.cancel, null)

                    val dialog = builder.create()
                    dialog.show()
                }
                increaseQuantity.setOnClickListener {
                    quantity= cartResponse[i].qty
                    quantity += 1
                    quantityView.text = quantity.toString()
                    updateCart(cartResponse[i].id.toString(),quantity.toString())
                }
                decreaseQuantity.setOnClickListener {
                    if( cartResponse[i].qty>1){
                        quantity= cartResponse[i].qty
                        quantity -= 1
                        quantityView.text = quantity.toString()
                        updateCart(cartResponse[i].id.toString(),quantity.toString())
                    }
                }
                itemSelected.setOnClickListener {
                    tokenManager?.let { it1 -> viewModel.deleteCartItemById(it1,cartResponse[i].id.toString()) }
                    getDeleteProductResponse()

                }
                categorySKU.text = cartResponse[i].products_with_description.sku
                 productThumbnail.load(EndPoints.BASE_URL + cartResponse[i].products_with_description.image)
                if(cartResponse[i].products_with_description.descriptions.isNotEmpty()){
                    if(userPrefManager.language.equals("ne")){
                        productName.text = cartResponse[i].products_with_description.descriptions[1].name
                        productPrice.text =getString(R.string.rs)+GeneralUtils.getUnicodeNumber( cartResponse[i].final_price.toString())
                        productTotalPrice.text =getString(R.string.rs)+ GeneralUtils.getUnicodeNumber(cartResponse[i].total_price.toString())
                    }else{
                        productName.text = cartResponse[i].products_with_description.descriptions[0].name
                        productPrice.text = getString(R.string.rs)+cartResponse[i].final_price.toString()
                        productTotalPrice.text = getString(R.string.rs)+cartResponse[i].total_price.toString()

                    }
                }
                binding.viewCartList.addView(itemView)
            }
        }else{
            binding.ifEmpty.visibility=View.VISIBLE
        }
    }

    private fun getDeleteProductResponse() {
        viewModel.deleteCartItem.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
//                        finish()
//                        startActivity(intent)
                        getPictures()
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                           binding.addToCart.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun getDeleteResponse() {
        viewModel.deleteCart.observe(this, Observer { response ->
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
                           binding.addToCart.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun updateCart(id:String,qty:String) {
        tokenManager?.let { it1 -> viewModel.updateCartResponseView(it1,id,qty) }

        viewModel.updateCartResponse.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { picsResponse ->
//                        finish()
//                        startActivity(intent)
                        getPictures()
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                           binding.addToCart.errorSnack(message, Snackbar.LENGTH_LONG)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}