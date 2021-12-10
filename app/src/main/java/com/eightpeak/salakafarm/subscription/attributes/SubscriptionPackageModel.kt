package com.eightpeak.salakafarm.subscription.attributes

import com.google.gson.annotations.SerializedName

class SubscriptionPackageModel(


    @SerializedName("sub_packages") val sub_packages : List<Sub_packages>
    )

data class Sub_item (

    @SerializedName("id") val id : Int,
    @SerializedName("image") val image : String,
    @SerializedName("sort") val sort : Int,
    @SerializedName("unit") val unit : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("descriptions") val descriptions : List<Descriptions>
)
data class Sub_packages (

    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("number_of_days") val number_of_days : Int,
    @SerializedName("sub_item_id") val sub_item_id : Int,
    @SerializedName("range") val range : String,
    @SerializedName("discount_price_per_unit") val discount_price_per_unit : Int,
    @SerializedName("branch_id") val branch_id : Int,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("unit_price") val unit_price : Int,
    @SerializedName("sub_item") val sub_item : Sub_item
)

data class Descriptions (

    @SerializedName("sub_item_id") val sub_item_id : Int,
    @SerializedName("lang") val lang : String,
    @SerializedName("title") val title : String,
    @SerializedName("keyword") val keywordword : String,
    @SerializedName("description") val description : String,
    @SerializedName("content") val content : String
)