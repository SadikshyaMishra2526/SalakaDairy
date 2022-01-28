import com.eightpeak.salakafarm.views.home.products.productbyid.Description_Product
import com.eightpeak.salakafarm.views.home.products.productbyid.Descriptions
import com.eightpeak.salakafarm.views.home.products.productbyid.Promotion_price
import com.google.gson.annotations.SerializedName


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