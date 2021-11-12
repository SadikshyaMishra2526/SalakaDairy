package com.eightpeak.salakafarm.subscription.attributes

import com.google.gson.annotations.SerializedName

class SubscriptionPackageModel(

    @SerializedName("sub_item") val sub_item: List<Sub_item>
)

data class Sub_item(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("number_of_days") val number_of_days: Int,
    @SerializedName("sub_item_id") val sub_item_id: Int,
    @SerializedName("total_price") val total_price: Int,
    @SerializedName("discount_price") val discount_price: Int,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
)