package com.eightpeak.salakafarm.views.addresslist

import com.google.gson.annotations.SerializedName

class AddressListModel (
    @SerializedName("address_list")
    val address_list: List<Address_list>)


data class Address_list (

    @SerializedName("id") val id : Int,
    @SerializedName("customer_id") val customer_id : Int,
    @SerializedName("first_name") val first_name : String,
    @SerializedName("last_name") val last_name : String,
    @SerializedName("first_name_kana") val first_name_kana : String,
    @SerializedName("last_name_kana") val last_name_kana : String,
    @SerializedName("postcode") val postcode : String,
    @SerializedName("address1") val address1 : String,
    @SerializedName("address2") val address2 : String,
    @SerializedName("address3") val address3 : String,
    @SerializedName("country") val country : String,
    @SerializedName("phone") val phone : String
)