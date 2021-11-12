package com.eightpeak.salakafarm.subscription.attributes

import com.google.gson.annotations.SerializedName

class BranchModel (
    @SerializedName("branches") val branches : List<Branches>
)

data class Branches (
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("contact") val contact : Int,
    @SerializedName("address") val address : String,
    @SerializedName("latitude") val latitude : Double,
    @SerializedName("longitude") val longitude : Double,
    @SerializedName("capacity") val capacity : Int,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String
)