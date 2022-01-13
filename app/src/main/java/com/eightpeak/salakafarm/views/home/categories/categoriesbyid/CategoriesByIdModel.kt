package com.eightpeak.salakafarm.views.home.categories.categoriesbyid

import com.eightpeak.salakafarm.views.home.products.productbyid.Promotion_price
import com.google.gson.annotations.SerializedName

data class CategoriesByIdModel(

    @SerializedName("id") val id: Int?=null,
    @SerializedName("image") val image: String?=null,
    @SerializedName("alias") val alias: String?=null,
    @SerializedName("parent") val parent: Int?=null,
    @SerializedName("top") val top: Int?=null,
    @SerializedName("status") val status: Int?=null,
    @SerializedName("sort") val sort: Int?=null,
    @SerializedName("descriptions") val descriptions: List<Descriptions>?=null,
    @SerializedName("products_with_description") val products_with_description: List<Products_with_description>
)

data class Products_with_description(

    @SerializedName("id") val id: Int?=null,
    @SerializedName("sku") val sku: String?=null,
    @SerializedName("upc") val upc: String?=null,
    @SerializedName("ean") val ean: String?=null,
    @SerializedName("jan") val jan: String?=null,
    @SerializedName("isbn") val isbn: String?=null,
    @SerializedName("mpn") val mpn: String?=null,
    @SerializedName("image") val image: String?=null,
    @SerializedName("brand_id") val brand_id: Int?=null,
    @SerializedName("supplier_id") val supplier_id: Int?=null,
    @SerializedName("price") val price: Int?=null,
    @SerializedName("cost") val cost: Int?=null,
    @SerializedName("stock") val stock: Int?=null,
    @SerializedName("sold") val sold: Int?=null,
    @SerializedName("minimum") val minimum: Int?=null,
    @SerializedName("weight_class") val weight_class: String?=null,
    @SerializedName("weight") val weight: Int?=null,
    @SerializedName("length_class") val length_class: String?=null,
    @SerializedName("length") val length: Int?=null,
    @SerializedName("width") val width: Int?=null,
    @SerializedName("height") val height: Int?=null,
    @SerializedName("kind") val kind: Int?=null,
    @SerializedName("property") val property: String?=null,
    @SerializedName("tax_id") val tax_id: String?=null,
    @SerializedName("status") val status: Int?=null,
    @SerializedName("sort") val sort: Int?=null,
    @SerializedName("view") val view: Int?=null,
    @SerializedName("alias") val alias: String?=null,
    @SerializedName("date_lastview") val date_lastview: String?=null,
    @SerializedName("date_available") val date_available: String?=null,
    @SerializedName("created_at") val created_at: String?=null,
    @SerializedName("updated_at") val updated_at: String?=null,
    @SerializedName("pivot") val pivot: Pivot?=null,
    @SerializedName("descriptions") val descriptions: List<Descriptions>?=null,
    @SerializedName("average_rating") val average_rating: Float?=null,
    @SerializedName("no_of_rating") val no_of_rating: String?=null,
    @SerializedName("promotion_price") val promotion_price: Promotion_price
)

data class Promotion_price(
    @SerializedName("product_id") val product_id: Int?=null,
    @SerializedName("price_promotion") val price_promotion: Int?=null,
    @SerializedName("date_start") val date_start: String?=null,
    @SerializedName("date_end") val date_end: String?=null,
    @SerializedName("status_promotion") val status_promotion: Int?=null,
    @SerializedName("created_at") val created_at: String?=null,
    @SerializedName("updated_at") val updated_at: String
)

data class Descriptions(
    @SerializedName("product_id") val product_id: Int?=null,
    @SerializedName("lang") val lang: String?=null,
    @SerializedName("name") val name: String?=null,
    @SerializedName("keyword") val keywordword: String?=null,
    @SerializedName("description") val description: String?=null,
    @SerializedName("content") val content: String
)

data class Pivot(

    @SerializedName("category_id") val category_id: Int?=null,
    @SerializedName("product_id") val product_id: Int
)