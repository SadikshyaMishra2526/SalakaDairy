package com.eightpeak.salakafarm.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.Utils
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class OrderViewModel  (
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

//get Order list
    val getOrderList: MutableLiveData<Resource<ProductByIdModel>> = MutableLiveData()



    fun getProductById(id:String) = viewModelScope.launch {
        getProductDetailsById(id)
    }


    private suspend fun getProductDetailsById(id:String) {
        getOrderList.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getProductListById(id)
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

    private fun handlePicsResponse(response: Response<ProductByIdModel>): Resource<ProductByIdModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}
