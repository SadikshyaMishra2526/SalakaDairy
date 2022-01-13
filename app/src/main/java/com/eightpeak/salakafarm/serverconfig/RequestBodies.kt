package com.eightpeak.salakafarm.serverconfig

import retrofit2.http.Field


object RequestBodies {

    data class LoginBody(
        val email: String,
        val password: String,
        val remember_me: String,
        val fcm_token: String
    )

    data class RegisterBody(
        val first_name: String,
        val last_name: String,
        val email: String,
        val password: String,
//        val address1: String,
//        val address2: String,
        val country: String,
        val phone: String
    ) {
        override fun toString(): String {
            return "RegisterBody(first_name='$first_name', last_name='$last_name', email='$email', password='$password', country='$country', phone='$phone')"
        }
    }

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
        val branch_id: String,
        val address_id: String,
        val subscribed_total_amount: String,
        val subscribed_discount: String,
        val subscribed_price: String,
        val unit_per_day: String,
        val starting_date: String,
        val delivery_peroid: String,
        val sub_package_id: String,
        val total_quantity: String,
        val mode: String,
        val refId: String,
        val oid: String
    )


    data class UserProfile(
        val first_name: String,
        val last_name: String,
        val sex: String,
        val birthday: String
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

    data class AddComplain(
        val title: String,
        val description: String,
    )


    data class AddOrder(
        val oid: String,
        val refId: String,
        val amt: String,
        val shipping: String,
        val address_id: String,
    )

    data class EmpLatlng(
        val id: String,
        val type: String
    )

    data class AddAlteration(
        val subscription_id: String,
        val alter_status: String,
        val qty: String,
        val alter_for: String,
    )

    data class AddAddress(
        val address1: String,
        val address2: String,
        val address3: String,
        val phone: String,
        val lat: String,
        val lng: String
    )

    data class SubHistoryList(
        val from: String,
        val to: String
    )
}

