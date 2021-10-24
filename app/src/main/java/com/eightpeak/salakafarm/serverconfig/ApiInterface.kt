package com.eightpeak.salakafarm.serverconfig

import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.serverconfig.network.AccessToken
import com.eightpeak.salakafarm.utils.EndPoints.Companion.REFRESH_TOKEN
import com.eightpeak.salakafarm.views.home.categories.CategoriesModel
import com.eightpeak.salakafarm.views.home.categories.categoriesbyid.CategoriesByIdModel
import com.eightpeak.salakafarm.views.home.products.ProductModel
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdModel
import com.eightpeak.salakafarm.views.home.slider.SliderModel
import com.eightpeak.salakafarm.views.register.RegisterResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {

    //  get shop info
    @GET(EndPoints.SLIDER)
    suspend fun getSlider(): Response<SliderModel>


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
    suspend fun loginCustomer(@Body body: RequestBodies.LoginBody): Response<RegisterResponse>

    @POST(EndPoints.REGISTER)
    suspend fun registerCustomer(@Body body: RequestBodies.RegisterBody): Response<RegisterResponse>

    @POST(REFRESH_TOKEN)
    suspend fun refresh(@Body body: RequestBodies.TokenBody):Response<AccessToken>


}