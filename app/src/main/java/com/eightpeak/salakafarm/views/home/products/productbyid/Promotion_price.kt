package com.eightpeak.salakafarm.views.home.products.productbyid

import com.google.gson.annotations.SerializedName

data class Promotion_price (

    @SerializedName("product_id") val product_id : Int?=null,
    @SerializedName("price_promotion") val price_promotion : String?=null,
    @SerializedName("date_start") val date_start : String?=null,
    @SerializedName("date_end") val date_end : String?=null,
    @SerializedName("status_promotion") val status_promotion : Int?=null,
    @SerializedName("created_at") val created_at : String?=null,
    @SerializedName("updated_at") val updated_at : String
)