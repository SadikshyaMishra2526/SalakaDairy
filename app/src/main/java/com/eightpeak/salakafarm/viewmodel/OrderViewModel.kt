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
import com.eightpeak.salakafarm.subscription.displaysubscription.EmployeeTrackDetails
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.Utils
import com.eightpeak.salakafarm.views.home.products.Data
import com.eightpeak.salakafarm.views.home.products.ServerResponse
import com.eightpeak.salakafarm.views.order.orderview.orderhistory.OrderHistoryDetailsModel
import com.eightpeak.salakafarm.views.order.orderview.orderhistory.OrderHistoryModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class OrderViewModel  (
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

//get Order list
val getOrderList: MutableLiveData<Resource<OrderHistoryModel>> = MutableLiveData()


    fun getOrderProductById(tokenManager: TokenManager) = viewModelScope.launch {
        getOrderProductDetailsById(tokenManager)
    }

    private suspend fun getOrderProductDetailsById(tokenManager:TokenManager) {
        getOrderList.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getOrderList(tokenManager)
                Log.i("TAG", "fetchPics: $response")
                getOrderList.postValue(handlePicsResponse(response))
            } else {
                getOrderList.postValue(
                    Resource.Error(getApplication<Application>().getString(
                        R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> getOrderList.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> getOrderList.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handlePicsResponse(response: Response<OrderHistoryModel>): Resource<OrderHistoryModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



//get Order details list
val getOrderListByDetails: MutableLiveData<Resource<OrderHistoryDetailsModel>> = MutableLiveData()


fun getOrderHistoryDetails(tokenManager: TokenManager,id:String) = viewModelScope.launch {
    fetchOrderHistoryDetails(tokenManager,id)
}

private suspend fun fetchOrderHistoryDetails(tokenManager:TokenManager,id: String) {
    getOrderListByDetails.postValue(Resource.Loading())
    try {
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            val response = appRepository.getOrderHistoryDetails(tokenManager,id)
            getOrderListByDetails.postValue(handleOrderHistoryDetails(response))
        } else {
            getOrderListByDetails.postValue(
                Resource.Error(getApplication<Application>().getString(
                    R.string.no_internet_connection)))
        }
    } catch (t: Throwable) {
        Log.i("TAG", "fetchPics: "+t.localizedMessage)
        when (t) {
            is IOException -> getOrderListByDetails.postValue(
                Resource.Error(
                    getApplication<Application>().getString(
                        R.string.network_failure
                    )
                )
            )
            else -> getOrderListByDetails.postValue(
                Resource.Error(
                    getApplication<Application>().getString(
                        R.string.conversion_error
                    )
                )
            )
        }
    }
}

private fun handleOrderHistoryDetails(response: Response<OrderHistoryDetailsModel>): Resource<OrderHistoryDetailsModel> {
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


    val empLatLng: MutableLiveData<Resource<EmployeeTrackDetails>> = MutableLiveData()

    fun empLatLng(tokenManager: TokenManager, body:RequestBodies.EmpLatlng) = viewModelScope.launch {
        empLatLngByView(tokenManager,body)
    }


    private suspend fun empLatLngByView(tokenManager: TokenManager,  body:RequestBodies.EmpLatlng) {
        empLatLng.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getEmpLatLng(tokenManager,body)
                Log.i("TAG", "fetchPics: $response")
                empLatLng.postValue(handleEmpLatLngResponse(response))
            } else {
                empLatLng.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {

            when (t) {
                is IOException -> empLatLng.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> empLatLng.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleEmpLatLngResponse(response: Response<EmployeeTrackDetails>): Resource<EmployeeTrackDetails> {
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
