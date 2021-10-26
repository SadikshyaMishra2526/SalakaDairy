package com.eightpeak.salakafarm.viewmodel

import UserProfileModel
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.Utils
import com.eightpeak.salakafarm.views.home.slider.SliderModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class UserProfileViewModel (app: Application,
private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val userProfileData: MutableLiveData<Resource<UserProfileModel>> = MutableLiveData()



    private fun getUserDetails(token:String) = viewModelScope.launch {
        fetchUserDetails(token)
    }


    private suspend fun fetchUserDetails(token: String) {
        userProfileData.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getUserDetails(token)
                Log.i("TAG", "fetchUserDetails: "+appRepository.getUserDetails(token))
                userProfileData.postValue(handlePicsResponse(response))
            } else {
                userProfileData.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> userProfileData.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> userProfileData.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handlePicsResponse(response: Response<UserProfileModel>): Resource<UserProfileModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}