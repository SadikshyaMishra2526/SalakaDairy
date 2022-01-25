package com.eightpeak.salakafarm.subscription.displaysubscription.models

import com.google.gson.annotations.SerializedName

data class SubscriptionHistoryModel (

    @SerializedName("moreInfo") val moreInfo : List<MoreInfo1>?=null
) {
    override fun toString(): String {
        return "SubscriptionHistoryModel(moreInfo=$moreInfo)"
    }
}

data class MoreInfo1 (
    @SerializedName("deliveryHistory") val deliveryHistory : List<DeliveryHistory>?=null,
    @SerializedName("deliveryAlter") val deliveryAlter : List<DeliveryAlter1>?=null,
    @SerializedName("date") val date : String?=null,
    @SerializedName("date_nep") val date_nep : String?=null
)
data class DeliveryHistory (

    @SerializedName("employee_id") val employee_id : Int?=null,
    @SerializedName("qty") val qty : Int?=null,
    @SerializedName("created_at") val created_at : String?=null,
    @SerializedName("date") val date : String?=null,
    @SerializedName("employee") val employee : Employee?=null
)

data class DeliveryAlter1 (

    @SerializedName("alter_for") val alter_for : String?=null,
    @SerializedName("alter_status") val alter_status : Int?=null,
    @SerializedName("qty") val qty : Int?=null,
    @SerializedName("alter_peroid") val alter_peroid : String?=null,
    @SerializedName("date") val date : String?=null,
    @SerializedName("created_at") val created_at : String?=null
)
data class Employee (

    @SerializedName("id") val id : Int?=null,
    @SerializedName("first_name") val first_name : String?=null,
    @SerializedName("last_name") val last_name : String?=null,
    @SerializedName("name") val name : String?=null
)