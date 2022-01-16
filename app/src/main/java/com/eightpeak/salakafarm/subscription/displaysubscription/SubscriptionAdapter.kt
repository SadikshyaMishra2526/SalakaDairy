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
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.date.AD
import com.eightpeak.salakafarm.utils.Constants
import com.eightpeak.salakafarm.utils.GeneralUtils
import kotlinx.android.synthetic.main.categories_item.view.*
import kotlinx.android.synthetic.main.subscription_date_item.view.*

class SubscriptionAdapter(private val onClickListener: (View, DeliveryHistory) -> Unit) :
    RecyclerView.Adapter<SubscriptionAdapter.DataListViewHolder>() {

    inner class DataListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<DeliveryHistory>() {

        override fun areItemsTheSame(oldItem: DeliveryHistory, newItem: DeliveryHistory): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: DeliveryHistory,
            newItem: DeliveryHistory
        ): Boolean {
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
        val userPrefManager = UserPrefManager(App.getContext())
         val ad= AD()
        holder.itemView.apply {

            var todayDate: String = ad.convertDate(GeneralUtils.getTodayDate())
            // TODO Auto-generated method stub
            val dateList: Array<String> = todayDate.split("-".toRegex()).toTypedArray()
            val dayOnly: Array<String> = dateList[2].split("\n".toRegex()).toTypedArray()
            val today=Integer.parseInt(dayOnly[0])
            date_item.text = dateDetails.date.toString()

               if (dateDetails.alter_status == 3) {
                    alter_status.visibility = View.GONE
                } else if (dateDetails.alter_status == 2) {
                    alter_status.text = "+"+dateDetails.alter_qty+" litre"
                    alter_status.setTextColor(Color.GREEN)
                   date_item.setTextColor(Color.GREEN)
                } else if (dateDetails.alter_status == 1) {
                    alter_status.text = "Cancelled"
                   date_item.setTextColor(Color.RED)
                   alter_status.setTextColor(Color.RED)
                } else if (dateDetails.alter_status == 0) {
                   alter_status.text = "-"+dateDetails.alter_qty+" litre"
                    alter_status.setTextColor(Color.BLUE)
                   date_item.setTextColor(Color.BLUE)
                }else if (dateDetails.alter_status == null) {
                   alter_status.visibility = View.GONE
                }

            if (dateDetails.date!! < today) {
                if (dateDetails.delivery_count == 0) {
                    sub_item.setBackgroundColor(resources.getColor(R.color.tab_indicator_gray))
                } else {
                    if (userPrefManager.deliveryPeriod == 2) {
                        if(dateDetails.delivery_count==1){
                            morning_delivery.visibility = View.VISIBLE
                        }else if(dateDetails.delivery_count==2){
                            morning_delivery.visibility = View.VISIBLE
                            evening_delivery.visibility = View.VISIBLE
                        }
                    }else{
                        morning_delivery.visibility = View.VISIBLE
                    }
                    sub_item.setBackgroundColor(resources.getColor(R.color.sub_color_lighter))
                }
            } else if (dateDetails.date == today) {
                sub_item.setBackgroundColor(resources.getColor(R.color.yellow_lighter))
                if (userPrefManager.deliveryPeriod == 2) {
                    if(dateDetails.delivery_count==1){
                        morning_delivery.visibility = View.VISIBLE
                    }else if(dateDetails.delivery_count==2){
                        morning_delivery.visibility = View.VISIBLE
                        evening_delivery.visibility = View.VISIBLE
                        sub_item.setBackgroundColor(resources.getColor(R.color.sub_color_lighter))
                    }
                }else if(userPrefManager.deliveryPeriod == 1){
                    morning_delivery.visibility = View.VISIBLE
                }else{
                    morning_delivery.visibility = View.GONE
                }
            } else if (dateDetails.date > today) {
                sub_item.setBackgroundColor(resources.getColor(R.color.white))
                holder.itemView.setOnClickListener { view ->
                    onClickListener.invoke(view, dateDetails)
                }
            }
        }

    }
}