package com.eightpeak.salakafarm.viewmodel

import UserProfileModel
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.subutils.Event
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.Utils
import com.eightpeak.salakafarm.views.addresslist.AddressListModel
import com.eightpeak.salakafarm.views.home.products.ServerResponse
import com.eightpeak.salakafarm.views.home.products.UpdatePasswordResponse
import com.eightpeak.salakafarm.views.home.products.UserAddressEdit
import com.eightpeak.salakafarm.views.home.products.UserProfileResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
                Log.i("TAG", "fetchUserAddress: $response")
                userAddressList.postValue(handleAddressResponse(response))
            } else {
                userAddressList.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchUserAddress: "+t.localizedMessage)
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

    val userProfileDetails: MutableLiveData<Resource<UserProfileResponse>> = MutableLiveData()

     fun getUserProfileDetails(token:TokenManager, first_name: RequestBody,
                               last_name: RequestBody,
                               sex: RequestBody,
                               birthday: RequestBody,
                               avatar: MultipartBody.Part) = viewModelScope.launch {
        fetchUserDetails(token,first_name,last_name,sex,birthday,avatar)
    }

    private suspend fun fetchUserDetails(token: TokenManager,  first_name: RequestBody,
                                         last_name: RequestBody,
                                         sex: RequestBody,
                                         birthday: RequestBody,
                                         avatar: MultipartBody.Part) {
        userProfileDetails.postValue(Resource.Loading())

        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getUserProfile(token,first_name,last_name,sex,birthday,avatar)
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

    private fun handleUserProfileResponse(response: Response<UserProfileResponse>): Resource<UserProfileResponse> {
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
                Log.i("TAG", "fetchAddressDetails: "+response)
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
    val updatePassword: MutableLiveData<Resource<UpdatePasswordResponse>> = MutableLiveData()

    public fun updatePasswordDetails(token:TokenManager, body: RequestBodies.UpdatePassword) = viewModelScope.launch {
        fetchPasswordDetails(token,body)
    }

    private suspend fun fetchPasswordDetails(token: TokenManager , body: RequestBodies.UpdatePassword) {
        updatePassword.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.updatePassword(token,body)
                Log.i("TAG", "fetchPasswordDetails: "+response.body())
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

    private fun handleUpdatePasswordDetailsResponse(response: Response<UpdatePasswordResponse>): Resource<UpdatePasswordResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }




//    add new address
     val addNewAddress: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

     fun addNewAddressDetails(token:TokenManager,body: RequestBodies.AddAddress) = viewModelScope.launch {
        addNewAddress(token,body)
    }

    private suspend fun addNewAddress(token: TokenManager , body: RequestBodies.AddAddress) {
        addNewAddress.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.addNewAddress(token,body)
                Log.i("TAG", "addNewAddress: +"+response)
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


 val editAddress: MutableLiveData<Resource<UserAddressEdit>> = MutableLiveData()

     fun editAddressDetails(token:TokenManager,body: RequestBodies.EditAddress) = viewModelScope.launch {
        editAddress(token,body)
    }

    private suspend fun editAddress(token: TokenManager , body: RequestBodies.EditAddress) {
        editAddress.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.editAddress(token,body)
                Log.i("TAG", "editAddress: +$response")
                editAddress.postValue(handleEditAddressDetailsResponse(response))
            } else {
                editAddress.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> editAddress.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> editAddress.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleEditAddressDetailsResponse(response: Response<UserAddressEdit>): Resource<UserAddressEdit> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


//    delete existing address
val deleteAddress: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

     fun deleteAddressDetails(token:TokenManager,addressId:String) = viewModelScope.launch {
        deleteAddress(token,addressId)
         Log.i("TAG", "deleteAddress: "+addressId)

     }

    private suspend fun deleteAddress(token: TokenManager , addressId:String) {
        deleteAddress.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.deleteAddress(token,addressId)
                Log.i("TAG", "deleteAddress: "+response)
                deleteAddress.postValue(handleDeleteAddressResponse(response))
            } else {
                deleteAddress.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "deleteAddress: "+t.localizedMessage)
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

    private val _userDetailsResponse = MutableLiveData<Event<Resource<UserProfileModel>>>()
    val userDetailsResponse: LiveData<Event<Resource<UserProfileModel>>> = _userDetailsResponse


    fun userDetailsUser(tokenManager: TokenManager) = viewModelScope.launch {
        userDetails(tokenManager)
    }

    private suspend fun userDetails(tokenManager: TokenManager) {
        _userDetailsResponse.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getUserDetails(tokenManager)

                Log.i("TAG", "login:user details "+response)
                _userDetailsResponse.postValue(handleuserDetailsResponse(response))
            } else {
                _userDetailsResponse.postValue(Event(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection))))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "login: ${t.localizedMessage}")
            when (t) {
                is IOException -> {
                    _userDetailsResponse.postValue(
                        Event(Resource.Error(
                            getApplication<Application>().getString(
                                R.string.network_failure
                            )
                        ))
                    )
                }
                else -> {
                    _userDetailsResponse.postValue(
                        Event(Resource.Error(
                            getApplication<Application>().getString(
                                R.string.conversion_error
                            )
                        ))
                    )
                }
            }
        }
    }
    private fun handleuserDetailsResponse(response: Response<UserProfileModel>): Event<Resource<UserProfileModel>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }


//    logout

    val logout: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun requestLogout(token:TokenManager) = viewModelScope.launch {
        requestLogoutResponse(token)
    }

    private suspend fun requestLogoutResponse(token: TokenManager) {
        logout.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.logout(token)
                Log.i("TAG", "logoutlogoutlogoutlogout: $response")
                logout.postValue(handleLogoutResponse(response))
            } else {
                logout.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "logoutlogout: "+t.localizedMessage)
            when (t) {
                is IOException -> logout.postValue(
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
    private fun handleLogoutResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}