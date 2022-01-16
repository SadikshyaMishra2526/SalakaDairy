package com.eightpeak.salakafarm.subscription.displaysubscription

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.subscription.displaysubscription.models.MoreInfo1
import com.eightpeak.salakafarm.subscription.displaysubscription.models.SubscriptionHistoryModel
import kotlinx.android.synthetic.main.subscription_date_item.view.*
import kotlinx.android.synthetic.main.subscription_history_item.view.*

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
            if (subscriptionHistory != null) {
               history_date.text=subscriptionHistory.date
                Log.i("TAG", "onBindViewHolder: "+subscriptionHistory.deliveryAlter?.size)

                   if(subscriptionHistory.deliveryAlter==null){
                       sub_history_layout.visibility=View.GONE
                   }else{
                       for (i in subscriptionHistory.deliveryAlter){
                           if(i.alter_status==0){
                               alter_status_history.text="Milk Deducted"
                               alter_status_history.setTextColor(Color.BLUE)
                           }else if(i.alter_status==1){
                               alter_status_history.text="Subscription Cancelled"
                               alter_status_history.setTextColor(Color.RED)
                           }else if(i.alter_status==2){
                               alter_status_history.text="Milk added"
                               alter_status_history.setTextColor(Color.GREEN)
                           }
                           alter_period.text=i.alter_peroid.toString()
                           alter_quantity.text=i.qty.toString()+" litre"
                       }
                   }


    if(subscriptionHistory.deliveryHistory?.size!=0){
        for(i in subscriptionHistory.deliveryHistory!!){
            order_delivered_by.text=i.employee?.name
            delivered_quantity.text=i.qty.toString()+" litre"
        }
    }



            }

        }
    }
}