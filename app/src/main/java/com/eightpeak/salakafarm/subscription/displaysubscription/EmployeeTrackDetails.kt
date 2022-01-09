package com.eightpeak.salakafarm.subscription.displaysubscription

import com.google.gson.annotations.SerializedName

data  class EmployeeTrackDetails (

    @SerializedName("latlng") val latlng : Latlng
)
data class Latlng (

    @SerializedName("id") val id : Int,
    @SerializedName("first_name") val first_name : String,
    @SerializedName("last_name") val last_name : String,
    @SerializedName("email") val email : String,
    @SerializedName("phone") val phone : String,
    @SerializedName("gender") val gender : Int,
    @SerializedName("citizenship") val citizenship : String,
    @SerializedName("avatar") val avatar : String,
    @SerializedName("branch_id") val branch_id : Int,
    @SerializedName("approved_at") val approved_at : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("lat") val lat : Double,
    @SerializedName("lng") val lng : Double,
    @SerializedName("fcm_token") val fcm_token : String,
    @SerializedName("name") val name : String
)