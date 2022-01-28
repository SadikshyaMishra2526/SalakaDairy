package com.eightpeak.salakafarm.views.home.products.productbyid

import com.google.gson.annotations.SerializedName

data class Categories_description (

	@SerializedName("id") val id : Int,
	@SerializedName("image") val image : String,
	@SerializedName("alias") val alias : String,
	@SerializedName("parent") val parent : Int,
	@SerializedName("top") val top : Int,
	@SerializedName("status") val status : Int,
	@SerializedName("sort") val sort : Int,
	@SerializedName("pivot") val pivot : Pivot,
	@SerializedName("descriptions") val descriptions : List<Descriptions>
)