package com.eightpeak.salakafarm.views.home.products.productbyid

import com.google.gson.annotations.SerializedName

data class ProductRatingModel (

    @SerializedName("ratings") val ratings : Ratings
    )
data class Ratings (

    @SerializedName("current_page") val current_page : Int?=null,
    @SerializedName("data") val data : List<Data>?=null,
    @SerializedName("first_page_url") val first_page_url : String?=null,
    @SerializedName("from") val from : Int?=null,
    @SerializedName("last_page") val last_page : Int?=null,
    @SerializedName("last_page_url") val last_page_url : String?=null,
    @SerializedName("links") val links : List<Links>?=null,
    @SerializedName("next_page_url") val next_page_url : String?=null,
    @SerializedName("path") val path : String?=null,
    @SerializedName("per_page") val per_page : Int?=null,
    @SerializedName("prev_page_url") val prev_page_url : String?=null,
    @SerializedName("to") val to : Int?=null,
    @SerializedName("total") val total : Int
)

data class Customer (
    @SerializedName("first_name") val first_name : String?=null,
    @SerializedName("name") val name : String,
)
data class Links (

    @SerializedName("url") val url : String?=null,
    @SerializedName("label") val label : String?=null,
    @SerializedName("active") val active : Boolean
)

data class Data (
    @SerializedName("customer") val customer :Customer?=null,
    @SerializedName("id") val id : Int?=null,
    @SerializedName("rating") val rating : Int?=null,
    @SerializedName("comment") val comment : String?=null,
    @SerializedName("product_id") val product_id : Int?=null,
    @SerializedName("customer_id") val customer_id : Int?=null,
    @SerializedName("created_at") val created_at : String?=null,
    @SerializedName("updated_at") val updated_at : String
)