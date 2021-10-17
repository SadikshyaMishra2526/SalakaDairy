package com.eightpeak.salakafarm.views.home.categories.categoriesbyid

import com.google.gson.annotations.SerializedName

data class CategoriesByIdModel(
    @SerializedName("id") val id : Int,
    @SerializedName("image") val image : String,
    @SerializedName("alias") val alias : String,
    @SerializedName("parent") val parent : Int,
    @SerializedName("top") val top : Int,
    @SerializedName("status") val status : Int,
    @SerializedName("sort") val sort : Int,
    @SerializedName("descriptions") val descriptions : List<Descriptions>
)

data class Descriptions (
    @SerializedName("category_id") val category_id : Int,
    @SerializedName("lang") val lang : String,
    @SerializedName("title") val title : String,
    @SerializedName("keyword") val keywordword : String,
    @SerializedName("description") val description : String
)