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
                Log.i("TAG", "fetchPics: $response")
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

    val wishlist: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()



    fun addtowishlist(tokenManager: TokenManager, productId:String) = viewModelScope.launch {
        wishlistByView(tokenManager,productId)
    }


    private suspend fun wishlistByView(tokenManager: TokenManager, productId:String) {
        wishlist.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.addToWishList(tokenManager,productId)
                Log.i("TAG", "fetchPics: $response")
                wishlist.postValue(handleWishListResponse(response))
            } else {
                wishlist.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {

            when (t) {
                is IOException -> wishlist.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> wishlist.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleWishListResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }else{
            Log.i("TAG", "handleWishListResponse:$response ")
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