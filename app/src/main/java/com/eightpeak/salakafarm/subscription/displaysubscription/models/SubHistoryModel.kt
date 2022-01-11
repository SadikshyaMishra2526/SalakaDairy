package com.eightpeak.salakafarm.subscription.displaysubscription.models

import com.google.gson.annotations.SerializedName

data class SubHistoryModel (
    @SerializedName("moreInfo") val moreInfo : List<List<MoreInfo>>
        )

data class MoreInfo (

    @SerializedName("deliveryHistory") val deliveryHistory : DeliveryHistory,
    @SerializedName("deliveryAlter") val deliveryAlter : DeliveryAlter,
    @SerializedName("date") val date : String
)


data class DeliveryAlter (

    @SerializedName("deliveryHistory") val deliveryHistory : String

)