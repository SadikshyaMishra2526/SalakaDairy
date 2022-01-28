package com.eightpeak.salakafarm.subscription.displaysubscription

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eightpeak.salakafarm.App
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.database.UserPrefManager
import com.eightpeak.salakafarm.date.AD
import com.eightpeak.salakafarm.subscription.displaysubscription.models.DeliveryHistoryDisplay
import com.eightpeak.salakafarm.utils.GeneralUtils

class SubscriptionAdapter(private val onClickListener: (View, DeliveryHistoryDisplay) -> Unit) :
    RecyclerView.Adapter<SubscriptionAdapter.DataListViewHolder>() {

    inner class DataListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<DeliveryHistoryDisplay>() {

        override fun areItemsTheSame(oldItem: DeliveryHistoryDisplay, newItem: DeliveryHistoryDisplay): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: DeliveryHistoryDisplay,
            newItem: DeliveryHistoryDisplay
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

            val dateItem = findViewById<TextView>(R.id.date_item)
            val dateEng = findViewById<TextView>(R.id.date_eng)
            val alterStatus = findViewById<TextView>(R.id.alter_status)
            val subItem = findViewById<LinearLayout>(R.id.sub_item)
            val morningDelivery = findViewById<ImageView>(R.id.morning_delivery)
            val eveningDelivery = findViewById<ImageView>(R.id.evening_delivery)

            var todayDate: String = ad.convertDate(GeneralUtils.getTodayDate())
            // TODO Auto-generated method stub
            val dateList: Array<String> = todayDate.split("-".toRegex()).toTypedArray()
            val dayOnly: Array<String> = dateList[2].split("\n".toRegex()).toTypedArray()
            val today=Integer.parseInt(dayOnly[0])
            dateItem.text = dateDetails.date.toString()
            dateEng.text = dateDetails.date_eng.toString()

               if (dateDetails.alter_status == 3) {
                    alterStatus.visibility = View.GONE
                } else if (dateDetails.alter_status == 2) {
                    alterStatus.text = "+"+dateDetails.alter_qty+" litre"
                    alterStatus.setTextColor(Color.GREEN)
                   dateItem.setTextColor(Color.GREEN)
                } else if (dateDetails.alter_status == 1) {
                    alterStatus.text = "Cancelled"
                   dateItem.setTextColor(Color.RED)
                   alterStatus.setTextColor(Color.RED)
                } else if (dateDetails.alter_status == null) {
                   alterStatus.visibility = View.GONE
                }

            if (dateDetails.date!! < today) {
                if (dateDetails.delivery_count == 0) {
                    subItem.setBackgroundColor(resources.getColor(R.color.tab_indicator_gray))
                } else {
                    if (userPrefManager.deliveryPeriod == 2) {
                        if(dateDetails.delivery_count==1){
                            morningDelivery.visibility = View.VISIBLE
                        }else if(dateDetails.delivery_count==2){
                            morningDelivery.visibility = View.VISIBLE
                            eveningDelivery.visibility = View.VISIBLE
                        }
                    }else{
                        morningDelivery.visibility = View.VISIBLE
                    }
                    subItem.setBackgroundColor(resources.getColor(R.color.sub_color_lighter))
                }
            } else if (dateDetails.date == today) {

                subItem.setBackgroundColor(resources.getColor(R.color.yellow_lighter))
                if (userPrefManager.deliveryPeriod == 2) {
                    if(dateDetails.delivery_count==1){
                        morningDelivery.visibility = View.VISIBLE
                    }else if(dateDetails.delivery_count==2){
                        morningDelivery.visibility = View.VISIBLE
                        eveningDelivery.visibility = View.VISIBLE
                        subItem.setBackgroundColor(resources.getColor(R.color.sub_color_lighter))
                    }
                }else if(userPrefManager.deliveryPeriod == 1||userPrefManager.deliveryPeriod == 0){
                    if(dateDetails.delivery_count==0){
                        morningDelivery.visibility = View.GONE
                     }else{
                        subItem.setBackgroundColor(resources.getColor(R.color.sub_color_lighter))
                        morningDelivery.visibility = View.VISIBLE

                    }
                }

            } else if (dateDetails.date > today) {
                subItem.setBackgroundColor(resources.getColor(R.color.white))
                holder.itemView.setOnClickListener { view ->
                    onClickListener.invoke(view, dateDetails)
                }
            }
        }

    }
}