package com.eightpeak.salakafarm.serverconfig

import retrofit2.http.Field


object RequestBodies {

    data class LoginBody(
        val email: String,
        val password: String,
        val remember_me: String
    )

    data class RegisterBody(
        val first_name: String,
        val last_name: String,
        val email: String,
        val password: String,
        val address1: String,
        val address2: String,
        val country: String,
        val phone: String
    )

    data class TokenBody(
        val grant_type: String,
        val refresh_token: String,
        val client_id: String,
        val client_secret: String,
        val scope: String
    )

    data class CategoriesByIDBody(
        val id: String,

    )

    data class AddToCart(
        val product_id: String,
        val qty: String,
        val options: String,
    )

    data class WishList(
        val product_id: String
    )


      data class AddSubscription(
        val sub_item_id: String,
        val sub_package_id: String,
        val delivery_peroid: String,
        val starting_date: String,
        val branch_id: String,
    )


    data class UserProfile(
        val first_name: String,
        val last_name: String,
        val sex: String,
        val starting_date: String
    )
    data class UpdateAddressList(
        val first_name: String,
        val last_name: String,
        val postcode: String,
        val address1: String,
        val address2: String,
        val id: String
    )

    data class UpdatePassword(
        val old_password: String,
        val new_password: String,
    )

}

