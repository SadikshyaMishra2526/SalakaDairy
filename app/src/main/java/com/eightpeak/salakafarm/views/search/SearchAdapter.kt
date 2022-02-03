package com.eightpeak.salakafarm.views.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
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
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdActivity

class SearchAdapter  : RecyclerView.Adapter<SearchAdapter.ProductListViewHolder>() {

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
        val productItems = differ.currentList[position]


        holder.itemView.apply {
            val productThumbnail = findViewById<ImageView>(R.id.product_thumbnail)
            val btAddToCart = findViewById<TextView>(R.id.bt_add_to_cart)
            val productViewItem = findViewById<CardView>(R.id.productViewItem)
            val productName = findViewById<TextView>(R.id.product_name)
            productThumbnail.load(EndPoints.BASE_URL + productItems.image)

            val productPrice = findViewById<TextView>(R.id.product_price)
            var userPrefManager = UserPrefManager(App.getContext())

            if (productItems.descriptions.isNotEmpty()) {
                if (userPrefManager.language.equals("ne")) {
                    productName.text = productItems.descriptions[1].name
                    productPrice.text =
                        context.getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(productItems.price.toString())

                } else {
                    productName.text = productItems.descriptions[0].name
                    productPrice.text =
                        context.getString(R.string.rs) + productItems.price.toString()
                }
            }

            btAddToCart.setOnClickListener {
                val args = Bundle()
                args.putString(Constants.PRODUCT_ID, productItems.id.toString())
                val bottomSheet = AddToCartView()
                bottomSheet.arguments = args
                bottomSheet.show(
                    (context as FragmentActivity).supportFragmentManager,
                    bottomSheet.tag
                )
            }
            productViewItem.setOnClickListener {
                val intent = Intent(context, ProductByIdActivity::class.java)
                intent.putExtra(Constants.PRODUCT_ID, productItems.id.toString())
                context.startActivity(intent)
            }
        }

    }
}