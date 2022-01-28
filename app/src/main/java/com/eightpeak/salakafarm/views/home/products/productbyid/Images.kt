package com.eightpeak.salakafarm.views.home.products.productbyid

import com.google.gson.annotations.SerializedName

data class Images (

	@SerializedName("id") val id : Int,
	@SerializedName("image") val image : String,
	@SerializedName("product_id") val product_id : Int
)