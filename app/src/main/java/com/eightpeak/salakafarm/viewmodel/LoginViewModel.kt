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
import com.eightpeak.salakafarm.views.login.LoginResponse
import com.eightpeak.salakafarm.views.register.RegisterResponse
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
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
                Log.i("TAG", "login:login "+response)
                _loginResponse.postValue(handlePicsResponse(response))
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




}