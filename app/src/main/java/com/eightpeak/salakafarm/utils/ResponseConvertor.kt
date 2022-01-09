package com.eightpeak.salakafarm.utils

import DisplaySubscriptionModel
import android.annotation.SuppressLint
import android.util.Log
import com.eightpeak.salakafarm.serverconfig.network.ApiClient
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object ResponseConvertor {

    fun convertErrors(response: ResponseBody?): ApiError? {
        val converter: Converter<ResponseBody, ApiError> =
            ApiClient.retrofitBuilder.build().responseBodyConverter(
                ApiError::class.java, arrayOfNulls<Annotation>(0)
            )
        var apiError: ApiError? = null
        try {
            apiError = converter.convert(response)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return apiError
    }

    fun convertSubscriptionViewModel(response: ResponseBody): DisplaySubscriptionModel? {
        val converter: Converter<ResponseBody, DisplaySubscriptionModel> =
            ApiClient.retrofitBuilder.build().responseBodyConverter(
                DisplaySubscriptionModel::class.java, arrayOfNulls<Annotation>(0)
            )
        var displaySubscriptionModel: DisplaySubscriptionModel? = null
        try {
            displaySubscriptionModel = converter.convert(response)
        } catch (e: IOException) {
            Log.i("TAG", "convertSubscriptionViewModel: "+e.localizedMessage)
            e.printStackTrace()
        }
        return displaySubscriptionModel
    }



    @SuppressLint("SimpleDateFormat")
    fun getTodayDate(): String {
        val sdf = SimpleDateFormat("dd/mm/yyyy")
        return sdf.format(Date())
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sdf.format(Date())
    }


}