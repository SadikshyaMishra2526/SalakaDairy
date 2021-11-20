package com.eightpeak.salakafarm.views.pages

import com.google.gson.annotations.SerializedName

data  class PageDetailsModel(
@SerializedName("page") val page : Page
)
data class Page (

    @SerializedName("id") val id : Int,
    @SerializedName("image") val image : String,
    @SerializedName("alias") val alias : String,
    @SerializedName("status") val status : Int,
    @SerializedName("descriptions") val descriptions : List<Descriptions>
)

data class Descriptions (

    @SerializedName("page_id") val page_id : Int,
    @SerializedName("lang") val lang : String,
    @SerializedName("title") val title : String,
    @SerializedName("keyword") val keywordword : String,
    @SerializedName("description") val description : String,
    @SerializedName("content") val content : String
)