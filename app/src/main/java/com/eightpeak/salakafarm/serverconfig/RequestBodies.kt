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
}
