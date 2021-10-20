package com.eightpeak.salakafarm.repository

import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.RetrofitInstance

class AppRepository {


    suspend fun loginUser(body: RequestBodies.LoginBody) = RetrofitInstance.useApiWithoutToken.loginCustomer(body)

    suspend fun registerUser(body: RequestBodies.RegisterBody) = RetrofitInstance.useApiWithoutToken.registerCustomer(body)

    suspend fun fetchSlider() = RetrofitInstance.useApiWithoutToken.getSlider()

    suspend fun getCategoriesList() = RetrofitInstance.useApiWithoutToken.getCategoriesList()

    suspend fun getCategoriesById(id:String) = RetrofitInstance.useApiWithoutToken.getCategoriesList()


    suspend fun getProductList() = RetrofitInstance.useApiWithoutToken.getProductList()

}