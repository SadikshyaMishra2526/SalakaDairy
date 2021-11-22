package com.eightpeak.salakafarm.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.subscription.attributes.BranchModel
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.Utils
import com.eightpeak.salakafarm.views.home.products.ServerResponse
import com.eightpeak.salakafarm.views.order.orderview.viewordercheckoutdetails.CheckOutModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class CheckOutViewModel (app: Application,
                         private val appRepository: AppRepository
) : AndroidViewModel(app) {

//    for checkout details

    val checkoutResponse: MutableLiveData<Resource<CheckOutModel>> = MutableLiveData()
    fun getCheckOutResponse(tokenManager: TokenManager) = viewModelScope.launch {
        getCheckOutResponseFetch(tokenManager)
    }

    private suspend fun getCheckOutResponseFetch(tokenManager: TokenManager) {
        checkoutResponse.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getCheckoutDetails(tokenManager)
                Log.i(
                    "TAG", "fetchPics: " +
                            appRepository.getCheckoutDetails(tokenManager)
                )
                checkoutResponse.postValue(handleCheckoutResponse(response))
            } else {
                checkoutResponse.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {

            Log.i("TAG", "handleCheckoutResponse: " + t.localizedMessage)
            when (t) {
                is IOException -> checkoutResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> checkoutResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleCheckoutResponse(response: Response<CheckOutModel>): Resource<CheckOutModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    //    delete product cart
    val deleteCartById: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun deleteCartById(tokenManager: TokenManager, productId:String) = viewModelScope.launch {
        deleteCartItem(tokenManager,productId)
    }


    private suspend fun deleteCartItem(tokenManager: TokenManager, productId:String) {
        deleteCartById.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.deleteCartItem(tokenManager,productId)
                Log.i("TAG", "fetchPics: $response")
                deleteCartById.postValue(handledeleteCartByIdResponse(response))
            } else {
                deleteCartById.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> deleteCartById.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> deleteCartById.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handledeleteCartByIdResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



    val deleteCartItem: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun deleteCartItemById(tokenManager: TokenManager, productId:String) = viewModelScope.launch {
        deleteCart(tokenManager,productId)
    }


    private suspend fun deleteCart(tokenManager: TokenManager, productId:String) {
        deleteCartItem.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.deleteCartItem(tokenManager,productId)
                Log.i("TAG", "fetchPics: $response")
                deleteCartItem.postValue(handledeleteCartItemResponse(response))
            } else {
                deleteCartItem.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> deleteCartItem.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> deleteCartItem.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handledeleteCartItemResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}