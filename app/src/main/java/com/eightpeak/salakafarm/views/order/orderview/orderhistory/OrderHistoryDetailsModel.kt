package com.eightpeak.salakafarm.views.order.orderview.orderhistory

import com.google.gson.annotations.SerializedName

class OrderHistoryDetailsModel (

    @SerializedName("order_details") val order_details : Order_details
    )
data class Order_details (

    @SerializedName("id") val id : Int,
    @SerializedName("customer_id") val customer_id : Int,
    @SerializedName("domain") val domain : String,
    @SerializedName("subtotal") val subtotal : Int,
    @SerializedName("shipping") val shipping : Int,
    @SerializedName("discount") val discount : Int,
    @SerializedName("payment_status") val payment_status : Payment_status,
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
    @SerializedName("phone") val phone : String,
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
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("refId") val refId : String,
    @SerializedName("oid") val oid : String,
    @SerializedName("details") val details : List<Details>,
    @SerializedName("order_total") val order_total : List<Order_total>,
    @SerializedName("order_status") val order_status : Order_status,
    @SerializedName("history") val history : List<History>
)
data class Details (

    @SerializedName("id") val id : Int,
    @SerializedName("order_id") val order_id : Int,
    @SerializedName("product_id") val product_id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("price") val price : Int,
    @SerializedName("qty") val qty : Int,
    @SerializedName("store_id") val store_id : Int,
    @SerializedName("total_price") val total_price : Int,
    @SerializedName("tax") val tax : Int,
    @SerializedName("sku") val sku : String,
    @SerializedName("currency") val currency : String,
    @SerializedName("exchange_rate") val exchange_rate : Double,
    @SerializedName("attribute") val attribute : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String
)
data class Order_total (

    @SerializedName("id") val id : Int,
    @SerializedName("order_id") val order_id : Int,
    @SerializedName("title") val title : String,
    @SerializedName("code") val code : String,
    @SerializedName("value") val value : Int,
    @SerializedName("text") val text : String,
    @SerializedName("sort") val sort : Int,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String
)

data class History (

    @SerializedName("id") val id : Int,
    @SerializedName("order_id") val order_id : Int,
    @SerializedName("content") val content : String,
    @SerializedName("admin_id") val admin_id : Int,
    @SerializedName("customer_id") val customer_id : Int,
    @SerializedName("order_status_id") val order_status_id : Int,
    @SerializedName("add_date") val add_date : String
)

data class Payment_status (

    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String
)


data class Order_status (

    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String
)