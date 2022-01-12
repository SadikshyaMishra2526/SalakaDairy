package com.eightpeak.salakafarm.viewmodel

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
import com.eightpeak.salakafarm.views.home.products.ServerResponse
import com.eightpeak.salakafarm.views.home.products.productbyid.ProductByIdModel
import com.eightpeak.salakafarm.views.register.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class OTPViewModel  (
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val generateOtp: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()
    fun checkOTP(phone: String) = viewModelScope.launch {
        requestOTP(phone)
    }

    private suspend fun requestOTP(phone: String) {
        generateOtp.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.generateOTP(phone)
                Log.i("TAG", "fetchPics: $response")
                generateOtp.postValue(handleCheckOTP(response))
            } else {
                generateOtp.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.no_internet_connection
                        )
                    )
                )
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: " + t.localizedMessage)
            when (t) {
                is IOException -> generateOtp.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> generateOtp.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleCheckOTP(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



    val verifyOtp: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()
    fun checkVerifyOTP(phone: String,otp: String) = viewModelScope.launch {
       checkOTPValidation(phone,otp)
    }

    private suspend fun checkOTPValidation(phone: String,otp: String) {
        verifyOtp.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.verifiyOTP(phone,otp)
                Log.i("TAG", "fetchPics: $response")
                verifyOtp.postValue(handleOTPVerification(response))
            } else {
                verifyOtp.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.no_internet_connection
                        )
                    )
                )
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: " + t.localizedMessage)
            when (t) {
                is IOException -> verifyOtp.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> verifyOtp.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleOTPVerification(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private val _registerResponse = MutableLiveData<Event<Resource<RegisterResponse>>>()
    val registerResponse: LiveData<Event<Resource<RegisterResponse>>> = _registerResponse


    fun registerUser(body: RequestBodies.RegisterBody) = viewModelScope.launch {
        register(body)
    }

    private suspend fun register(body: RequestBodies.RegisterBody) {
        Log.e("TAG", "register: $body")
        _registerResponse.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.registerUser(body)
                Log.i("TAG", "register: "+response.body())
                _registerResponse.postValue(handleRegisterResponse(response))
            } else {
                _registerResponse.postValue(
                    Event(
                        Resource.Error(getApplication<Application>().getString(
                            R.string.no_internet_connection)))
                )
            }
        } catch (t: Throwable) {
            Log.i("TAG", "register: "+t.localizedMessage)
            when (t) {
                is IOException -> {
                    _registerResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Application>().getString(
                                    R.string.network_failure
                                )
                            ))
                    )
                }
                else -> {
                    _registerResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Application>().getString(
                                    R.string.conversion_error
                                )
                            ))
                    )
                }
            }
        }
    }

    private fun handleRegisterResponse(response: Response<RegisterResponse>): Event<Resource<RegisterResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }

}