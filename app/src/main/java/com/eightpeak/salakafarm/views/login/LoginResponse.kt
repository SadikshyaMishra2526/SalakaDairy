package com.eightpeak.salakafarm.views.login

import com.google.gson.annotations.SerializedName

data class LoginResponse (

    @SerializedName("msg") val msg : String,
    @SerializedName("error") val error : Int,
    @SerializedName("subscription") val subscription : Boolean,
    @SerializedName("access_token") val access_token : String,
    @SerializedName("token_type") val token_type : String,
    @SerializedName("expires_at") val expires_at : String)