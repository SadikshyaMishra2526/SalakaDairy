package com.eightpeak.salakafarm.views.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.product_item.view.*

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
        Log.i("TAG", "onBindViewHolder:Categories " + productItems.image)
        holder.itemView.apply {
            product_thumbnail.load(EndPoints.BASE_URL + productItems.image)

            var userPrefManager = UserPrefManager(App.getContext())

            if (productItems.descriptions.isNotEmpty()) {
                if (userPrefManager.language.equals("ne")) {
                    product_name.text = productItems.descriptions[1].name
                    product_price.text =
                        context.getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(productItems.price.toString())

                } else {
                    product_name.text = productItems.descriptions[0].name
                    product_price.text =
                        context.getString(R.string.rs) + productItems.price.toString()

                }
            }

            bt_add_to_cart.setOnClickListener {
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