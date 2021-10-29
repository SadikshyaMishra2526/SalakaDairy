package com.eightpeak.salakafarm.serverconfig

import UserProfileModel
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.serverconfig.network.AccessToken
import com.eightpeak.salakafarm.utils.EndPoints.Companion.REFRESH_TOKEN
import com.eightpeak.salakafarm.utils.EndPoints.Companion.SEARCH_RESPONSE
import com.eightpeak.salakafarm.utils.EndPoints.Companion.USER_DETAILS
import com.eightpeak.salakafarm.views.home.categories.CategoriesModel
import com.eightpeak.salakafarm.views.home.categories.categoriesbyid.CategoriesByIdModel
import com.eightpeak.salakafarm.views.home.products.ProductModel
import com.eightpeak.salakafarm.views.home.products.ServerResponse
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdModel
import com.eightpeak.salakafarm.views.home.slider.SliderModel
import com.eightpeak.salakafarm.views.login.LoginResponse
import com.eightpeak.salakafarm.views.register.RegisterResponse
import com.eightpeak.salakafarm.views.search.SearchModel
import okhttp3.ResponseBody
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
    suspend fun refresh(@Body body: RequestBodies.TokenBody):Response<AccessToken>

    @GET(USER_DETAILS)
    suspend fun userDetails():Response<UserProfileModel>

     @GET(SEARCH_RESPONSE)
    suspend fun getSearchResponse(@Query("keyword") keyword: String,@Query("filter_sort") filter_sort: String):Response<SearchModel>

    @FormUrlEncoded
    @POST(EndPoints.ADD_TO_WISHLIST)
    suspend fun addToWishList(@Field("product_id") product_id: String): Response<ServerResponse>

     @FormUrlEncoded
     @POST(EndPoints.ADD_TO_CART)
    suspend fun addToCart(@Field("product_id") product_id: String,@Field("qty") qty: String,@Field("options") options: String): Response<ServerResponse>


}