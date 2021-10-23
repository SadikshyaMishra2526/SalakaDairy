package com.eightpeak.salakafarm.views.home.products

import com.eightpeak.salakafarm.views.home.products.productbyid.Attributes
import com.google.gson.annotations.SerializedName

data class ProductModel (
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
	@SerializedName("total") val total : Int)


data class Data (

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
	@SerializedName("images") val images : List<Images>,
	@SerializedName("descriptions") val descriptions : List<Descriptions>,
	@SerializedName("promotion_price") val promotion_price : Promotion_price,
	@SerializedName("attributes") val attributes : List<Attributes>
)
data class Images (

	@SerializedName("id") val id : Int,
	@SerializedName("image") val image : String,
	@SerializedName("product_id") val product_id : Int
)

data class Descriptions (

	@SerializedName("product_id") val product_id : Int,
	@SerializedName("lang") val lang : String,
	@SerializedName("name") val name : String,
	@SerializedName("keyword") val keywordword : String,
	@SerializedName("description") val description : String,
	@SerializedName("content") val content : String
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


data class Links (

	@SerializedName("url") val url : String,
	@SerializedName("label") val label : String,
	@SerializedName("active") val active : Boolean
)


