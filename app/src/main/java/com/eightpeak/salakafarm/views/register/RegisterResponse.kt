package com.eightpeak.salakafarm.views.register

import com.google.gson.annotations.SerializedName

data class RegisterResponse (

	@SerializedName("first_name") val first_name : String,
	@SerializedName("email") val email : String,
	@SerializedName("last_name") val last_name : String,
	@SerializedName("address1") val address1 : String,
	@SerializedName("address2") val address2 : String,
	@SerializedName("phone") val phone : Int,
	@SerializedName("country") val country : String,
	@SerializedName("updated_at") val updated_at : String,
	@SerializedName("created_at") val created_at : String,
	@SerializedName("id") val id : Int,
	@SerializedName("address_id") val address_id : Int,
	@SerializedName("name") val name : String,

	@SerializedName("error") val error : String,
	@SerializedName("msg") val msg : String,
	@SerializedName("detail") val detail : String

) {
	override fun toString(): String {
		return "RegisterResponse(first_name='$first_name', email='$email', last_name='$last_name', address1='$address1', address2='$address2', phone=$phone, country='$country', updated_at='$updated_at', created_at='$created_at', id=$id, address_id=$address_id, name='$name', error='$error', msg='$msg', detail='$detail')"
	}
}