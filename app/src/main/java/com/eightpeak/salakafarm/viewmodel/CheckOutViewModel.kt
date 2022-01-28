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
import com.eightpeak.salakafarm.subscription.attributes.BranchModel
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.Utils
import com.eightpeak.salakafarm.views.addresslist.AddressListModel
import com.eightpeak.salakafarm.views.home.products.OrderResponse
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
                Log.i("TAG", "checkoutResponse: $response")
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



    val addOrder: MutableLiveData<Resource<OrderResponse>> = MutableLiveData()

    fun addOrder(tokenManager: TokenManager,body:RequestBodies.AddOrder) = viewModelScope.launch {
        addOrderResponse(tokenManager,body)
    }

    private suspend fun addOrderResponse(tokenManager: TokenManager,body:RequestBodies.AddOrder) {
        addOrder.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.addOrder(tokenManager,body)

                Log.i("TAG", "addOrderResponse:mathi "+response)
                Log.i("TAG", "fetchPics: $response")
                addOrder.postValue(handleAddOrderResponse(response))
            } else {
                addOrder.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "addOrderResponse:tala "+t.localizedMessage)
            when (t) {
                is IOException -> addOrder.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> addOrder.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleAddOrderResponse(response: Response<OrderResponse>): Resource<OrderResponse> {
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


    val userAddressList: MutableLiveData<Resource<AddressListModel>> = MutableLiveData()

    fun getUserAddressList(token: TokenManager) = viewModelScope.launch {
        fetchUserAddress(token)
    }

    private suspend fun fetchUserAddress(token: TokenManager) {
        userAddressList.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getAddressList(token)
                userAddressList.postValue(handleAddressResponse(response))
            } else {
                userAddressList.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> userAddressList.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> userAddressList.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleAddressResponse(response: Response<AddressListModel>): Resource<AddressListModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}