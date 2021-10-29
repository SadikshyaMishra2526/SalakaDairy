package com.eightpeak.salakafarm.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eightpeak.salakafarm.R
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.serverconfig.network.TokenManager
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.Utils
import com.eightpeak.salakafarm.views.search.SearchModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class SearchViewModel (app: Application,
private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val searchData: MutableLiveData<Resource<SearchModel>> = MutableLiveData()
    fun getSearchData(tokenManager: TokenManager,keyword:String,filterSort :String) = viewModelScope.launch {
        getSearchResult(tokenManager,keyword,filterSort)
    }


    private suspend fun getSearchResult(
        tokenManager: TokenManager,
        keyword: String,
        filterSort: String
    ) {
        searchData.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getSearchResponse(tokenManager,keyword,filterSort)
                Log.i("TAG", "fetchPics: $response")
                searchData.postValue(handlePicsResponse(response))
            } else {
                searchData.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> searchData.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> searchData.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handlePicsResponse(response: Response<SearchModel>): Resource<SearchModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}