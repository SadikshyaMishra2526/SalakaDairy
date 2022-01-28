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
import com.eightpeak.salakafarm.views.home.categories.CategoriesModel
import com.eightpeak.salakafarm.views.home.categories.categoriesbyid.CategoriesByIdModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class CategoriesByIdViewModel(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val categoriesbyIdData: MutableLiveData<Resource<CategoriesByIdModel>> = MutableLiveData()



    fun getCategoriesByIdDetails(id:String) = viewModelScope.launch {
        getCategoriesById(id)
    }


    private suspend fun getCategoriesById(id:String) {
        categoriesbyIdData.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getCategoriesListById(id)
                categoriesbyIdData.postValue(handlePicsResponse(response))
            } else {
                categoriesbyIdData.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> categoriesbyIdData.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> categoriesbyIdData.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handlePicsResponse(response: Response<CategoriesByIdModel>): Resource<CategoriesByIdModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}