package com.eightpeak.salakafarm.views.home.products.productbyid

import com.google.gson.annotations.SerializedName

class ProductByIdModel(
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
    @SerializedName("tax_id") val tax_id : Int,
    @SerializedName("status") val status : Int,
    @SerializedName("sort") val sort : Int,
    @SerializedName("view") val view : Int,
    @SerializedName("alias") val alias : String,
    @SerializedName("date_lastview") val date_lastview : String,
    @SerializedName("date_available") val date_available : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("average_rating") val average_rating : Double,
    @SerializedName("no_of_rating") val no_of_rating : Int,
    @SerializedName("productRelation") val productRelation : List<ProductRelation>,
    @SerializedName("images") val images : List<Images>,
    @SerializedName("descriptions") val descriptions : List<Descriptions>,
    @SerializedName("categories_description") val categories_description : List<Categories_description>,
    @SerializedName("promotion_price") val promotion_price : Promotion_price,
    @SerializedName("brand") val brand : String,
    @SerializedName("attributes") val attributes : List<String>,
    @SerializedName("categories") val categories : List<Categories>

) {
    override fun toString(): String {
        return "ProductByIdModel(id=$id, sku='$sku', upc='$upc', ean='$ean', jan='$jan', isbn='$isbn', mpn='$mpn', image='$image', brand_id=$brand_id, supplier_id=$supplier_id, price=$price, cost=$cost, stock=$stock, sold=$sold, minimum=$minimum, weight_class='$weight_class', weight=$weight, length_class='$length_class', length=$length, width=$width, height=$height, kind=$kind, property='$property', tax_id=$tax_id, status=$status, sort=$sort, view=$view, alias='$alias', date_lastview='$date_lastview', date_available='$date_available', created_at='$created_at', updated_at='$updated_at', average_rating=$average_rating, no_of_rating=$no_of_rating, productRelation=$productRelation, images=$images, descriptions=$descriptions, categories_description=$categories_description, promotion_price=$promotion_price, brand='$brand', attributes=$attributes, categories=$categories)"
    }
}

data class ProductRelation (

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
    @SerializedName("tax_id") val tax_id : Int,
    @SerializedName("status") val status : Int,
    @SerializedName("sort") val sort : Int,
    @SerializedName("view") val view : Int,
    @SerializedName("alias") val alias : String,
    @SerializedName("date_lastview") val date_lastview : String,
    @SerializedName("date_available") val date_available : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("average_rating") val average_rating : Double,
    @SerializedName("no_of_rating") val no_of_rating : Int,
    @SerializedName("name") val name : String,
    @SerializedName("keyword") val keywordword : String,
    @SerializedName("description") val description : String,
    @SerializedName("promotion_price") val promotion_price : Promotion_price,
    @SerializedName("stores") val stores : List<String>,
    @SerializedName("descriptions") val descriptions : List<Descriptions>
)


data class Categories (

    @SerializedName("id") val id : Int,
    @SerializedName("image") val image : String,
    @SerializedName("alias") val alias : String,
    @SerializedName("parent") val parent : Int,
    @SerializedName("top") val top : Int,
    @SerializedName("status") val status : Int,
    @SerializedName("sort") val sort : Int,
    @SerializedName("pivot") val pivot : Pivot
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


data class Pivot (

    @SerializedName("product_id") val product_id : Int,
    @SerializedName("category_id") val category_id : Int
)
data class Descriptions (

    @SerializedName("category_id") val category_id : Int,
    @SerializedName("lang") val lang : String,
    @SerializedName("title") val title : String,
    @SerializedName("keyword") val keywordword : String,
    @SerializedName("description") val description : String
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
data class Images (

    @SerializedName("id") val id : Int,
    @SerializedName("image") val image : String,
    @SerializedName("product_id") val product_id : Int
)