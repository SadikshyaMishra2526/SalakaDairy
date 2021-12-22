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
import com.eightpeak.salakafarm.utils.subutils.Event
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.Utils
import com.eightpeak.salakafarm.views.register.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class RegisterViewModel (
    app: Application,
    private val appRepository: AppRepository) : AndroidViewModel(app) {

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
