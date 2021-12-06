package com.eightpeak.salakafarm.views.popup

import com.google.gson.annotations.SerializedName

data class PopUpModel (
    @SerializedName("popup") val popup : Popup
)

data class Popup (

    @SerializedName("id") val id : Int,
    @SerializedName("image") val image : String,
    @SerializedName("status") val status : Int,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String
)