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
import com.eightpeak.salakafarm.views.addtocart.addtocartfragment.CartResponse
import com.eightpeak.salakafarm.views.home.products.Data
import com.eightpeak.salakafarm.views.pages.PageDetailsModel
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



//    add to cart

    val addToCart: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun addToCartView(tokenManager: TokenManager, productId:String,qty:String,options:String) = viewModelScope.launch {
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

//    delete wishlist
val deleteWishlistById: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun deleteWishlistById(tokenManager: TokenManager, productId:String) = viewModelScope.launch {
        deleteViewListItem(tokenManager,productId)
    }


    private suspend fun deleteViewListItem(tokenManager: TokenManager, productId:String) {
        deleteWishlistById.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.deleteWishListItem(tokenManager,productId)
                Log.i("TAG", "fetchPics: $response")
                deleteWishlistById.postValue(handledeleteWishlistByIdResponse(response))
            } else {
                deleteWishlistById.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> deleteWishlistById.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> deleteWishlistById.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handledeleteWishlistByIdResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    //    delete all wishlist
    val deleteWishlistItem: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun deleteAllWishlist(tokenManager: TokenManager, productId:String) = viewModelScope.launch {
        deleteWishlist(tokenManager,productId)
    }


    private suspend fun deleteWishlist(tokenManager: TokenManager, productId:String) {
        deleteWishlistItem.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.deleteWishListItem(tokenManager,productId)
                Log.i("TAG", "fetchPics: $response")
                deleteWishlistItem.postValue(handledeleteWishlistItemResponse(response))
            } else {
                deleteWishlistItem.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> deleteWishlistItem.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> deleteWishlistItem.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handledeleteWishlistItemResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }




    //    get Page Title
    val getPageDetails: MutableLiveData<Resource<PageDetailsModel>> = MutableLiveData()

    fun getPageDetailsView(pageId:String) = viewModelScope.launch {
        fetchPageDetails(pageId)
    }


    private suspend fun fetchPageDetails( pageId:String) {
        getPageDetails.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getPageDetails(pageId)
                Log.i("TAG", "fetchPics: $response")
                getPageDetails.postValue(handleGetPageDetailsResponse(response))
            } else {
                getPageDetails.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> getPageDetails.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> getPageDetails.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleGetPageDetailsResponse(response: Response<PageDetailsModel>): Resource<PageDetailsModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    //    get Random Title
    val getRandomListResponse: MutableLiveData<Resource<List<Data>>> = MutableLiveData()

    fun getRandomListResponseView() = viewModelScope.launch {
        fetchRandomDetails()
    }


    private suspend fun fetchRandomDetails() {
        getRandomListResponse.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getRandomList()
                Log.i("TAG", "fetchPics: $response")
                getRandomListResponse.postValue(handleRandomListDetails(response))
            } else {
                getRandomListResponse.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> getRandomListResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> getRandomListResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleRandomListDetails(response: Response<List<Data>>): Resource<List<Data>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}