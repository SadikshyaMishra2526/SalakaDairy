package com.eightpeak.salakafarm.views.home.products

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.utils.AppUtils
import com.eightpeak.salakafarm.utils.Constants.Companion.PRODUCT_ID
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.utils.GeneralUtils
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdActivity
import kotlinx.android.synthetic.main.product_item.view.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.fragment.app.FragmentActivity

import android.os.Bundle





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
        Log.i("TAG", "onBindViewHolder:Categories " + categoriesItem.image)
        holder.itemView.apply {
            product_thumbnail.load(EndPoints.BASE_URL + categoriesItem.image)

            var userPrefManager = UserPrefManager(App.getContext())

            if (categoriesItem.descriptions.isNotEmpty()) {
                if (userPrefManager.language.equals("ne")) {
                    product_name.text = categoriesItem.descriptions[1].name
                    product_price.text =
                        context.getString(R.string.rs) + " " + GeneralUtils.getUnicodeNumber(categoriesItem.price.toString())

                } else {
                    product_name.text = categoriesItem.descriptions[0].name
                    product_price.text =
                        context.getString(R.string.rs) + categoriesItem.price.toString()

                }
            }

            bt_add_to_cart.setOnClickListener {
                val args = Bundle()
                args.putString(PRODUCT_ID, categoriesItem.id.toString())
                val bottomSheet = AddToCartView()
                bottomSheet.arguments = args
                bottomSheet.show(
                    (context as FragmentActivity).supportFragmentManager,
                    bottomSheet.tag
                )
            }
            productViewItem.setOnClickListener {
                val intent = Intent(context, ProductByIdActivity::class.java)
                intent.putExtra(PRODUCT_ID, categoriesItem.id.toString())
                context.startActivity(intent)
            }
        }

    }
}