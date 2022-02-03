package com.eightpeak.salakafarm.views.home.products

import com.google.gson.annotations.SerializedName

data class ServerResponse (

    @SerializedName("error") val error : Int,
    @SerializedName("message") val message : String,

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
        return "ServerResponse(id=$id, product_id=$product_id, customer_id=$customer_id, qty=$qty, options='$options', created_at='$created_at', updated_at='$updated_at', latlng='$latlng')"
    }
}


data class UpdatePasswordResponse(
    @SerializedName("error") val error : Int,
    @SerializedName("message") val message : String,
)
data class ForgotPasswordResponse(
    @SerializedName("error") val error : Int,
    @SerializedName("msg") val errorMessage : String,
    @SerializedName("message") val successMessage : String,
)

data class UserProfileResponse(
    @SerializedName("error") val error : Int,
    @SerializedName("message") val message : String,
)

data class UserAddressEdit(
    @SerializedName("error") val error : Int,
    @SerializedName("message") val message : String,
)

class GoogleLoginResponse (
    @SerializedName("error") val error : String,
    @SerializedName("subscription") val subscription : Boolean,
    @SerializedName("access_token") val access_token : String,
    @SerializedName("token_type") val token_type : String,
    @SerializedName("expires_at") val expires_at : String)
data class Success (
    @SerializedName("token") val token : String
)

data class OrderResponse (
    @SerializedName("error") val error : Int,
    @SerializedName("message") val message : String,
    @SerializedName("orderID") val orderID : String
)