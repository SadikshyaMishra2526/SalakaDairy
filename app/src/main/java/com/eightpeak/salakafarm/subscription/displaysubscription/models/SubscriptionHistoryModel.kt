package com.eightpeak.salakafarm.subscription.displaysubscription.models

import com.google.gson.annotations.SerializedName

data class SubscriptionHistoryModel (

    @SerializedName("subscription") val subscription : Subscription
) {
    override fun toString(): String {
        return "DisplaySubscriptionModel(subscription=$subscription)"
    }
}
data class Subscription (

    @SerializedName("id") val id : Int,
    @SerializedName("customer_id") val customer_id : Int,
    @SerializedName("sub_package_id") val sub_package_id : Int,
    @SerializedName("total_quantity") val total_quantity : Int,
    @SerializedName("remaining_quantity") val remaining_quantity : Int,
    @SerializedName("delivery_peroid") val delivery_peroid : Int,
    @SerializedName("starting_date") val starting_date : String,
    @SerializedName("subscribed_price") val subscribed_price : Int,
    @SerializedName("subscribed_discount") val subscribed_discount : Int,
    @SerializedName("subscribed_total_amount") val subscribed_total_amount : Int,
    @SerializedName("unit_per_day") val unit_per_day : Int,
    @SerializedName("branch_id") val branch_id : Int,
    @SerializedName("extra") val extra : String,
    @SerializedName("last_delivered_status") val last_delivered_status : Int,
    @SerializedName("last_delivered_at") val last_delivered_at : String,
    @SerializedName("expired_at") val expired_at : String,
    @SerializedName("address_id") val address_id : Int,
    @SerializedName("employee_id") val employee_id : Int,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("deliveryHistory") val deliveryHistory : List<DeliveryHistory>,
    @SerializedName("address") val address : Address,
    @SerializedName("sub_package") val sub_package : Sub_package,
    @SerializedName("branch") val branch : Branch
) {
    override fun toString(): String {
        return "Subscription(id=$id, customer_id=$customer_id, sub_package_id=$sub_package_id, total_quantity=$total_quantity, remaining_quantity=$remaining_quantity, delivery_peroid=$delivery_peroid,delivery=$deliveryHistory, starting_date='$starting_date', subscribed_price=$subscribed_price, subscribed_discount=$subscribed_discount, subscribed_total_amount=$subscribed_total_amount, unit_per_day=$unit_per_day, branch_id=$branch_id, extra='$extra', last_delivered_status=$last_delivered_status, last_delivered_at='$last_delivered_at', expired_at='$expired_at', address_id=$address_id, employee_id=$employee_id, created_at='$created_at', updated_at='$updated_at', address=$address, sub_package=$sub_package, branch=$branch)"
    }
}

data class Branch (

    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String
)

data class Sub_package (

    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String
)

data class Address (
    @SerializedName("id") val id : Int,
    @SerializedName("phone") val phone : String,
    @SerializedName("address1") val address1 : String,
    @SerializedName("lat") val lat : Double,
    @SerializedName("lng") val lng : Double
)

data class DeliveryHistory (

    @SerializedName("date") val date : Int,
    @SerializedName("delivery_count") val delivery_count : Int,
    @SerializedName("alter_status") val alter_status : Int

        )