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
import com.eightpeak.salakafarm.views.addresslist.AddressListModel
import com.eightpeak.salakafarm.views.home.products.ServerResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class UserProfileViewModel (app: Application,
private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val userAddressList: MutableLiveData<Resource<AddressListModel>> = MutableLiveData()

    fun getUserAddressList(token:TokenManager) = viewModelScope.launch {
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

    val userProfileDetails: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    public fun getUserProfileDetails(token:TokenManager,body: RequestBodies.UserProfile) = viewModelScope.launch {
        fetchUserDetails(token,body)
    }

    private suspend fun fetchUserDetails(token: TokenManager,body: RequestBodies.UserProfile) {
        userProfileDetails.postValue(Resource.Loading())

        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getUserProfile(token,body)
                Log.i("TAG", "fetchPics: $response")

                userProfileDetails.postValue(handleUserProfileResponse(response))
            } else {
                userProfileDetails.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: ${t.localizedMessage}")

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

    //    for update details password
    val updatePassword: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    public fun updatePasswordDetails(token:TokenManager,body: RequestBodies.UpdatePassword) = viewModelScope.launch {
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




//    add new address
val addNewAddress: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    public fun addNewAddressDetails(token:TokenManager,body: RequestBodies.AddAddress) = viewModelScope.launch {
        addNewAddress(token,body)
    }

    private suspend fun addNewAddress(token: TokenManager , body: RequestBodies.AddAddress) {
        addNewAddress.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.addNewAddress(token,body)
                addNewAddress.postValue(handleAddNewAddressDetailsResponse(response))
            } else {
                addNewAddress.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> addNewAddress.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> addNewAddress.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleAddNewAddressDetailsResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


//    delete existing address
val deleteAddress: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    public fun deleteAddressDetails(token:TokenManager,addressId:String) = viewModelScope.launch {
        deleteAddress(token,addressId)
    }

    private suspend fun deleteAddress(token: TokenManager , addressId:String) {
        deleteAddress.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.deleteAddress(token,addressId)
                deleteAddress.postValue(handleDeleteAddressResponse(response))
            } else {
                deleteAddress.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> deleteAddress.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> deleteAddress.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleDeleteAddressResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}