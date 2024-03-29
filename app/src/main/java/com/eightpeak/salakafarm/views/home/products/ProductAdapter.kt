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
import kotlinx.android.synthetic.main.product_item.view.*
import androidx.fragment.app.FragmentActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.Constants.Companion.NO_LOGIN
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.utils.subutils.errorSnack
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

          val tokenManager = TokenManager.getInstance(context.getSharedPreferences(
                Constants.TOKEN_PREF,
                AppCompatActivity.MODE_PRIVATE
            ))
            product_thumbnail.load(EndPoints.BASE_URL + categoriesItem.image)
            rated_by.text = "("+categoriesItem.no_of_rating+") "

            val rating: Int? = categoriesItem?.average_rating?.roundToInt()

            if(rating==1){
                rating_1.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
            }else if(rating==2){
                rating_1.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_2.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
            }else if(rating==3){
                rating_1.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_2.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_3.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
            }else if(rating==4){
                rating_1.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_2.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_3.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_4.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
            }else if(rating==5){
                rating_1.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_2.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_3.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_4.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
                rating_5.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_star_rate_24))
            }

            val userPrefManager = UserPrefManager(App.getContext())
                  if(categoriesItem.stock!! >0){
                      out_of_stock.visibility=View.GONE
                  }else{
                      out_of_stock.visibility=View.VISIBLE
                  }
            if (categoriesItem.descriptions!!.isNotEmpty()) {
                if (userPrefManager.language.equals("ne")) {
                    product_name.text = categoriesItem?.descriptions[1].name

                    if(categoriesItem.promotion_price!=null){
                      product_price_discount.text=GeneralUtils.getUnicodeNumber(categoriesItem.price.toString())
                      product_price_discount.paintFlags = product_price_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                      product_price.text =
                          context.getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(categoriesItem.promotion_price?.price_promotion.toString())
                  }else{
                      product_price.text =
                          context.getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(categoriesItem.price.toString())
                  }

                } else {
                    product_name.text = categoriesItem?.descriptions?.get(0)?.name
                    Log.i("TAG", "onBindViewHolder: "+categoriesItem.promotion_price?.price_promotion)
                    if(categoriesItem.promotion_price==null){
                        product_price.text =
                            context.getString(R.string.rs) + categoriesItem.price.toString()
                    }else{

                        product_price_discount.text=categoriesItem.price.toString()
                        product_price_discount.paintFlags = product_price_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        product_price.text =
                            context.getString(R.string.rs) + categoriesItem.promotion_price?.price_promotion.toString()
                    }
                }
            }


            product_feature.text=categoriesItem.sku
            bt_add_to_cart.setOnClickListener {
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
                    product_feature.notLoginWarningSnack(context, Snackbar.LENGTH_LONG)
                }

            }
            productViewItem.setOnClickListener {
                val intent = Intent(context, ProductByIdActivity::class.java)
                intent.putExtra(PRODUCT_ID, categoriesItem.id.toString())
                context.startActivity(intent)
            }

            product_add_to_wishlist.setOnClickListener {
                if(tokenManager.token!= NO_LOGIN) {
                    if(wishlistClick){
                        wishlistClick=false
                        product_add_to_wishlist.load(R.drawable.favourite)
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
                        product_add_to_wishlist.load(R.drawable.ic_baseline_favorite_24)
                    }

                }else{
                    product_feature.notLoginWarningSnack(context, Snackbar.LENGTH_LONG)
                }
            }

            product_add_to_compare_list.setOnClickListener {
                val intent = Intent("custom-message")
                intent.putExtra("compare_list", true)
                intent.putExtra("product_id", categoriesItem.id.toString())
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

            }


        }

    }
}