package com.eightpeak.salakafarm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.Utils
import com.eightpeak.salakafarm.views.addresslist.AddressListModel
import com.eightpeak.salakafarm.views.home.products.ServerResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class UserProfileViewModel (app: Application,
private val appRepository: AppRepository
) : AndroidViewModel(app) {

    private val userAddressList: MutableLiveData<Resource<AddressListModel>> = MutableLiveData()

    private fun getUserAddressList(token:TokenManager) = viewModelScope.launch {
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


// user profile details

    private val userProfileDetails: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    private fun getUserProfileDetails(token:TokenManager,body: RequestBodies.UserProfile) = viewModelScope.launch {
        fetchUserDetails(token,body)
    }

    private suspend fun fetchUserDetails(token: TokenManager,body: RequestBodies.UserProfile) {
        userProfileDetails.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getUserProfile(token,body)
                userProfileDetails.postValue(handleUserProfileResponse(response))
            } else {
                userProfileDetails.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> userProfileDetails.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> userProfileDetails.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleUserProfileResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


//    for update details address
    private val updateAddressDetails: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    private fun getUserAddressDetails(token:TokenManager,body: RequestBodies.UpdateAddressList) = viewModelScope.launch {
        fetchAddressDetails(token,body)
    }

    private suspend fun fetchAddressDetails(token: TokenManager,body: RequestBodies.UpdateAddressList) {
        updateAddressDetails.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.updateAddressList(token,body)
                updateAddressDetails.postValue(handleUpdateAddressResponse(response))
            } else {
                updateAddressDetails.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> updateAddressDetails.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> updateAddressDetails.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleUpdateAddressResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())




    }

    //    for update details address
    private val updatePassword: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    private fun updatePasswordDetails(token:TokenManager,body: RequestBodies.UpdatePassword) = viewModelScope.launch {
        fetchPasswordDetails(token,body)
    }

    private suspend fun fetchPasswordDetails(token: TokenManager , body: RequestBodies.UpdatePassword) {
        updatePassword.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.updatePassword(token,body)
                updatePassword.postValue(handleUpdatePasswordDetailsResponse(response))
            } else {
                updatePassword.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> updatePassword.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> updatePassword.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleUpdatePasswordDetailsResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}