package com.eightpeak.salakafarm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.ApiInterface
import com.eightpeak.salakafarm.serverconfig.network.ApiClient
import com.eightpeak.salakafarm.utils.subutils.MyApplication
import com.eightpeak.salakafarm.views.home.slider.SliderModel
import com.hadi.retrofitmvvm.util.Resource
import com.hadi.retrofitmvvm.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class SliderViewModel(
    app: Application,
    private val appRepository: AppRepository
    ) : AndroidViewModel(app) {

        val picsData: MutableLiveData<Resource<SliderModel>> = MutableLiveData()

        init {
            getSliderPictures()
        }

        private fun getSliderPictures() = viewModelScope.launch {
            fetchPics()
        }


        private suspend fun fetchPics() {
            picsData.postValue(Resource.Loading())
            try {
                if (Utils.hasInternetConnection(getApplication<MyApplication>())) {
                    val response = appRepository.fetchSlider()
                    picsData.postValue(handlePicsResponse(response))
                } else {
                    picsData.postValue(Resource.Error(getApplication<MyApplication>().getString(R.string.no_internet_connection)))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> picsData.postValue(
                        Resource.Error(
                            getApplication<MyApplication>().getString(
                                R.string.network_failure
                            )
                        )
                    )
                    else -> picsData.postValue(
                        Resource.Error(
                            getApplication<MyApplication>().getString(
                                R.string.conversion_error
                            )
                        )
                    )
                }
            }
        }

        private fun handlePicsResponse(response: Response<SliderModel>): Resource<SliderModel> {
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    return Resource.Success(resultResponse)
                }
            }
            return Resource.Error(response.message())
        }


    }