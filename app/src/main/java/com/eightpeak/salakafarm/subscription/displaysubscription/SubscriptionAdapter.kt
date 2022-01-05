package com.eightpeak.salakafarm.subscription.displaysubscription

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.views.home.products.AddToCartView
import com.eightpeak.salakafarm.views.home.products.Data
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdActivity
import kotlinx.android.synthetic.main.product_item.view.*

class SubscriptionAdapter : RecyclerView.Adapter<SubscriptionAdapter.ProductListViewHolder>() {

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
            product_thumbnail.load(EndPoints.BASE_URL + categoriesItem.image)

            var userPrefManager = UserPrefManager(App.getContext())
            if(categoriesItem.stock>0){
                out_of_stock.visibility= View.GONE
            }else{
                out_of_stock.visibility= View.VISIBLE
            }
            if (categoriesItem.descriptions.isNotEmpty()) {
                if (userPrefManager.language.equals("ne")) {
                    product_name.text = categoriesItem.descriptions[1].name

                    if(!categoriesItem.cost.equals("0")){
                        product_price_discount.text= GeneralUtils.getUnicodeNumber(categoriesItem.price.toString())
                        product_price_discount.paintFlags = product_price_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        product_price.text =
                            context.getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(categoriesItem.cost.toString())
                    }else{
                        product_price.text =
                            context.getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(categoriesItem.price.toString())
                    }

                } else {
                    product_name.text = categoriesItem.descriptions[0].name

                    if(!categoriesItem.cost.equals("0")){
                        product_price_discount.text=categoriesItem.price.toString()
                        product_price_discount.paintFlags = product_price_discount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        product_price.text =
                            context.getString(R.string.rs) + categoriesItem.cost.toString()
                    }else{
                        product_price.text =
                            context.getString(R.string.rs) + categoriesItem.price.toString()
                    }
                }
            }


            product_feature.text=categoriesItem.sku
            bt_add_to_cart.setOnClickListener {
                val args = Bundle()
                args.putString(Constants.PRODUCT_ID, categoriesItem.id.toString())
                val bottomSheet = AddToCartView()
                bottomSheet.arguments = args
                bottomSheet.show(
                    (context as FragmentActivity).supportFragmentManager,
                    bottomSheet.tag
                )
            }
            productViewItem.setOnClickListener {
                val intent = Intent(context, ProductByIdActivity::class.java)
                intent.putExtra(Constants.PRODUCT_ID, categoriesItem.id.toString())
                context.startActivity(intent)
            }

            product_add_to_wishlist.setOnClickListener {
                val intent = Intent("custom-message")
                intent.putExtra("wishlist", true)
                intent.putExtra("product_id", categoriesItem.id.toString())
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

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