package com.eightpeak.salakafarm.views.home.categories.categoriesbyid

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.eightpeak.salakafarm.views.home.products.AddToCartView
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdActivity
import kotlinx.android.synthetic.main.product_item.view.*

class CategoriesByIdAdapter : RecyclerView.Adapter<CategoriesByIdAdapter.CategoriesByIdViewHolder>() {

    inner class CategoriesByIdViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    lateinit var userPrefManager: UserPrefManager

    private val differCallback = object : DiffUtil.ItemCallback<Products>() {

        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoriesByIdViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.product_item,
            parent,
            false
        )
    )
    override fun getItemCount() =  differ.currentList.size


    override fun onBindViewHolder(holder: CategoriesByIdViewHolder, position: Int) {
        val categoriesItem = differ.currentList[position]
        userPrefManager= UserPrefManager(App.getContext())

        Log.i("TAG", "onBindViewHolder:Categories "+categoriesItem.image)
        holder.itemView.apply {
            product_thumbnail.load(EndPoints.BASE_URL +categoriesItem.image)
//            if(productsItem.descriptions.isNotEmpty()){
//              if(userPrefManager.language.equals("ne")){
//                  product_name.text = productsItem.descriptions[1].name
//              }else{
//                  product_name.text = productsItem.descriptions[0].name
//
//              }
//            }
            product_price.text = categoriesItem.price.toString()
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
        }
    }
}