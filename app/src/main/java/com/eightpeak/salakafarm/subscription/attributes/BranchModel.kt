package com.eightpeak.salakafarm.subscription.attributes

import com.google.gson.annotations.SerializedName

class BranchModel (
    @SerializedName("branches") val branches : List<Branches>
)

data class Branches (

    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("contact") val contact : String,
    @SerializedName("address") val address : String,
    @SerializedName("latitude") val latitude : Double,
    @SerializedName("longitude") val longitude : Double,
    @SerializedName("capacity") val capacity : String,
    @SerializedName("account_number") val account_number : String,
    @SerializedName("qrcode") val qrcode : String,
    @SerializedName("user_id") val user_id : Int,
    @SerializedName("branch_status") val branch_status : Int,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("bank") val bank : String,
    @SerializedName("account_holder") val account_holder : String,
    @SerializedName("sub_packages_count") val sub_packages_count : Int
)