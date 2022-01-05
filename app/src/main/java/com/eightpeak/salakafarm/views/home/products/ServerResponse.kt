package com.eightpeak.salakafarm.views.home.products

import com.google.gson.annotations.SerializedName

data class ServerResponse (
    @SerializedName("id") val id : Int,
    @SerializedName("product_id") val product_id : Int,
    @SerializedName("customer_id") val customer_id : Int,
    @SerializedName("qty") val qty : Int,
    @SerializedName("options") val options : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("latlng") val latlng : String

    ) {
    override fun toString(): String {
        return "ServerResponse(id=$id, product_id=$product_id, customer_id=$customer_id, qty=$qty, options='$options', created_at='$created_at', updated_at='$updated_at')"
    }
}