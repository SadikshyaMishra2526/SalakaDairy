package com.eightpeak.salakafarm.views.gallery

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL


class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    inner class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GalleryViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.gallery_item_view,
            parent,
            false
        )
    )

    override fun getItemCount() = differ.currentList.size


    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val galleryList = differ.currentList[position]
        holder.itemView.apply {

            val galleryImageView = findViewById<ImageView>(R.id.gallery_image_view)
            galleryImageView.load(BASE_URL + galleryList.image)
        }
    }

}