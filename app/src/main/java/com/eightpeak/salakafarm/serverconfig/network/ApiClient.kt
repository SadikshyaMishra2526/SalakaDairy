package com.eightpeak.salakafarm.serverconfig.network

import com.eightpeak.salakafarm.serverconfig.ApiInterface
import com.eightpeak.salakafarm.utils.EndPoints
import com.eightpeak.salakafarm.utils.EndPoints.Companion.BASE_URL
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

public object ApiClient {
    private val interceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


    private val client = OkHttpClient.Builder()
        .connectTimeout(EndPoints.CONNECTION_TIME_OUT, TimeUnit.MINUTES)
        .readTimeout(EndPoints.CONNECTION_TIME_OUT, TimeUnit.MINUTES)
        .writeTimeout(EndPoints.CONNECTION_TIME_OUT, TimeUnit.MINUTES)

        .retryOnConnectionFailure(true)
        .addNetworkInterceptor(StethoInterceptor())
        .build()


    val retrofitBuilder: Retrofit.Builder by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }


    val apiInterface: ApiInterface by lazy {
        retrofitBuilder
            .build()
            .create(ApiInterface::class.java)
    }
}