package com.eightpeak.salakafarm.utils

class EndPoints {
    companion object {

        //customer apis
        const val BASE_URL = "https://salakafarm.com/public/"
        const val REGISTER = "api/auth/create"
        const val LOGIN = "api/auth/login"
        const val CUSTOMER_INFORMATION = "api/auth/user"
        const val LOGOUT = "api/logout_user"
        const val FORGOT_PASSWORD = "api/auth/forgot_password_reset_link"
        const val GOOGLE_LOGIN = "api/auth/login_customer_through_google"
        const val SLIDER = "api/banners"

        //customer token
        const val REFRESH_TOKEN = "/oauth/token"
        const val USER_DETAILS = "api/auth/user"

        //categories
        const val CATEGORIES_LIST = "api/categories"
        const val CATEGORIES_VIA_ID = "api/categories/{id}"

        //      products
        const val PRODUCT_LIST = "api/products"
        const val PRODUCT_VIA_ID = "api/products/{id}"


        const val SEARCH_RESPONSE = "api/search/"
        const val ADD_TO_WISHLIST = "api/add_to_wishlist"
        const val ADD_TO_CART = "api/add_to_cart"
        const val GET_CART_DETAILS = "api/get_cart"
        const val UPDATE_CART = "api/update_to_cart"

        const val GET_WISHLIST_DETAILS = "api/get_wishlist"
        const val DELETE_WISHLIST_ITEM = "api/delete_wishlist"
        const val COMPARE_LIST_DETAILS = "api/get_product_list_for_compare"
        const val DELETE_WISHLIST = "api/clear_wishlist"


//        cart

        const val DELETE_CART_ITEM = "api/delete_cart"
        const val DELETE_CART = "api/clear_cart"

        //        subscription
        const val GET_SUB_PACKAGE = "api/get_sub_packages"
        const val ADD_SUBSCRIPTION = "api/subscribed"
        const val GET_SUB_ITEM = "api/get_sub_item"
        const val GET_BRANCHES = "api/get_branch"
        const val GET_SUBSCRIPTION_DISPLAY = "api/get_customer_subscription"
        const val CANCEL_SUBSCRIPTION = "api/cancel_subscription"
        const val POST_RATE = "api/rate"
        const val GET_RATE = "api/get_product_rating"
        //address
        const val GET_ADDRESS = "api/get_address_list"
        const val SUB_HISTORY = "api/get_customer_subscription_detail_by_dates"


        //        order
        const val GET_CHECKOUT_DETAILS = " api/get_checkout_details"
        const val GET_ORDER_LIST = " api/get_order_list"
        const val GET_ORDER_DETAILS = " api/get_order_details"

        const val UPDATE_ADDRESS_LIST = " api/update_address_list"

        const val UPDATE_USER_INFO = " api/update_user_info"

       const val UPDATE_USER_PASSWORD = " api/update_user_password"
        const val GET_ADDRESS_LIST = " api/get_address_list"



        const val GET_PAGE_DETAILS = " api/get_pages/{id}"
        const val GET_RANDOM_PRODUCTS = "api/random_products"

        const val UPDATE_CART_PRODUCTS = "api/update_to_cart"
        const val ADD_COMPLAIN = "api/store_complain"
        const val ADD_ORDER = "api/create_order"

        const val DELETE_ADDRESS = "api/delete_address"

        const val ADD_ADDRESS = "api/add_new_address"
        const val EDIT_ADDRESS = "api/update_address_list"
        const val  GET_EMPLOYEE_LATLNG= "api/get_employee_latlng"
        const val  ADD_ALTERATION= "api/sub_alter"


        const val POP_UP = "api/get_popup"
const val GET_GALLERY = "api/get_gallery"

        const val PAYMENT_EVIDENCE = "api/post_payment_evidence"


        const val VERIFY_OPT = "api/verify_otp"
        const val GENERATE_NEW_OTP = "api/generate_new_opt"
        const val CONTACT_US = "api/post_contact_us"

        const val CHANNEL_ID="channel_id"
        const val CHANNEL_NAME="channel name"
        const val CHANNEL_desc="channel desc"
    }
}