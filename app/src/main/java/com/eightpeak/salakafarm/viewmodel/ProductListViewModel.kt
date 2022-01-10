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
import com.eightpeak.salakafarm.views.home.products.ServerResponse
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException

class ProductListViewModel(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val picsData: MutableLiveData<Resource<ProductModel>> = MutableLiveData()

    init {
        getSliderPictures()
    }

    private fun getSliderPictures() = viewModelScope.launch {
        fetchPics()
    }


    private suspend fun fetchPics() {
        picsData.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getProductList()
                Log.i("TAG", "fetchPics: $response")
                picsData.postValue(handlePicsResponse(response))
            } else {
                picsData.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> picsData.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> picsData.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handlePicsResponse(response: Response<ProductModel>): Resource<ProductModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



//    add to wishlist

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

//    remove from wishlist
val removeFromWishlist: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()



    fun removeFromWishlist(tokenManager: TokenManager, productId:String) = viewModelScope.launch {
        removeFromWishView(tokenManager,productId)
    }


    private suspend fun removeFromWishView(tokenManager: TokenManager, productId:String) {
        removeFromWishlist.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.deleteWishListItem(tokenManager,productId)
                Log.i("TAG", "fetchPics: $response")
                removeFromWishlist.postValue(handleRevomeWishListResponse(response))
            } else {
                removeFromWishlist.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {

            when (t) {
                is IOException -> removeFromWishlist.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> removeFromWishlist.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleRevomeWishListResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }else{
            Log.i("TAG", "handleWishListResponse:$response ")
        }
        return Resource.Error(response.message())
    }

}