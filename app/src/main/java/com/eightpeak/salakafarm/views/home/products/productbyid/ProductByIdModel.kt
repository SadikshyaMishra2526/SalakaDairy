package com.eightpeak.salakafarm.views.home.products.productbyid

import com.google.gson.annotations.SerializedName

class ProductByIdModel
    (
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
    @SerializedName("productRelation") val productRelation: List<ProductRelation>?=null,
    @SerializedName("images") val images: List<Images>?=null,
    @SerializedName("descriptions") val descriptions: List<Descriptions>?=null,
    @SerializedName("promotion_price") val promotion_price: Promotion_price?=null,
    @SerializedName("attributes") val attributes: List<Attributes>?=null,
    @SerializedName("categories") val categories: List<Categories>?=null,
    @SerializedName("average_rating") val average_rating: Float?=null,
    @SerializedName("no_of_rating") val no_of_rating: String
)

data class Attributes(

    @SerializedName("id") val id: Int?=null,
    @SerializedName("name") val name: String?=null,
    @SerializedName("attribute_group_id") val attribute_group_id: Int?=null,
    @SerializedName("product_id") val product_id: Int?=null,
    @SerializedName("add_price") val add_price: Int?=null,
    @SerializedName("sort") val sort: Int?=null,
    @SerializedName("status") val status: Int
)

data class Images(
    @SerializedName("id") val id: Int?=null,
    @SerializedName("image") val image: String?=null,
    @SerializedName("product_id") val product_id: Int
)

data class Promotion_price(
    @SerializedName("product_id") val product_id: Int?=null,
    @SerializedName("price_promotion") val price_promotion: String?=null,
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

    @SerializedName("product_id") val product_id: Int?=null,
    @SerializedName("category_id") val category_id: Int
)

data class Stores(

    @SerializedName("id") val id: Int?=null,
    @SerializedName("logo") val logo: String?=null,
    @SerializedName("icon") val icon: String?=null,
    @SerializedName("phone") val phone: Int?=null,
    @SerializedName("long_phone") val long_phone: String?=null,
    @SerializedName("email") val email: String?=null,
    @SerializedName("time_active") val time_active: String?=null,
    @SerializedName("address") val address: String?=null,
    @SerializedName("office") val office: String?=null,
    @SerializedName("warehouse") val warehouse: String?=null,
    @SerializedName("template") val template: String?=null,
    @SerializedName("domain") val domain: String?=null,
    @SerializedName("partner") val partner: Int?=null,
    @SerializedName("code") val code: String?=null,
    @SerializedName("language") val language: String?=null,
    @SerializedName("timezone") val timezone: String?=null,
    @SerializedName("currency") val currency: String?=null,
    @SerializedName("status") val status: Int?=null,
    @SerializedName("active") val active: Int?=null,
    @SerializedName("pivot") val pivot: Pivot
)

data class ProductRelation(

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
    @SerializedName("name") val name: String?=null,
    @SerializedName("keyword") val keywordword: String?=null,
    @SerializedName("description") val description: String?=null,
    @SerializedName("promotion_price") val promotion_price: Promotion_price?=null,
    @SerializedName("stores") val stores: List<Stores>?=null,
    @SerializedName("descriptions") val descriptions: List<Descriptions>?=null,

    @SerializedName("average_rating") val average_rating: Float?=null,
    @SerializedName("no_of_rating") val no_of_rating: String
)

data class Categories(

    @SerializedName("id") val id: Int?=null,
    @SerializedName("image") val image: String?=null,
    @SerializedName("alias") val alias: String?=null,
    @SerializedName("parent") val parent: Int?=null,
    @SerializedName("top") val top: Int?=null,
    @SerializedName("status") val status: Int?=null,
    @SerializedName("sort") val sort: Int?=null,
    @SerializedName("pivot") val pivot: Pivot
)