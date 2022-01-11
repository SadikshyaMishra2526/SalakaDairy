package com.eightpeak.salakafarm.subscription.displaysubscription

import DeliveryHistory
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.utils.Constants
import kotlinx.android.synthetic.main.categories_item.view.*
import kotlinx.android.synthetic.main.subscription_date_item.view.*

class SubscriptionAdapter(   private val onClickListener: (View, DeliveryHistory) -> Unit) : RecyclerView.Adapter<SubscriptionAdapter.DataListViewHolder>() {

    inner class DataListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<DeliveryHistory>() {

        override fun areItemsTheSame(oldItem: DeliveryHistory, newItem: DeliveryHistory): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: DeliveryHistory, newItem: DeliveryHistory): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataListViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.subscription_date_item,
            parent,
            false
        )
    )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: DataListViewHolder, position: Int) {
        val dateDetails = differ.currentList[position]
        holder.itemView.apply {
            date_item.text = dateDetails.date.toString()
            if(  dateDetails.date==27||dateDetails.date>27){
                if(dateDetails.delivery_count!=0){
                    date_item.text = dateDetails.date.toString()
                }else{
                    date_item.text = dateDetails.date.toString()
                    holder.itemView.setOnClickListener { view ->
                        onClickListener.invoke(view, dateDetails)
                    }
                }
                sub_item.setBackgroundColor(Color.GREEN)
            }else{
                alter_subscription.setCardBackgroundColor(Color.GRAY)
            }



        }

    }
}