package com.eightpeak.salakafarm.views.comparelist

import com.google.gson.annotations.SerializedName

class CompareResponse (
    @SerializedName("products") val products : List<Products>
)

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
data class Promotion_price (

    @SerializedName("product_id") val product_id : Int,
    @SerializedName("price_promotion") val price_promotion : Int,
    @SerializedName("date_start") val date_start : String,
    @SerializedName("date_end") val date_end : String,
    @SerializedName("status_promotion") val status_promotion : Int,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String
)
data class Products (

    @SerializedName("id") val id : Int,
    @SerializedName("sku") val sku : String,
    @SerializedName("upc") val upc : String,
    @SerializedName("ean") val ean : String,
    @SerializedName("jan") val jan : String,
    @SerializedName("isbn") val isbn : String,
    @SerializedName("mpn") val mpn : String,
    @SerializedName("image") val image : String,
    @SerializedName("brand_id") val brand_id : Int,
    @SerializedName("supplier_id") val supplier_id : Int,
    @SerializedName("price") val price : Int,
    @SerializedName("cost") val cost : Int,
    @SerializedName("stock") val stock : Int,
    @SerializedName("sold") val sold : Int,
    @SerializedName("minimum") val minimum : Int,
    @SerializedName("weight_class") val weight_class : String,
    @SerializedName("weight") val weight : Int,
    @SerializedName("length_class") val length_class : String,
    @SerializedName("length") val length : Int,
    @SerializedName("width") val width : Int,
    @SerializedName("height") val height : Int,
    @SerializedName("kind") val kind : Int,
    @SerializedName("property") val property : String,
    @SerializedName("tax_id") val tax_id : String,
    @SerializedName("status") val status : Int,
    @SerializedName("sort") val sort : Int,
    @SerializedName("view") val view : Int,
    @SerializedName("alias") val alias : String,
    @SerializedName("date_lastview") val date_lastview : String,
    @SerializedName("date_available") val date_available : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("user_id") val user_id : Int,
    @SerializedName("images") val images : List<Images>,
    @SerializedName("descriptions") val descriptions : List<Descriptions>,
    @SerializedName("categories_description") val categories_description : List<Categories_description>,
    @SerializedName("promotion_price") val promotion_price : Promotion_price,
    @SerializedName("brand") val brand : Brand,
    @SerializedName("attributes") val attributes : List<String>
)
data class Images (

    @SerializedName("id") val id : Int,
    @SerializedName("image") val image : String,
    @SerializedName("product_id") val product_id : Int
)

data class Descriptions (

    @SerializedName("category_id") val category_id : Int,
    @SerializedName("lang") val lang : String,
    @SerializedName("title") val title : String,
    @SerializedName("keyword") val keywordword : String,
    @SerializedName("description") val description : String
)

data class Brand (

    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("alias") val alias : String,
    @SerializedName("image") val image : String,
    @SerializedName("url") val url : String,
    @SerializedName("status") val status : Int,
    @SerializedName("sort") val sort : Int
)
data class Pivot (

    @SerializedName("product_id") val product_id : Int,
    @SerializedName("category_id") val category_id : Int
)