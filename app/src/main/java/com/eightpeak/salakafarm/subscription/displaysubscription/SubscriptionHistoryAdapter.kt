package com.eightpeak.salakafarm.subscription.displaysubscription

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.subscription.displaysubscription.models.MoreInfo
import com.eightpeak.salakafarm.subscription.displaysubscription.models.SubHistoryModel
import kotlinx.android.synthetic.main.subscription_date_item.view.*

class SubscriptionHistoryAdapter( private val onClickListener: (View, MoreInfo) -> Unit) : RecyclerView.Adapter<SubscriptionHistoryAdapter.DataListViewHolder>() {

    inner class DataListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<MoreInfo>() {

        override fun areItemsTheSame(oldItem: MoreInfo, newItem: MoreInfo): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: MoreInfo, newItem: MoreInfo): Boolean {
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
//            if( dateDetails.date>27){
//                if(dateDetails.delivery_count!=0){
//                    date_item.text = dateDetails.date.toString()
//                }else{
//                    date_item.text = dateDetails.date.toString()
//                    holder.itemView.setOnClickListener { view ->
//                        onClickListener.invoke(view, dateDetails)
//                    }
//                }
//                sub_item.setBackgroundColor(Color.GREEN)
//            }else{
//                sub_item.setBackgroundColor(Color.GRAY)
//            }
        }

    }
}