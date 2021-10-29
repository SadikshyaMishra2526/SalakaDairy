package com.eightpeak.salakafarm.repository

import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.RetrofitInstance
import com.eightpeak.salakafarm.serverconfig.network.TokenManager

class AppRepository {


    suspend fun loginUser(body: RequestBodies.LoginBody) = RetrofitInstance.useApiWithoutToken.loginCustomer(body)

    suspend fun registerUser(body: RequestBodies.RegisterBody) = RetrofitInstance.useApiWithoutToken.registerCustomer(body)


    suspend fun fetchSlider() = RetrofitInstance.useApiWithoutToken.getSlider()

    suspend fun getCategoriesList() = RetrofitInstance.useApiWithoutToken.getCategoriesList()


    suspend fun getCategoriesListById(id: String) = RetrofitInstance.useApiWithoutToken.getCategoriesById(id)

    suspend fun getProductList() =  RetrofitInstance.useApiWithoutToken.getProductList()


    suspend fun getProductListById(id:String) =  RetrofitInstance.useApiWithoutToken.getProductById(id)


    suspend fun getUserDetails(tokenManager: TokenManager) =  RetrofitInstance.useApiWithAccessToken(tokenManager).userDetails()


   suspend fun getSearchResponse(tokenManager: TokenManager,keyboard:String,filterSort:String) =  RetrofitInstance.useApiWithAccessToken(tokenManager).getSearchResponse(keyboard,filterSort)


    suspend fun addToCart(tokenManager: TokenManager,product_id:String,qty:String,options:String) =  RetrofitInstance.useApiWithAccessToken(tokenManager).addToCart(product_id,qty,options)


    suspend fun addToWishList(tokenManager: TokenManager,product_id:String) =  RetrofitInstance.useApiWithoutToken.addToWishList(product_id)

}