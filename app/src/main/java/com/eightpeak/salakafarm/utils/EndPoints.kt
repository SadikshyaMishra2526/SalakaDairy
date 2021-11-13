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
        const val USER_DETAILS = "api/auth/user"

        //categories
        const val CATEGORIES_LIST = "api/categories"
        const val CATEGORIES_VIA_ID = "api/categories/{id}"

        //        products
        const val BRAND_LIST = "api/brands"
        const val BRAND_VIA_ID = "api/products/{id}"

        //        orders
        const val ORDER_LIST = "api/auth/orders"
        const val ORDER_VIA_ID = "api/auth/orders/{id}"


        const val CANCEL_ORDER = "api/member/cancel_order/{id}"
        const val CREATE_ORDER = "api/member/create_order"


        //      products
        const val PRODUCT_LIST = "api/products"
        const val PRODUCT_VIA_ID = "api/products/{id}"


        const val SEARCH_RESPONSE = "api/search/"
        const val ADD_TO_WISHLIST = "api/add_to_wishlist"
        const val ADD_TO_CART = "api/add_to_cart"
        const val GET_CART_DETAILS = "api/get_cart"

        const val GET_WISHLIST_DETAILS = "api/get_wishlist"
        const val DELETE_WISHLIST_ITEM = "api/delete_wishlist"
        const val COMPARE_LIST_DETAILS = "api/get_product_list_for_compare"

//        subscription
        const val GET_SUB_PACKAGE = "api/get_sub_packages"
        const val ADD_SUBSCRIPTION = "api/subscribed"
        const val GET_SUB_ITEM = "api/get_sub_item"
        const val GET_BRANCHES = "api/get_branch"

    }
}