package com.eightpeak.salakafarm.views.home.products.productbyid

import com.google.gson.annotations.SerializedName

data class Descriptions(

    @SerializedName("category_id") val category_id: Int,
    @SerializedName("lang") val lang: String,
    @SerializedName("title") val title: String,
    @SerializedName("name") val name: String,
    @SerializedName("main_name") val main_name: String,
    @SerializedName("keyword") val keywordword: String,
    @SerializedName("description") val description: String,
    @SerializedName("product_id") val product_id: Int,
    @SerializedName("content") val content: String
)