package com.eightpeak.salakafarm.subscription.attributes


import com.google.gson.annotations.SerializedName

data class SubscriptionItemModel (

	@SerializedName("sub_item") val sub_item : List<SubItem>
)
data class SubItem (

	@SerializedName("id") val id : Int,
	@SerializedName("image") val image : String,
	@SerializedName("sort") val sort : Int,
	@SerializedName("price") val price : Int,
	@SerializedName("unit") val unit : String,
	@SerializedName("created_at") val created_at : String,
	@SerializedName("updated_at") val updated_at : String,
	@SerializedName("descriptions") val descriptions : List<Descriptions>
)

//data class Descriptions (
//
//	@SerializedName("sub_item_id") val sub_item_id : Int,
//	@SerializedName("lang") val lang : String,
//	@SerializedName("title") val title : String,
//	@SerializedName("keyword") val keywordword : String,
//	@SerializedName("description") val description : String,
//	@SerializedName("content") val content : String
//)