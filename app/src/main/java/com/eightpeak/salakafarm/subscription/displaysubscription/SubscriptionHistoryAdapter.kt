package com.eightpeak.salakafarm.subscription.displaysubscription

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.subscription.displaysubscription.models.MoreInfo1

class SubscriptionHistoryAdapter() : RecyclerView.Adapter<SubscriptionHistoryAdapter.DataListViewHolder>() {

    inner class DataListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<MoreInfo1>() {

        override fun areItemsTheSame(oldItem: MoreInfo1, newItem: MoreInfo1): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: MoreInfo1, newItem: MoreInfo1): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataListViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.subscription_history_item,
            parent,
            false
        )
    )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: DataListViewHolder, position: Int) {
        val subscriptionHistory = differ.currentList[position]
        holder.itemView.apply {

        }
    }
}