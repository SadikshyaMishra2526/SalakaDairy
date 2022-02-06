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
    @SerializedName("customer") val customer : Customer

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

data class Customer (
    @SerializedName("id") val id : Int,
    @SerializedName("first_name") val first_name : String,
    @SerializedName("last_name") val last_name : String,
    @SerializedName("first_name_kana") val first_name_kana : String,
    @SerializedName("last_name_kana") val last_name_kana : String,
    @SerializedName("email") val email : String,
    @SerializedName("sex") val sex : String,
    @SerializedName("birthday") val birthday : String,
    @SerializedName("address_id") val address_id : Int,
    @SerializedName("postcode") val postcode : String,
    @SerializedName("address1") val address1 : String,
    @SerializedName("address2") val address2 : String,
    @SerializedName("address3") val address3 : String,
    @SerializedName("company") val company : String,
    @SerializedName("country") val country : String,
    @SerializedName("phone") val phone : String,
    @SerializedName("store_id") val store_id : Int,
    @SerializedName("status") val status : Int,
    @SerializedName("group") val group : Int,
    @SerializedName("email_verified_at") val email_verified_at : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("provider") val provider : String,
    @SerializedName("provider_id") val provider_id : String,
    @SerializedName("fcm_token") val fcm_token : String,
    @SerializedName("avatar") val avatar : String,
    @SerializedName("name") val name : String
)