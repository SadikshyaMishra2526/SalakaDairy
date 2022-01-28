package com.eightpeak.salakafarm.serverconfig

import UserProfileModel
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.serverconfig.network.AccessToken
import com.eightpeak.salakafarm.subscription.attributes.BranchModel
import com.eightpeak.salakafarm.subscription.attributes.SubscriptionItemModel
import com.eightpeak.salakafarm.subscription.attributes.SubscriptionPackageModel
import com.eightpeak.salakafarm.subscription.attributes.SubscriptionResponse
import com.eightpeak.salakafarm.subscription.displaysubscription.EmployeeTrackDetails
import com.eightpeak.salakafarm.subscription.displaysubscription.models.DisplaySubscriptionModel
import com.eightpeak.salakafarm.subscription.displaysubscription.models.SubscriptionHistoryModel
import com.eightpeak.salakafarm.utils.EndPoints.Companion.COMPARE_LIST_DETAILS
import com.eightpeak.salakafarm.utils.EndPoints.Companion.DELETE_CART
import com.eightpeak.salakafarm.utils.EndPoints.Companion.DELETE_CART_ITEM
import com.eightpeak.salakafarm.utils.EndPoints.Companion.DELETE_WISHLIST
import com.eightpeak.salakafarm.utils.EndPoints.Companion.DELETE_WISHLIST_ITEM
import com.eightpeak.salakafarm.utils.EndPoints.Companion.GET_CART_DETAILS
import com.eightpeak.salakafarm.utils.EndPoints.Companion.GET_WISHLIST_DETAILS
import com.eightpeak.salakafarm.utils.EndPoints.Companion.REFRESH_TOKEN
import com.eightpeak.salakafarm.utils.EndPoints.Companion.SEARCH_RESPONSE
import com.eightpeak.salakafarm.utils.EndPoints.Companion.UPDATE_ADDRESS_LIST
import com.eightpeak.salakafarm.utils.EndPoints.Companion.UPDATE_USER_INFO
import com.eightpeak.salakafarm.utils.EndPoints.Companion.UPDATE_USER_PASSWORD
import com.eightpeak.salakafarm.utils.EndPoints.Companion.USER_DETAILS
import com.eightpeak.salakafarm.views.addresslist.AddressListModel
import com.eightpeak.salakafarm.views.comparelist.CompareResponse
import com.eightpeak.salakafarm.views.home.categories.CategoriesModel
import com.eightpeak.salakafarm.views.home.categories.categoriesbyid.CategoriesByIdModel
import com.eightpeak.salakafarm.views.home.slider.SliderModel
import com.eightpeak.salakafarm.views.addtocart.addtocartfragment.CartResponse
import com.eightpeak.salakafarm.views.gallery.GalleryListModel
import com.eightpeak.salakafarm.views.home.products.*
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdModel
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductRatingModel
import com.eightpeak.salakafarm.views.login.LoginResponse
import com.eightpeak.salakafarm.views.order.orderview.orderhistory.OrderHistoryDetailsModel
import com.eightpeak.salakafarm.views.order.orderview.orderhistory.OrderHistoryModel
import com.eightpeak.salakafarm.views.order.orderview.viewordercheckoutdetails.CheckOutModel
import com.eightpeak.salakafarm.views.pages.PageDetailsModel
import com.eightpeak.salakafarm.views.pages.videos.YoutubeVideoModel
import com.eightpeak.salakafarm.views.popup.PopUpModel
import com.eightpeak.salakafarm.views.register.RegisterResponse
import com.eightpeak.salakafarm.views.search.SearchModel
import com.eightpeak.salakafarm.views.wishlist.WishListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {

    //  get shop info
    @GET(EndPoints.SLIDER)
    suspend fun getSlider(): Response<List<SliderModel>>


    @GET(EndPoints.CATEGORIES_LIST)
    suspend fun getCategoriesList(): Response<CategoriesModel>

    @GET(EndPoints.CATEGORIES_VIA_ID)
    suspend fun getCategoriesById(@Path("id") id: String): Response<CategoriesByIdModel>


    @GET(EndPoints.PRODUCT_LIST)
    suspend fun getProductList(): Response<ProductModel>

    @GET(EndPoints.PRODUCT_VIA_ID)
    suspend fun getProductById(@Path("id") id: String): Response<ProductByIdModel>


    //user details
    @POST(EndPoints.LOGIN)
    suspend fun loginCustomer(@Body body: RequestBodies.LoginBody): Response<LoginResponse>

    @POST(EndPoints.REGISTER)
    suspend fun registerCustomer(@Body body: RequestBodies.RegisterBody): Response<RegisterResponse>

    @POST(REFRESH_TOKEN)
    suspend fun refresh(@Body body: RequestBodies.TokenBody): Response<AccessToken>

    @GET(USER_DETAILS)
    suspend fun userDetails(): Response<UserProfileModel>


    @GET(SEARCH_RESPONSE)
    suspend fun getSearchResponse(
        @Query("keyword") keyword: String,
        @Query("filter_sort") filter_sort: String
    ): Response<SearchModel>

    @FormUrlEncoded
    @POST(EndPoints.ADD_TO_WISHLIST)
    suspend fun addToWishList(@Field("product_id") product_id: String): Response<ServerResponse>

    @FormUrlEncoded
    @POST(EndPoints.ADD_TO_CART)
    suspend fun addToCart(
        @Field("product_id") product_id: String,
        @Field("qty") qty: String,
        @Field("options") options: String
    ): Response<ServerResponse>


    @GET(GET_CART_DETAILS)
    suspend fun getCartList(): Response<CartResponse>


    @GET(GET_WISHLIST_DETAILS)
    suspend fun getWishList(): Response<WishListResponse>


    @GET(COMPARE_LIST_DETAILS)
    suspend fun getCompareProduct(
        @Query("product_ids") product_ids: String
    ): Response<CompareResponse>

    //for wishlist
    @POST(DELETE_WISHLIST_ITEM)
    suspend fun deleteWishlistItem(
        @Query("id") productId: String
    ): Response<ServerResponse>

    //for all items
    @POST(DELETE_WISHLIST)
    suspend fun deleteWishlist(
    ): Response<ServerResponse>

    //    for cart
    @POST(DELETE_CART_ITEM)
    suspend fun deleteCartItem(
        @Query("id") productId: String
    ): Response<ServerResponse>

    //for all cart
    @POST(DELETE_CART)
    suspend fun deleteCart(): Response<ServerResponse>


    //    for subscription

    @POST(EndPoints.ADD_SUBSCRIPTION)
    suspend fun addSubscription(@Body body: RequestBodies.AddSubscription): Response<SubscriptionResponse>

    @FormUrlEncoded
    @POST(EndPoints.GET_SUB_PACKAGE)
    suspend fun getSubscriptionPackage(@Field("branch_id") branch_id: Int): Response<SubscriptionPackageModel>

    @GET(EndPoints.GET_BRANCHES)
    suspend fun getBranches(): Response<BranchModel>

    @GET(EndPoints.GET_SUB_ITEM)
    suspend fun getSubscriptionItem(): Response<SubscriptionItemModel>

    @GET(EndPoints.GET_CHECKOUT_DETAILS)
    suspend fun getCheckoutDetails(): Response<CheckOutModel>


    @POST(UPDATE_ADDRESS_LIST)
    suspend fun updateAddressList(
        @Body body: RequestBodies.UpdateAddressList
    ): Response<ServerResponse>


    @POST(UPDATE_USER_PASSWORD)
    suspend fun updatePassword(@Body body: RequestBodies.UpdatePassword): Response<UpdatePasswordResponse>

    @GET(EndPoints.GET_ADDRESS)
    suspend fun getAddressList(): Response<AddressListModel>

    @GET(EndPoints.GET_ORDER_LIST)
    suspend fun getOrderHistoryList(): Response<OrderHistoryModel>

    @GET(EndPoints.GET_ORDER_DETAILS)
    suspend fun getOrderHistoryDetails(@Query("id") id: String): Response<OrderHistoryDetailsModel>


    @GET(EndPoints.GET_PAGE_DETAILS)
    suspend fun getPageDetails(@Path("id") id: String): Response<PageDetailsModel>


    @GET(EndPoints.GET_RANDOM_PRODUCTS)
    suspend fun getRandomList(): Response<List<Data>>


    @GET(EndPoints.POP_UP)
    suspend fun getPopUp(): Response<PopUpModel>


    @POST(EndPoints.UPDATE_CART_PRODUCTS)
    suspend fun updateCart(
        @Query("id") id: String,
        @Query("new_qty") new_qty: String
    )
            : Response<ServerResponse>


    @POST(EndPoints.ADD_COMPLAIN)
    suspend fun addComplain(@Body body: RequestBodies.AddComplain): Response<ServerResponse>


    @POST(EndPoints.ADD_ORDER)
    suspend fun addOrder(@Body body: RequestBodies.AddOrder): Response<OrderResponse>

    @POST(EndPoints.ADD_ADDRESS)
    suspend fun addNewAddress(@Body body: RequestBodies.AddAddress): Response<ServerResponse>


    @POST(EndPoints.EDIT_ADDRESS)
    suspend fun editAddress(@Body body: RequestBodies.EditAddress): Response<UserAddressEdit>


    @FormUrlEncoded
    @POST(EndPoints.DELETE_ADDRESS)
    suspend fun deleteAddress(@Field("id") addressId: String): Response<ServerResponse>


    @POST(EndPoints.GET_EMPLOYEE_LATLNG)
    suspend fun getEmployeeLatLng(@Body body: RequestBodies.EmpLatlng): Response<EmployeeTrackDetails>

    @POST(EndPoints.ADD_ALTERATION)
    suspend fun subscriptionAlteration(@Body body: RequestBodies.AddAlteration): Response<ServerResponse>


    @GET("search?key=AIzaSyBFfGX8c8zWp6O8spofADuDlJP_96BOc2M&channelId=UCIx6fxkutSyo-I6KxsSV8zg&part=snippet,id&order=date&maxResults=20")
    fun getVideoList(): Call<YoutubeVideoModel?>?


    @GET(EndPoints.GET_SUBSCRIPTION_DISPLAY)
    suspend fun getCustomerSubscription(): Response<DisplaySubscriptionModel>


    @POST(EndPoints.POST_RATE)
    suspend fun postRate(): Response<DisplaySubscriptionModel>

    @FormUrlEncoded
    @POST(EndPoints.CANCEL_SUBSCRIPTION)
    suspend fun cancelSubscription(@Field("subscription_id") subscriptionId: String): Response<ServerResponse>


    @Multipart
    @POST(EndPoints.PAYMENT_EVIDENCE)
    suspend fun paymentEvidence(
        @Part("mode") email: RequestBody,
        @Part("subscription_id") subscriptionId: RequestBody,
        @Part screenshot: MultipartBody.Part
    ): Response<ServerResponse>


    @Multipart
    @POST(UPDATE_USER_INFO)
    suspend fun getUserProfile(
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("sex") sex: RequestBody,
        @Part("birthday") birthday: RequestBody,
        @Part avatar: MultipartBody.Part
    ): Response<UserProfileResponse>


    @FormUrlEncoded
    @POST(EndPoints.GET_RATE)
    suspend fun getRate(@Field("product_id") addressId: String): Response<ProductRatingModel>


    @POST(EndPoints.SUB_HISTORY)
    suspend fun getSubscriptionHistory(@Body body: RequestBodies.SubHistoryList): Response<SubscriptionHistoryModel>


    @FormUrlEncoded
    @POST(EndPoints.GENERATE_NEW_OTP)
    suspend fun generateNewOTP(@Field("phone") phone: String): Response<ServerResponse>


    @FormUrlEncoded
    @POST(EndPoints.VERIFY_OPT)
    suspend fun verifyOTP(
        @Field("phone") phone: String,
        @Field("otp") otp: String
    ): Response<ServerResponse>


    @GET(EndPoints.GET_GALLERY)
    suspend fun getGallery(): Response<GalleryListModel>


    @POST(EndPoints.CONTACT_US)
    suspend fun contactUs(@Body body: RequestBodies.AddContactUs): Response<ServerResponse>


    @POST(EndPoints.LOGOUT)
    suspend fun logout(): Response<ServerResponse>

    @FormUrlEncoded
    @POST(EndPoints.FORGOT_PASSWORD)
    suspend fun forgotPassword(@Field("email") email: String): Response<ServerResponse>

    //    @POST(EndPoints.GOOGLE_LOGIN)
//    suspend fun googleLogin(@Body body:RequestBodies.GoogleLogin): Response<GoogleLoginResponse>
    @FormUrlEncoded
    @POST(EndPoints.GOOGLE_LOGIN)
    suspend fun googleLogin(
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("email") email: String,
        @Field("fcm_token") fcm_token: String,
        @Field("provider_id") provider_id: String,
        @Field("avatar") avatar: String,
        @Field("phone") phone: String): Response<GoogleLoginResponse>

}