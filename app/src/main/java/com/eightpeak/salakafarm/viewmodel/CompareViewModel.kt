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
import com.eightpeak.salakafarm.views.comparelist.CompareResponse
import com.eightpeak.salakafarm.views.home.products.ServerResponse
import com.eightpeak.salakafarm.views.addtocart.addtocartfragment.CartResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class CompareViewModel (
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val compareResponse: MutableLiveData<Resource<CompareResponse>> = MutableLiveData()
    fun getCompareResponse(tokenManager: TokenManager,compareId:String) = viewModelScope.launch {
        compareResponseFetch(tokenManager,compareId)
    }

    private suspend fun compareResponseFetch(tokenManager: TokenManager,compareId:String) {
        compareResponse.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getCompareProduct(tokenManager,compareId)
                Log.i("TAG", "fetchPics: " + appRepository.getCompareProduct(tokenManager,compareId))
                compareResponse.postValue(handleCartResponse(response))
            } else {
                compareResponse.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> compareResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> compareResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleCartResponse(response: Response<CompareResponse>): Resource<CompareResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private val wishListResponse: MutableLiveData<Resource<List<CartResponse>>> = MutableLiveData()
    fun getWishListResponse(tokenManager: TokenManager) = viewModelScope.launch {
        wishlistResponseFetch(tokenManager)
    }

    private suspend fun wishlistResponseFetch(tokenManager: TokenManager) {
        wishListResponse.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getWishList(tokenManager)
                Log.i("TAG", "fetchPics: " + appRepository.getWishList(tokenManager))
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


//    add to cart

    val addToCart: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun addToCartView(tokenManager: TokenManager, productId: String, qty: String, options: String) =
        viewModelScope.launch {
            addToCartById(tokenManager, productId, qty, options)
        }


    private suspend fun addToCartById(
        tokenManager: TokenManager,
        productId: String,
        qty: String,
        options: String
    ) {
        addToCart.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.addToCart(tokenManager, productId, qty, options)
                addToCart.postValue(handleAddToCartResponse(response))
            } else {
                addToCart.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> addToCart.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> addToCart.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleAddToCartResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}