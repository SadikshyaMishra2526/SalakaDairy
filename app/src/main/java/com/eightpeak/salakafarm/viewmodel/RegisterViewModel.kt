package com.eightpeak.salakafarm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.utils.subutils.MyApplication
import com.hadi.retrofitmvvm.util.Event
import com.hadi.retrofitmvvm.util.Resource
import com.hadi.retrofitmvvm.util.Utils
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException

class RegisterViewModel (
    app: Application,
    private val appRepository: AppRepository) : AndroidViewModel(app) {

    private val _registerResponse = MutableLiveData<Event<Resource<ResponseBody>>>()
    val registerResponse: LiveData<Event<Resource<ResponseBody>>> = _registerResponse


    fun registerUser(body: RequestBodies.RegisterBody) = viewModelScope.launch {
        register(body)
    }

    private suspend fun register(body: RequestBodies.RegisterBody) {
        _registerResponse.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<MyApplication>())) {
                val response = appRepository.registerUser(body)
                _registerResponse.postValue(handleRegisterResponse(response))
            } else {
                _registerResponse.postValue(
                    Event(
                        Resource.Error(getApplication<MyApplication>().getString(
                            R.string.no_internet_connection)))
                )
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _registerResponse.postValue(
                        Event(
                            Resource.Error(
                            getApplication<MyApplication>().getString(
                                R.string.network_failure
                            )
                        ))
                    )
                }
                else -> {
                    _registerResponse.postValue(
                        Event(
                            Resource.Error(
                            getApplication<MyApplication>().getString(
                                R.string.conversion_error
                            )
                        ))
                    )
                }
            }
        }
    }

    private fun handleRegisterResponse(response: Response<ResponseBody>): Event<Resource<ResponseBody>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }
}
