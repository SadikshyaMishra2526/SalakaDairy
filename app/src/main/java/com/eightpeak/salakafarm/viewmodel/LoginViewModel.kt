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
import com.eightpeak.salakafarm.views.home.products.GoogleLoginResponse
import com.eightpeak.salakafarm.views.login.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class LoginViewModel(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    private val _loginResponse = MutableLiveData<Event<Resource<LoginResponse>>>()
    val loginResponse:LiveData<Event<Resource<LoginResponse>>> = _loginResponse


    fun loginUser(body: RequestBodies.LoginBody) = viewModelScope.launch {
        login(body)
    }

    private suspend fun login(body: RequestBodies.LoginBody) {
        _loginResponse.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.loginUser(body)
                if(response.code()==401){
                    _loginResponse.postValue(
                        Event(Resource.Error(
                            getApplication<Application>().getString(R.string.unauthorized),
                        )))
                }else if(response.code()==404){
                    _loginResponse.postValue(
                        Event(Resource.Error(
                            "Phone not registered",
                        )))
                } else{
                    _loginResponse.postValue(handlePicsResponse(response))

                }
            } else {
                _loginResponse.postValue(Event(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection))))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "login: ${t.localizedMessage}")
            when (t) {
                is IOException -> {
                    _loginResponse.postValue(
                        Event(Resource.Error(
                            getApplication<Application>().getString(
                                R.string.network_failure
                            )
                        ))
                    )
                }
                else -> {
                    _loginResponse.postValue(
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

    private fun handlePicsResponse(response: Response<LoginResponse>): Event<Resource<LoginResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }




    private val _userDetailsResponse = MutableLiveData<Event<Resource<UserProfileModel>>>()
    val userDetailsResponse:LiveData<Event<Resource<UserProfileModel>>> = _userDetailsResponse


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



    val userAddressList: MutableLiveData<Resource<AddressListModel>> = MutableLiveData()

    fun getUserAddressList(token:TokenManager) = viewModelScope.launch {
        fetchUserAddress(token)
    }


    private suspend fun fetchUserAddress(token: TokenManager) {
        userAddressList.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getAddressList(token)
                Log.i("TAG", "fetchUserAddress: "+response)
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



   val socialGoogle: MutableLiveData<Resource<GoogleLoginResponse>> = MutableLiveData()

    fun socialGoogle(first_name: String,
                     last_name: String,
                     email: String,
                     fcm_token: String,
                     provider_id: String,
                     avatar: String,
                     provider: String,
                     phone: String) = viewModelScope.launch {
        googleLogin(first_name,last_name,email,fcm_token,provider_id,avatar,provider,phone)
    }


    private suspend fun googleLogin(first_name: String,
                                    last_name: String,
                                    email: String,
                                    fcm_token: String,
                                    provider_id: String,
                                    avatar: String,
                                    provider: String,
                                    phone: String) {
        socialGoogle.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.googleLogin(first_name,last_name,email,fcm_token,provider_id,avatar,provider,phone)
                Log.i("TAG", "fetchUserAddress: $response")
                socialGoogle.postValue(handleGoogleResponse(response))
            } else {
                socialGoogle.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchUserAddress: "+t.localizedMessage)
            when (t) {
                is IOException -> socialGoogle.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> socialGoogle.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleGoogleResponse(response: Response<GoogleLoginResponse>): Resource<GoogleLoginResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



}