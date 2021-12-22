package com.eightpeak.salakafarm.views.order.orderview.viewordercheckoutdetails

import com.google.gson.annotations.SerializedName

class CheckOutModel (
    @SerializedName("order_details") val order_details : Order_details)

data class Order_details (

    @SerializedName("title") val title : String,
    @SerializedName("description") val description : String,
    @SerializedName("keyword") val keywordword : String,
    @SerializedName("cartItem") val cartItem : List<CartItem>,
    @SerializedName("totalMethod") val totalMethod : List<String>,
    @SerializedName("addressList") val addressList : List<AddressList>,
    @SerializedName("dataTotal") val dataTotal : List<DataTotal>,
    @SerializedName("shippingAddress") val shippingAddress : ShippingAddress
)
data class CartItem (

    @SerializedName("id") val id : Int,
    @SerializedName("product_id") val product_id : Int,
    @SerializedName("customer_id") val customer_id : Int,
    @SerializedName("qty") val qty : Int,
    @SerializedName("options") val options : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("original_price") val original_price : Int,
    @SerializedName("final_price") val final_price : Int,
    @SerializedName("total_price") val total_price : Int,
    @SerializedName("products_with_description") val products_with_description : Products_with_description
)

data class AddressList (

    @SerializedName("id") val id : Int,
    @SerializedName("customer_id") val customer_id : Int,
    @SerializedName("first_name") val first_name : String,
    @SerializedName("last_name") val last_name : String,
    @SerializedName("first_name_kana") val first_name_kana : String,
    @SerializedName("last_name_kana") val last_name_kana : String,
    @SerializedName("postcode") val postcode : String,
    @SerializedName("address1") val address1 : String,
    @SerializedName("address2") val address2 : String,
    @SerializedName("address3") val address3 : String,
    @SerializedName("country") val country : String,
    @SerializedName("phone") val phone : String
)


data class DataTotal (

    @SerializedName("title") val title : String,
    @SerializedName("code") val code : String,
    @SerializedName("value") val value : Int,
    @SerializedName("text") val text : String,
    @SerializedName("sort") val sort : Int
)
data class ShippingAddress (

    @SerializedName("first_name") val first_name : String,
    @SerializedName("last_name") val last_name : String,
    @SerializedName("first_name_kana") val first_name_kana : String,
    @SerializedName("last_name_kana") val last_name_kana : String,
    @SerializedName("email") val email : String,
    @SerializedName("address1") val address1 : String,
    @SerializedName("address2") val address2 : String,
    @SerializedName("address3") val address3 : String,
    @SerializedName("postcode") val postcode : String,
    @SerializedName("company") val company : String,
    @SerializedName("country") val country : String,
    @SerializedName("phone") val phone : String,
    @SerializedName("comment") val comment : String
)

data class Products_with_description (

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
    @SerializedName("descriptions") val descriptions : List<Descriptions>
)

data class Descriptions (

    @SerializedName("product_id") val product_id : Int,
    @SerializedName("lang") val lang : String,
    @SerializedName("name") val name : String,
    @SerializedName("keyword") val keywordword : String,
    @SerializedName("description") val description : String,
    @SerializedName("content") val content : String
)