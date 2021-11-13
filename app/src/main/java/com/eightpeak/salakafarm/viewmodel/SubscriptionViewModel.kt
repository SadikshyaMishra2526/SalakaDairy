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
import com.eightpeak.salakafarm.subscription.attributes.BranchModel
import com.eightpeak.salakafarm.subscription.attributes.SubscriptionItemModel
import com.eightpeak.salakafarm.subscription.attributes.SubscriptionPackageModel
import com.eightpeak.salakafarm.subscription.attributes.SubscriptionResponse
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.Utils
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class SubscriptionViewModel (
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

//    for branches list


    val branchesResponse: MutableLiveData<Resource<BranchModel>> = MutableLiveData()
    fun getBranchesList(tokenManager: TokenManager) = viewModelScope.launch {
        branchResponseFetch(tokenManager)
    }

    private suspend fun branchResponseFetch(tokenManager: TokenManager) {
        branchesResponse.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getBranchList(tokenManager)
                Log.i(
                    "TAG", "fetchPics: " +
                            appRepository.getBranchList(tokenManager)
                )
                branchesResponse.postValue(handleBranchResponse(response))
            } else {
                branchesResponse.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {

            Log.i("TAG", "handleBranchResponse: " + t.localizedMessage)
            when (t) {
                is IOException -> branchesResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> branchesResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleBranchResponse(response: Response<BranchModel>): Resource<BranchModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    //    for subscription item list
    val subItemList: MutableLiveData<Resource<SubscriptionItemModel>> = MutableLiveData()

    fun getSubItemList(tokenManager: TokenManager) = viewModelScope.launch {
        subItemList(tokenManager)
    }

    private suspend fun subItemList(tokenManager: TokenManager) {
        subItemList.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getSubscriptionItemList(tokenManager)
                Log.i(
                    "TAG",
                    "getSubscriptionItemList: " + appRepository.getSubscriptionItemList(tokenManager)
                )
                subItemList.postValue(handleSubscriptionItemResponse(response))

            } else {
                subItemList.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {

            Log.i("TAG", "handleBranchResponse: " + t.localizedMessage)
            when (t) {
                is IOException -> subItemList.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> subItemList.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleSubscriptionItemResponse(response: Response<SubscriptionItemModel>): Resource<SubscriptionItemModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


//   get subscription package

    val subPackageList: MutableLiveData<Resource<SubscriptionPackageModel>> = MutableLiveData()

    fun getSubPackageList(tokenManager: TokenManager, id: Int) = viewModelScope.launch {
        subPackageList(tokenManager,id)
    }

    private suspend fun subPackageList(tokenManager: TokenManager,id:Int) {
        subPackageList.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getSubscriptionPackage(tokenManager,id)
                Log.i(
                    "TAG",
                    "getSubscriptionPackageList: " + appRepository.getSubscriptionPackage(tokenManager,id)
                )
                subPackageList.postValue(handleSubscriptionPackageResponse(response))

            } else {
                subPackageList.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {

            Log.i("TAG", "handleBranchResponse: " + t.localizedMessage)
            when (t) {
                is IOException -> subPackageList.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> subPackageList.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleSubscriptionPackageResponse(response: Response<SubscriptionPackageModel>): Resource<SubscriptionPackageModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



//    add to subscription
private val addSubscription: MutableLiveData<Resource<SubscriptionResponse>> = MutableLiveData()

    fun addSubscription(tokenManager: TokenManager, body: RequestBodies.AddSubscription) = viewModelScope.launch {
        addSubscriptionResponse(tokenManager,body)
    }

    private suspend fun addSubscriptionResponse(tokenManager: TokenManager,body: RequestBodies.AddSubscription) {
        addSubscription.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.addSubscription(tokenManager,body)
                Log.i(
                    "TAG",
                    "getSubscriptionPackageList: " + appRepository.addSubscription(tokenManager,body)
                )
                addSubscription.postValue(handleSubscriptionResponse(response))

            } else {
                addSubscription.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {

            Log.i("TAG", "handleBranchResponse: " + t.localizedMessage)
            when (t) {
                is IOException -> addSubscription.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> addSubscription.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleSubscriptionResponse(response: Response<SubscriptionResponse>): Resource<SubscriptionResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}










