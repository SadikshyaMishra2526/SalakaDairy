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
import com.eightpeak.salakafarm.views.home.products.ServerResponse
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdModel
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductRatingModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ProductByIdViewModel (
    app: Application,
    private val appRepository: AppRepository) : AndroidViewModel(app) {

    val productDetailsById: MutableLiveData<Resource<ProductByIdModel>> = MutableLiveData()
    fun getProductById(id:String) = viewModelScope.launch {
        getProductDetailsById(id)
    }

    private suspend fun getProductDetailsById(id:String) {
        productDetailsById.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getProductListById(id)
                productDetailsById.postValue(handlePicsResponse(response))
            } else {
                productDetailsById.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> productDetailsById.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> productDetailsById.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handlePicsResponse(response: Response<ProductByIdModel>): Resource<ProductByIdModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


//    add to cart

    val addToCart: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()



    fun addToCart(tokenManager: TokenManager, productId:String,qty:String,options:String) = viewModelScope.launch {
        addToCartById(tokenManager,productId,qty,options)
    }


    private suspend fun addToCartById(tokenManager: TokenManager, productId:String,qty:String,options:String) {
        addToCart.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.addToCart(tokenManager,productId,qty,options)
                Log.i("TAG", "fetchPics: $response")
                addToCart.postValue(handleAddToCartResponse(response))
            } else {
                addToCart.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
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



//    add to wishlist

    val wishlist: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()
    fun addTowishlist(tokenManager: TokenManager, productId:String) = viewModelScope.launch {
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
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
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
        }
        return Resource.Error(response.message())
    }





//get product rating
//    add to wishlist

    val productRating: MutableLiveData<Resource<ProductRatingModel>> = MutableLiveData()

    fun productRating( productId:String) = viewModelScope.launch {
        productRatingByView(productId)
    }


    private suspend fun productRatingByView( productId:String) {
        productRating.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getProductRating(productId)
                Log.i("TAG", "fetchPics: $response")
                productRating.postValue(handleProductRatingResponse(response))
            } else {
                productRating.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> productRating.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> productRating.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleProductRatingResponse(response: Response<ProductRatingModel>): Resource<ProductRatingModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }




}