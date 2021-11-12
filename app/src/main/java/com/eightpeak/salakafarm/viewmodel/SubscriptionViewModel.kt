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
import com.eightpeak.salakafarm.subscription.attributes.BranchModel
import com.eightpeak.salakafarm.subscription.attributes.SubscriptionItemModel
import com.eightpeak.salakafarm.subscription.attributes.SubscriptionPackageModel
import com.eightpeak.salakafarm.utils.subutils.Event
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.Utils
import com.eightpeak.salakafarm.views.addtocart.addtocartfragment.CartResponse
import com.eightpeak.salakafarm.views.home.slider.SliderModel
import com.eightpeak.salakafarm.views.search.SearchModel
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
        cartResponseFetch(tokenManager)
    }
    private suspend fun cartResponseFetch(tokenManager: TokenManager) {
        branchesResponse.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getBranchList(tokenManager)
                Log.i("TAG", "fetchPics: "+
                         appRepository.getBranchList(tokenManager))
                branchesResponse.postValue(handleBranchResponse(response))
            } else {
                branchesResponse.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
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
    private val _subItemList = MutableLiveData<Event<Resource<SubscriptionItemModel>>>()

    fun getSubItemList(tokenManager :TokenManager) = viewModelScope.launch {
        subItemList(tokenManager)
    }

    private suspend fun subItemList(tokenManager: TokenManager) {
        _subItemList.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getSubscriptionItemList(tokenManager)
                Log.i("TAG", "getSubscriptionItemList: "+appRepository.getSubscriptionItemList(tokenManager))
                _subItemList.postValue(handleSubscriptionItemResponse(response))
            } else {
                _subItemList.postValue(
                    Event(
                        Resource.Error(getApplication<Application>().getString(
                            R.string.no_internet_connection)))
                )
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _subItemList.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Application>().getString(
                                    R.string.network_failure
                                )
                            ))
                    )
                }
                else -> {
                    _subItemList.postValue(
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

    private fun handleSubscriptionItemResponse(response: Response<SubscriptionItemModel>): Event<Resource<SubscriptionItemModel>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }




    //    for subscription package list
    private val _subPackageList = MutableLiveData<Event<Resource<SubscriptionPackageModel>>>()

    fun getSubPackageList(tokenManager :TokenManager,sub_item_id:String) = viewModelScope.launch {
        subPackageList(tokenManager,sub_item_id)
    }

    private suspend fun subPackageList(tokenManager: TokenManager,sub_item_id:String) {
        _subPackageList.postValue(Event(Resource.Loading()))
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getSubscriptionPackage(tokenManager,sub_item_id)
                Log.i("TAG", "getSubscriptionPackageList: "+appRepository.getSubscriptionPackage(tokenManager,sub_item_id))
                _subPackageList.postValue(handleSubscriptionPackageResponse(response))
            } else {
                _subPackageList.postValue(
                    Event(
                        Resource.Error(getApplication<Application>().getString(
                            R.string.no_internet_connection)))
                )
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _subPackageList.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Application>().getString(
                                    R.string.network_failure
                                )
                            ))
                    )
                }
                else -> {
                    _subPackageList.postValue(
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

    private fun handleSubscriptionPackageResponse(response: Response<SubscriptionPackageModel>): Event<Resource<SubscriptionPackageModel>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }
}

