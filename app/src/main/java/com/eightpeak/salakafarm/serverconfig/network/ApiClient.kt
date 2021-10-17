package com.eightpeak.salakafarm.serverconfig.network

import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        fun getRetrofitInstance(): Retrofit
        {
            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}
