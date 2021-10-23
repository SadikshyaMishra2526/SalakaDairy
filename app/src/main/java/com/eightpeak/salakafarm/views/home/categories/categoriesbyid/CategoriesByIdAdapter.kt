package com.eightpeak.salakafarm.views.home.categories.categoriesbyid

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.views.home.products.Data
import kotlinx.android.synthetic.main.product_item.view.*

class CategoriesByIdAdapter : RecyclerView.Adapter<CategoriesByIdAdapter.CategoriesByIdViewHolder>() {

    inner class CategoriesByIdViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Data>() {

        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
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
        Log.i("TAG", "onBindViewHolder:Categories "+categoriesItem.image)
        holder.itemView.apply {
            product_thumbnail.load(EndPoints.BASE_URL +categoriesItem.image)
            product_name.text = categoriesItem.descriptions[0].name
            product_price.text = categoriesItem.price.toString()
        }
    }
}