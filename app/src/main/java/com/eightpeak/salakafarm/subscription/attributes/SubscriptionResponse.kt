package com.eightpeak.salakafarm.subscription.attributes

import com.google.gson.annotations.SerializedName



class SubscriptionResponse (@SerializedName("subscribed") val subscribed : Subscribed)

data class Subscribed (
    @SerializedName("customer_id") val customer_id : Int,
    @SerializedName("sub_package_id") val sub_package_id : Int,
    @SerializedName("delivery_peroid") val delivery_peroid : Int,
    @SerializedName("starting_date") val starting_date : String,
    @SerializedName("branch_id") val branch_id : Int,
    @SerializedName("subscribed_price") val subscribed_price : Int,
    @SerializedName("subscribed_discount") val subscribed_discount : Int,
    @SerializedName("subscribed_total_amount") val subscribed_total_amount : Int,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("id") val id : Int
)