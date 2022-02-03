package com.eightpeak.salakafarm.views.home.products

import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.utils.Constants.Companion.PRODUCT_ID
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdActivity
import androidx.fragment.app.FragmentActivity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.Constants.Companion.NO_LOGIN
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.notLoginWarningSnack
import com.google.android.material.snackbar.Snackbar
import kotlin.math.roundToInt


class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductListViewHolder>() {

    inner class ProductListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Data>() {

        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
            Log.i("TAG", "areItemsTheSame: " + oldItem.image)
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductListViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.product_item,
            parent,
            false
        )
    )

    override fun getItemCount() = differ.currentList.size


    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        val categoriesItem = differ.currentList[position]
        holder.itemView.apply {
            var wishlistClick=true


            val rating1 = findViewById<ImageView>(R.id.rating_1)
            val rating2 = findViewById<ImageView>(R.id.rating_2)
            val rating3 = findViewById<ImageView>(R.id.rating_3)
            val rating4 = findViewById<ImageView>(R.id.rating_4)
            val rating5 = findViewById<ImageView>(R.id.rating_5)
            val productAddToWishlist = findViewById<ImageView>(R.id.product_add_to_wishlist)
            val outOfStock = findViewById<ImageView>(R.id.out_of_stock)
            val ratedBy = findViewById<TextView>(R.id.rated_by)
            val productPriceDiscount = findViewById<TextView>(R.id.product_price_discount)
            val productPrice = findViewById<TextView>(R.id.product_price)
            val productName = findViewById<TextView>(R.id.product_name)
            val productFeature = findViewById<TextView>(R.id.product_feature)
            val btAddToCart = findViewById<TextView>(R.id.bt_add_to_cart)
            val productViewItem = findViewById<CardView>(R.id.productViewItem)
            val productAddToCompareList = findViewById<ImageButton>(R.id.product_add_to_compare_list)
            val productThumbnail = findViewById<ImageView>(R.id.product_thumbnail)


            val tokenManager = TokenManager.getInstance(context.getSharedPreferences(
                Constants.TOKEN_PREF,
                AppCompatActivity.MODE_PRIVATE
            ))
            productThumbnail.load(EndPoints.BASE_URL + categoriesItem.image)
            ratedBy.text = "("+categoriesItem.no_of_rating+") "

            val rating: Int? = categoriesItem?.average_rating?.roundToInt()

            if(rating==1){
                rating1.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
            }else if(rating==2){
                rating1.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating2.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
            }else if(rating==3){
                rating1.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating2.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating3.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
            }else if(rating==4){
                rating1.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating2.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating3.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating4.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
            }else if(rating==5){
                rating1.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating2.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating3.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating4.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating5.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
            }

            val userPrefManager = UserPrefManager(App.getContext())
                  if(categoriesItem.stock!! >0){
                      outOfStock.visibility=View.GONE
                  }else{
                      outOfStock.visibility=View.VISIBLE
                  }
            if (categoriesItem.descriptions!!.isNotEmpty()) {
                if (userPrefManager.language.equals("ne")) {
                    productName.text = categoriesItem?.descriptions[1].name

                    if(categoriesItem.promotion_price!=null){
                      productPriceDiscount.text=GeneralUtils.getUnicodeNumber(categoriesItem.price.toString())
                      productPriceDiscount.paintFlags = productPriceDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                      productPrice.text =
                          context.getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(categoriesItem.promotion_price?.price_promotion.toString())
                  }else{
                      productPrice.text =
                          context.getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(categoriesItem.price.toString())
                  }

                } else {
                    productName.text = categoriesItem?.descriptions?.get(0)?.name
                    Log.i("TAG", "onBindViewHolder: "+categoriesItem.promotion_price?.price_promotion)
                    if(categoriesItem.promotion_price==null){
                        productPrice.text =
                            context.getString(R.string.rs) + categoriesItem.price.toString()
                    }else{

                        productPriceDiscount.text=categoriesItem.price.toString()
                        productPriceDiscount.paintFlags = productPriceDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        productPrice.text =
                            context.getString(R.string.rs) + categoriesItem.promotion_price?.price_promotion.toString()
                    }
                }
            }

            Log.i("TAG", "onBindViewHolder: "+categoriesItem.sku)
            productFeature.text=categoriesItem.sku
            btAddToCart.setOnClickListener {
                Log.i("TAG", "onBindViewHolder: "+tokenManager.token)
                if(tokenManager.token!= NO_LOGIN){
                    val args = Bundle()
                    args.putString(PRODUCT_ID, categoriesItem.id.toString())
                    val bottomSheet = AddToCartView()
                    bottomSheet.arguments = args
                    bottomSheet.show(
                        (context as FragmentActivity).supportFragmentManager,
                        bottomSheet.tag
                    )
                }else{
                    productFeature.notLoginWarningSnack(context, Snackbar.LENGTH_LONG)
                }

            }
            productViewItem.setOnClickListener {
                val intent = Intent(context, ProductByIdActivity::class.java)
                intent.putExtra(PRODUCT_ID, categoriesItem.id.toString())
                context.startActivity(intent)
            }

            productAddToWishlist.setOnClickListener {
                if(tokenManager.token!= NO_LOGIN) {
                    if(wishlistClick){
                        wishlistClick=false
                        productAddToWishlist.load(R.drawable.favourite)
                        val intent = Intent("custom-message")
                        intent.putExtra("wishlist", true)
                        intent.putExtra("product_id", categoriesItem.id.toString())
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                    }else{
                        val intent = Intent("custom-message")
                        intent.putExtra("wishlist", true)
                        intent.putExtra("remove", true)
                        intent.putExtra("product_id", categoriesItem.id.toString())
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

                        wishlistClick=true
                        productAddToWishlist.load(R.drawable.ic_baseline_favorite_24)
                    }

                }else{
                    productFeature.notLoginWarningSnack(context, Snackbar.LENGTH_LONG)
                }
            }

            productAddToCompareList.setOnClickListener {
                val intent = Intent("custom-message")
                intent.putExtra("compare_list", true)
                intent.putExtra("product_id", categoriesItem.id.toString())
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

            }


        }

    }
}