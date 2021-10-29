package com.eightpeak.salakafarm.views.home.categories

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import com.eightpeak.salakafarm.views.home.categories.categoriesbyid.CategoriesByIdActivity
import kotlinx.android.synthetic.main.categories_item.view.*

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    inner class CategoriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Data>() {

        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
            Log.i("TAG", "areItemsTheSame: "+oldItem.image)
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoriesViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.categories_item,
            parent,
            false
        )
    )
    override fun getItemCount() =  differ.currentList.size


    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val categoriesItem = differ.currentList[position]
        holder.itemView.apply {

            categories_thumbnail.load(BASE_URL+categoriesItem.image)
            categories_name.text = categoriesItem.descriptions[0].title
            categories_by_id.setOnClickListener {
                val intent = Intent(context, CategoriesByIdActivity::class.java)
                intent.putExtra(Constants.CATEGORIES_ID,categoriesItem.id.toString())
                context.startActivity(intent)
            }
        }
    }
}