package com.eightpeak.salakafarm.serverconfig

import UserProfileModel
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.serverconfig.network.AccessToken
import com.eightpeak.salakafarm.subscription.attributes.BranchModel
import com.eightpeak.salakafarm.subscription.attributes.SubscriptionItemModel
import com.eightpeak.salakafarm.subscription.attributes.SubscriptionPackageModel
import com.eightpeak.salakafarm.subscription.attributes.SubscriptionResponse
import com.eightpeak.salakafarm.utils.EndPoints.Companion.COMPARE_LIST_DETAILS
import com.eightpeak.salakafarm.utils.EndPoints.Companion.DELETE_WISHLIST_ITEM
import com.eightpeak.salakafarm.utils.EndPoints.Companion.GET_CART_DETAILS
import com.eightpeak.salakafarm.utils.EndPoints.Companion.GET_WISHLIST_DETAILS
import com.eightpeak.salakafarm.utils.EndPoints.Companion.REFRESH_TOKEN
import com.eightpeak.salakafarm.utils.EndPoints.Companion.SEARCH_RESPONSE
import com.eightpeak.salakafarm.utils.EndPoints.Companion.USER_DETAILS
import com.eightpeak.salakafarm.views.comparelist.CompareResponse
import com.eightpeak.salakafarm.views.home.categories.CategoriesModel
import com.eightpeak.salakafarm.views.home.categories.categoriesbyid.CategoriesByIdModel
import com.eightpeak.salakafarm.views.home.products.ProductModel
import com.eightpeak.salakafarm.views.home.products.ServerResponse
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdModel
import com.eightpeak.salakafarm.views.home.slider.SliderModel
import com.eightpeak.salakafarm.views.addtocart.addtocartfragment.CartResponse
import com.eightpeak.salakafarm.views.login.LoginResponse
import com.eightpeak.salakafarm.views.register.RegisterResponse
import com.eightpeak.salakafarm.views.search.SearchModel
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
    suspend fun getCartList(): Response<List<CartResponse>>


    @GET(GET_WISHLIST_DETAILS)
    suspend fun getWishList(): Response<List<CartResponse>>



    @GET(COMPARE_LIST_DETAILS)
    suspend fun getCompareProduct(
        @Query("product_ids") product_ids: String
    ): Response<CompareResponse>


    @POST(DELETE_WISHLIST_ITEM)
    suspend fun deleteWishlistItem(
        @Query("id") productId: String
    ): Response<ServerResponse>


    //    for subscription

    @POST(EndPoints.ADD_SUBSCRIPTION)
    suspend fun addSubscription(@Body body: RequestBodies.AddSubscription): Response<SubscriptionResponse>

    @FormUrlEncoded
    @POST(EndPoints.GET_SUB_PACKAGE)
    suspend fun getSubscriptionPackage(@Field("sub_item_id")sub_item_id:Int): Response<SubscriptionPackageModel>

    @GET(EndPoints.GET_BRANCHES)
    suspend fun getBranches(): Response<BranchModel>

    @GET(EndPoints.GET_SUB_ITEM)
    suspend fun getSubscriptionItem(): Response<SubscriptionItemModel>


}