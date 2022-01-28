package com.eightpeak.salakafarm.views.home.products.productbyid

import com.google.gson.annotations.SerializedName

class Description_Product(

    @SerializedName("product_id") val product_id : Int,
    @SerializedName("lang") val lang : String,
    @SerializedName("name") val title : String,
    @SerializedName("keyword") val keywordword : String,
    @SerializedName("description") val description : String,
    @SerializedName("content") val content : String
)