package com.eightpeak.salakafarm.serverconfig

import com.eightpeak.salakafarm.serverconfig.network.CustomAuthenticator
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.EndPoints
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class RetrofitInstance {
    companion object {

        private val client = buildClient()
        private val retrofit = buildRetrofit(client)

        private fun buildClient(): OkHttpClient {
            val builder:  OkHttpClient.Builder = OkHttpClient.Builder()
                .connectTimeout(EndPoints.CONNECTION_TIME_OUT, TimeUnit.MINUTES)
                .readTimeout(EndPoints.CONNECTION_TIME_OUT, TimeUnit.MINUTES)
                .writeTimeout(EndPoints.CONNECTION_TIME_OUT, TimeUnit.MINUTES)
                .addNetworkInterceptor(StethoInterceptor())
                .addInterceptor(object : Interceptor {
                    @Throws(IOException::class)
                    override fun intercept(chain: Interceptor.Chain): Response {
                        var request: Request = chain.request()
                        val builder: Request.Builder = request.newBuilder()
                            .addHeader("apiconnection", "appmobile")
                            .addHeader("apikey", "bc1039fd2d3b46fbf5027c069068b869")
                            .addHeader("Accept", "application/json")
                            .addHeader("Connection", "close")
                        request = builder.build()
                        return chain.proceed(request)
                    }
                })
            return builder.build()
        }

        private fun buildRetrofit(client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(EndPoints.BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }

        private fun <T> createService(service: Class<T>?): T {
            return retrofit.create(service)
        }

        private fun <T> createServiceWithAuth(service: Class<T>?, tokenManager: TokenManager): T {
            val newClient = client.newBuilder().addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    var request: Request = chain.request()
                    val builder:    Request.Builder = request.newBuilder()
                    if (tokenManager.token != null) {
                        builder.addHeader(

                            "Authorization",
                            "Bearer " + tokenManager.token
                        )
                    }
                    request = builder.build()
                    return chain.proceed(request)
                }
            }).authenticator(CustomAuthenticator.getInstance(tokenManager)).build()
            val newRetrofit = retrofit.newBuilder().client(newClient).build()
            return newRetrofit.create(service)
        }

        fun getRetrofit(): Retrofit? {
            return retrofit
        }

        val useApiWithoutToken by lazy {
            createService(ApiInterface::class.java)
        }

        fun useApiWithAccessToken( tokenManager: TokenManager)  : ApiInterface =
            createServiceWithAuth(ApiInterface::class.java,tokenManager)

//        val useApiWithAccessToken by lazy {
//            createServiceWithAuth(ApiInterface::class.java,)
//        }
    }

}
