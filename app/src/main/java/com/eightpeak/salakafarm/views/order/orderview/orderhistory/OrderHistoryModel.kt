package com.eightpeak.salakafarm.views.order.orderview.orderhistory

import com.google.gson.annotations.SerializedName

class OrderHistoryModel (

    @SerializedName("orderlist") val orderlist : List<Orderlist>)

data class Orderlist (

    @SerializedName("id") val id : Int,
    @SerializedName("customer_id") val customer_id : Int,
    @SerializedName("domain") val domain : String,
    @SerializedName("subtotal") val subtotal : Int,
    @SerializedName("shipping") val shipping : Int,
    @SerializedName("discount") val discount : Int,
    @SerializedName("payment_status") val payment_status : Int,
    @SerializedName("shipping_status") val shipping_status : Int,
    @SerializedName("status") val status : Int,
    @SerializedName("tax") val tax : Int,
    @SerializedName("total") val total : Int,
    @SerializedName("currency") val currency : String,
    @SerializedName("exchange_rate") val exchange_rate : Double,
    @SerializedName("received") val received : Int,
    @SerializedName("balance") val balance : Int,
    @SerializedName("first_name") val first_name : String,
    @SerializedName("last_name") val last_name : String,
    @SerializedName("first_name_kana") val first_name_kana : String,
    @SerializedName("last_name_kana") val last_name_kana : String,
    @SerializedName("address1") val address1 : String,
    @SerializedName("address2") val address2 : String,
    @SerializedName("address3") val address3 : String,
    @SerializedName("country") val country : String,
    @SerializedName("company") val company : String,
    @SerializedName("postcode") val postcode : String,
    @SerializedName("phone") val phone : Int,
    @SerializedName("email") val email : String,
    @SerializedName("comment") val comment : String,
    @SerializedName("payment_method") val payment_method : String,
    @SerializedName("shipping_method") val shipping_method : String,
    @SerializedName("user_agent") val user_agent : String,
    @SerializedName("device_type") val device_type : String,
    @SerializedName("ip") val ip : String,
    @SerializedName("transaction") val transaction : String,
    @SerializedName("store_id") val store_id : Int,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String
)