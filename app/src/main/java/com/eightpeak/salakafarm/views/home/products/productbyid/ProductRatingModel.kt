package com.eightpeak.salakafarm.views.home.products.productbyid

import com.google.gson.annotations.SerializedName

data class ProductRatingModel (

    @SerializedName("ratings") val ratings : Ratings
    )
data class Ratings (

    @SerializedName("current_page") val current_page : Int,
    @SerializedName("data") val data : List<Data>,
    @SerializedName("first_page_url") val first_page_url : String,
    @SerializedName("from") val from : Int,
    @SerializedName("last_page") val last_page : Int,
    @SerializedName("last_page_url") val last_page_url : String,
    @SerializedName("links") val links : List<Links>,
    @SerializedName("next_page_url") val next_page_url : String,
    @SerializedName("path") val path : String,
    @SerializedName("per_page") val per_page : Int,
    @SerializedName("prev_page_url") val prev_page_url : String,
    @SerializedName("to") val to : Int,
    @SerializedName("total") val total : Int
)

data class Links (

    @SerializedName("url") val url : String,
    @SerializedName("label") val label : String,
    @SerializedName("active") val active : Boolean
)

data class Data (

    @SerializedName("id") val id : Int,
    @SerializedName("rating") val rating : Int,
    @SerializedName("comment") val comment : String,
    @SerializedName("product_id") val product_id : Int,
    @SerializedName("customer_id") val customer_id : Int,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String
)