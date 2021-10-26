import com.google.gson.annotations.SerializedName

data class UserProfileModel (

	@SerializedName("id") val id : Int,
	@SerializedName("first_name") val first_name : String,
	@SerializedName("last_name") val last_name : String,
	@SerializedName("first_name_kana") val first_name_kana : String,
	@SerializedName("last_name_kana") val last_name_kana : String,
	@SerializedName("email") val email : String,
	@SerializedName("sex") val sex : String,
	@SerializedName("birthday") val birthday : String,
	@SerializedName("address_id") val address_id : Int,
	@SerializedName("postcode") val postcode : String,
	@SerializedName("address1") val address1 : String,
	@SerializedName("address2") val address2 : String,
	@SerializedName("address3") val address3 : String,
	@SerializedName("company") val company : String,
	@SerializedName("country") val country : String,
	@SerializedName("phone") val phone : Int,
	@SerializedName("store_id") val store_id : Int,
	@SerializedName("status") val status : Int,
	@SerializedName("group") val group : Int,
	@SerializedName("email_verified_at") val email_verified_at : String,
	@SerializedName("created_at") val created_at : String,
	@SerializedName("updated_at") val updated_at : String,
	@SerializedName("provider") val provider : String,
	@SerializedName("provider_id") val provider_id : String,
	@SerializedName("name") val name : String
)