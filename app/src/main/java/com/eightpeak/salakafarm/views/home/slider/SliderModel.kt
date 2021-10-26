package com.eightpeak.salakafarm.views.home.slider


import com.google.gson.annotations.SerializedName

data class SliderModel
    (
    @SerializedName("id") val id : Int,
    @SerializedName("title") val title : String,
    @SerializedName("image") val image : String,
    @SerializedName("url") val url : String)