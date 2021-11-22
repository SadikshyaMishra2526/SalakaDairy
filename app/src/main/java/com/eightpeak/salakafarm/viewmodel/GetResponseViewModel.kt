package com.eightpeak.salakafarm.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
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
    val deleteWishlist: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun deleteAllWish(tokenManager: TokenManager) = viewModelScope.launch {
        deleteWishlist(tokenManager)
    }

    suspend fun deleteWishlist(tokenManager: TokenManager) {
        deleteWishlist.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.deleteWishListAll(tokenManager)
                Log.i("TAG", "fetchPics: $response")
                deleteWishlist.postValue(handledeleteWishlistResponse(response))
            } else {
                deleteWishlist.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> deleteWishlist.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> deleteWishlist.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handledeleteWishlistResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
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



    //    get updateCart
    val updateCartResponse: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun updateCartResponseView(tokenManager: TokenManager, productId:String, QTY:String) = viewModelScope.launch {
        updateCartDetails(tokenManager,productId,QTY)
    }


    private suspend fun updateCartDetails(
        tokenManager: TokenManager,
        productId: String,
        QTY: String
    ) {
        updateCartResponse.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.updateCart(tokenManager,productId,QTY)
                Log.i("TAG", "fetchPics: $response")
                updateCartResponse.postValue(handleUpdateCartDetails(response))
            } else {
                updateCartResponse.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> updateCartResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> updateCartResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleUpdateCartDetails(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }




    //    delete wishlist
    val deleteCart: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun deleteCart(tokenManager: TokenManager) = viewModelScope.launch {
        deleteCartItem(tokenManager)
    }


    private suspend fun deleteCartItem(tokenManager: TokenManager) {
        deleteCart.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.deleteCart(tokenManager)
                Log.i("TAG", "fetchPics: $response")
                deleteCart.postValue(handledeleteCartResponse(response))
            } else {
                deleteCart.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> deleteCart.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> deleteCart.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handledeleteCartResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



    //    delete wishlist
    val addComplain: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun addComplain(tokenManager: TokenManager,body: RequestBodies.AddComplain) = viewModelScope.launch {
        addComplainItem(tokenManager,body)
    }


    private suspend fun addComplainItem(tokenManager: TokenManager,body: RequestBodies.AddComplain) {
        addComplain.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.addComplain(tokenManager,body)
                Log.i("TAG", "fetchPics: $response")
                addComplain.postValue(handleAddComplainResponse(response))
            } else {
                addComplain.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> addComplain.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> addComplain.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleAddComplainResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}