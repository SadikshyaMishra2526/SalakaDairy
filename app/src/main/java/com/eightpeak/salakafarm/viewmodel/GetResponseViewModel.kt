package com.eightpeak.salakafarm.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.Utils
import com.eightpeak.salakafarm.views.home.products.ProductModel
import com.eightpeak.salakafarm.views.home.slider.SliderModel
import com.eightpeak.salakafarm.views.home.ui.addtocart.CartResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class GetResponseViewModel (
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val cartResponse: MutableLiveData<Resource<List<CartResponse>>> = MutableLiveData()
    fun getCartResponse(tokenManager: TokenManager) = viewModelScope.launch {
        cartResponseFetch(tokenManager)
    }
    private suspend fun cartResponseFetch(tokenManager: TokenManager) {
        cartResponse.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response =appRepository.getCartList(tokenManager)
                Log.i("TAG", "fetchPics: "+appRepository.getCartList(tokenManager))
                cartResponse.postValue(handleCartResponse(response))
            } else {
                cartResponse.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> cartResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> cartResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleCartResponse(response: Response<List<CartResponse>>): Resource<List<CartResponse>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    val wishListResponse: MutableLiveData<Resource<List<CartResponse>>> = MutableLiveData()
    fun getWishListResponse(tokenManager: TokenManager) = viewModelScope.launch {
        wishlistResponseFetch(tokenManager)
    }
    private suspend fun wishlistResponseFetch(tokenManager: TokenManager) {
        wishListResponse.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response =appRepository.getWishList(tokenManager)
                Log.i("TAG", "fetchPics: "+appRepository.getWishList(tokenManager))
                wishListResponse.postValue(handleWishListResponse(response))
            } else {
                wishListResponse.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> wishListResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> wishListResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleWishListResponse(response: Response<List<CartResponse>>): Resource<List<CartResponse>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}