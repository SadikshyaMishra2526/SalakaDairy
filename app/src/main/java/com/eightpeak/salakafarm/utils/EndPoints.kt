package com.eightpeak.salakafarm.utils

class EndPoints {
    companion object {
        //customer apis
        const val BASE_URL = "https://salakafarm.com/public/"
        const val REGISTER = "api/auth/create"
        const val LOGIN = "api/auth/login"
        const val CUSTOMER_INFORMATION = "api/auth/user"
        const val LOGOUT = "api/auth/logout"

        const val SLIDER = "api/banners"

        //customer token
        const val REFRESH_TOKEN = "/oauth/token"

        //categories
        const val CATEGORIES_LIST = "/api/categories"
        const val CATEGORIES_VIA_ID = "api/categories/{id}"

        //        products
        const val BRAND_LIST = "api/brands"
        const val BRAND_VIA_ID = "api/products/{id}"

        //        orders
        const val ORDER_LIST = "api/auth/orders"
        const val ORDER_VIA_ID = "api/auth/orders/{id}"   //        products
        const val CANCEL_ORDER = "api/member/cancel_order/{id}"
        const val CREATE_ORDER = "api/member/create_order"   //


        //      products
        const val PRODUCT_LIST = "api/products"
        const val PRODUCT_VIA_ID = "api/products/{id}"


    }
}