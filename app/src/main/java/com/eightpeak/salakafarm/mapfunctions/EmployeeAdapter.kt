package com.eightpeak.salakafarm.mapfunctions

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.subscription.attributes.Branches

class EmployeeAdapter : RecyclerView.Adapter<EmployeeAdapter.ProductListViewHolder>() {

    inner class ProductListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Branches>() {

        override fun areItemsTheSame(oldItem: Branches, newItem: Branches): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Branches, newItem: Branches): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductListViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.employee_details_item,
            parent,
            false
        )
    )

    override fun getItemCount() = differ.currentList.size


    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        val branchDetails = differ.currentList[position]
        holder.itemView.apply {

            val branchName = findViewById<TextView>(R.id.branch_name_map)
            val branchAddress = findViewById<TextView>(R.id.branch_address)
            val branchContact = findViewById<TextView>(R.id.branch_contact)
            val branchSubscription = findViewById<TextView>(R.id.branch_subscription)
            branchName.text=branchDetails.name
            branchAddress.text=branchDetails.address
            branchContact.text=branchDetails.contact
            branchSubscription.text=branchDetails.sub_packages_count.toString()

        }
    }
}