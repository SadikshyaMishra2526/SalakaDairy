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
import com.eightpeak.salakafarm.views.home.products.Data
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdModel
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


}
