package com.eightpeak.salakafarm.views.home.products.productbyid

import com.google.gson.annotations.SerializedName

data class Pivot (

	@SerializedName("product_id") val product_id : Int,
	@SerializedName("category_id") val category_id : Int
)