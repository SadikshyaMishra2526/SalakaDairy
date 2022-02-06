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
import com.eightpeak.salakafarm.utils.subutils.Resource
import com.eightpeak.salakafarm.utils.subutils.Utils
import com.eightpeak.salakafarm.views.home.products.ServerResponse
import com.eightpeak.salakafarm.views.addtocart.addtocartfragment.CartResponse
import com.eightpeak.salakafarm.views.gallery.GalleryListModel
import com.eightpeak.salakafarm.views.home.products.Data
import com.eightpeak.salakafarm.views.home.products.ForgotPasswordResponse
import com.eightpeak.salakafarm.views.pages.PageDetailsModel
import com.eightpeak.salakafarm.views.popup.PopUpModel
import com.eightpeak.salakafarm.views.wishlist.WishListResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class GetResponseViewModel (
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val cartResponse: MutableLiveData<Resource<CartResponse>> = MutableLiveData()
    fun getCartResponse(tokenManager: TokenManager) = viewModelScope.launch {
        cartResponseFetch(tokenManager)
    }
    private suspend fun cartResponseFetch(tokenManager: TokenManager) {
        cartResponse.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response =appRepository.getCartList(tokenManager)
                cartResponse.postValue(handleCartResponse(response))
            } else {
                cartResponse.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "cartResponseFetch: "+t.localizedMessage)
            when (t) {

                is IOException -> cartResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> cartResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleCartResponse(response: Response<CartResponse>): Resource<CartResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }





    val wishListResponse: MutableLiveData<Resource<WishListResponse>> = MutableLiveData()

    fun getWishListResponse(tokenManager: TokenManager) = viewModelScope.launch {
        wishlistResponseFetch(tokenManager)
    }
    private suspend fun wishlistResponseFetch(tokenManager: TokenManager) {
        wishListResponse.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response =appRepository.getWishList(tokenManager)
                wishListResponse.postValue(handleWishListResponse(response))
            } else {
                wishListResponse.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> wishListResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> wishListResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleWishListResponse(response: Response<WishListResponse>): Resource<WishListResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



//    add to cart


    val addToCart: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()
    fun addToCart(tokenManager: TokenManager, productId:String,qty:String,options:String) = viewModelScope.launch {
        addToCartById(tokenManager,productId,qty,options)
    }


    private suspend fun addToCartById(tokenManager: TokenManager, productId:String,qty:String,options:String) {
        addToCart.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.addToCart(tokenManager,productId,qty,options)
                Log.i("TAG", "fetchPics: $response")
                addToCart.postValue(handleAddToCartResponse(response))
            } else {
                addToCart.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> addToCart.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> addToCart.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleAddToCartResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



    //    delete wishlist
val deleteWishlistById: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun deleteWishlistById(tokenManager: TokenManager, productId:String) = viewModelScope.launch {
        deleteViewListItem(tokenManager,productId)
    }


    private suspend fun deleteViewListItem(tokenManager: TokenManager, productId:String) {
        deleteWishlistById.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.deleteWishListItem(tokenManager,productId)
//                Log.i("TAG", "fetchPics: $response")
                deleteWishlistById.postValue(handledeleteWishlistByIdResponse(response))
            } else {
                deleteWishlistById.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> deleteWishlistById.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> deleteWishlistById.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handledeleteWishlistByIdResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        Log.i("TAG", "fetchPics: "+response)

        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    //    delete all wishlist
    val deleteWishlist: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun deleteAllWish(tokenManager: TokenManager) = viewModelScope.launch {
        deleteWishlist(tokenManager)
    }

    suspend fun deleteWishlist(tokenManager: TokenManager) {
        deleteWishlist.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.deleteWishListAll(tokenManager)
                Log.i("TAG", "fetchPics: $response")
                deleteWishlist.postValue(handledeleteWishlistResponse(response))
            } else {
                deleteWishlist.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> deleteWishlist.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> deleteWishlist.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handledeleteWishlistResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }




    //    get Page Title
    val getPageDetails: MutableLiveData<Resource<PageDetailsModel>> = MutableLiveData()

    fun getPageDetailsView(pageId:String) = viewModelScope.launch {
        fetchPageDetails(pageId)
    }


    private suspend fun fetchPageDetails( pageId:String) {
        getPageDetails.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getPageDetails(pageId)
                Log.i("TAG", "fetchPics: $response")
                getPageDetails.postValue(handleGetPageDetailsResponse(response))
            } else {
                getPageDetails.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> getPageDetails.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> getPageDetails.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleGetPageDetailsResponse(response: Response<PageDetailsModel>): Resource<PageDetailsModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    //    get Random Title
    val getRandomListResponse: MutableLiveData<Resource<List<Data>>> = MutableLiveData()

    fun getRandomListResponseView() = viewModelScope.launch {
        fetchRandomDetails()
    }


    private suspend fun fetchRandomDetails() {
        getRandomListResponse.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getRandomList()
                Log.i("TAG", "fetchPics: $response")
                getRandomListResponse.postValue(handleRandomListDetails(response))
            } else {
                getRandomListResponse.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> getRandomListResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> getRandomListResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleRandomListDetails(response: Response<List<Data>>): Resource<List<Data>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



    //    get updateCart
    val updateCartResponse: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun updateCartResponseView(tokenManager: TokenManager, productId:String, QTY:String) = viewModelScope.launch {
        updateCartDetails(tokenManager,productId,QTY)
    }


    private suspend fun updateCartDetails(
        tokenManager: TokenManager,
        productId: String,
        QTY: String
    ) {
        updateCartResponse.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.updateCart(tokenManager,productId,QTY)
                Log.i("TAG", "fetchPics: $response")
                updateCartResponse.postValue(handleUpdateCartDetails(response))
            } else {
                updateCartResponse.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> updateCartResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> updateCartResponse.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleUpdateCartDetails(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }




    //    delete wishlist
    val deleteCart: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun deleteCart(tokenManager: TokenManager) = viewModelScope.launch {
        deleteCartItem(tokenManager)
    }


    private suspend fun deleteCartItem(tokenManager: TokenManager) {
        deleteCart.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.deleteCart(tokenManager)
                Log.i("TAG", "fetchPics: $response")
                deleteCart.postValue(handledeleteCartResponse(response))
            } else {
                deleteCart.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> deleteCart.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> deleteCart.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handledeleteCartResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



    //    addComplain
    val addComplain: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun addComplain(tokenManager: TokenManager,body: RequestBodies.AddComplain) = viewModelScope.launch {
        addComplainItem(tokenManager,body)
    }


    private suspend fun addComplainItem(tokenManager: TokenManager,body: RequestBodies.AddComplain) {
        addComplain.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.addComplain(tokenManager,body)
                Log.i("TAG", "fetchPics: $response")
                addComplain.postValue(handleAddComplainResponse(response))
            } else {
                addComplain.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> addComplain.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> addComplain.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleAddComplainResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    val deleteCartItem: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun deleteCartItemById(tokenManager: TokenManager, productId:String) = viewModelScope.launch {
        deleteCart(tokenManager,productId)
    }


    private suspend fun deleteCart(tokenManager: TokenManager, productId:String) {
        deleteCartItem.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.deleteCartItem(tokenManager,productId)
                Log.i("TAG", "fetchPics: $response")
                deleteCartItem.postValue(handledeleteCartItemResponse(response))
            } else {
                deleteCartItem.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> deleteCartItem.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> deleteCartItem.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handledeleteCartItemResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


//    get popup view
val getPopUp: MutableLiveData<Resource<PopUpModel>> = MutableLiveData()

    fun getPopUpBanner(tokenManager: TokenManager) = viewModelScope.launch {
        getPopUP(tokenManager)
    }


    private suspend fun getPopUP(tokenManager: TokenManager) {
        getPopUp.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getPopUp(tokenManager)
                Log.i("TAG", "fetchPics: $response")
                getPopUp.postValue(handleGetPopUpResponse(response))
            } else {
                getPopUp.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getPopUp.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> getPopUp.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleGetPopUpResponse(response: Response<PopUpModel>): Resource<PopUpModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



//    get Gallery view
val getGalleryDetails: MutableLiveData<Resource<GalleryListModel>> = MutableLiveData()

    fun getGalleryDetailsBanner() = viewModelScope.launch {
        getPopUP()
    }


    private suspend fun getPopUP() {
        getGalleryDetails.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.getGallery()
                Log.i("TAG", "fetchPics: $response")
                getGalleryDetails.postValue(handleGetGalleryResponse(response))
            } else {
                getGalleryDetails.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getGalleryDetails.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> getGalleryDetails.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleGetGalleryResponse(response: Response<GalleryListModel>): Resource<GalleryListModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }





//    add Contact Us
    val contactUs: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()

    fun addContactUs(body: RequestBodies.AddContactUs) = viewModelScope.launch {
        addContactUsData(body)
    }
    private suspend fun addContactUsData(body: RequestBodies.AddContactUs) {
        addComplain.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.addContactUs(body)
                Log.i("TAG", "fetchPics: $response")
                addComplain.postValue(handleAddContactUsResponse(response))
            } else {
                addComplain.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> addComplain.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> addComplain.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleAddContactUsResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

//forgot password


    //    add Contact Us
    val forgotPassword: MutableLiveData<Resource<ForgotPasswordResponse>> = MutableLiveData()

    fun forgotPassword(email: String) = viewModelScope.launch {
        forgotPasswordView(email)
    }
    private suspend fun forgotPasswordView(email: String) {
        forgotPassword.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.forgotPassword(email)
                Log.i("TAG", "fetchPics: $response")
                forgotPassword.postValue(handleForgotPasswordResponse(response))
            } else {
                forgotPassword.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            Log.i("TAG", "fetchPics: "+t.localizedMessage)
            when (t) {
                is IOException -> forgotPassword.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> forgotPassword.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleForgotPasswordResponse(response: Response<ForgotPasswordResponse>): Resource<ForgotPasswordResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


//    add to wishlist

    val wishlist: MutableLiveData<Resource<ServerResponse>> = MutableLiveData()
    fun addtowishlist(tokenManager: TokenManager, productId:String) = viewModelScope.launch {
        wishlistByView(tokenManager,productId)
    }
    private suspend fun wishlistByView(tokenManager: TokenManager, productId:String) {
        wishlist.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<Application>())) {
                val response = appRepository.addToWishList(tokenManager,productId)
                Log.i("TAG", "fetchPics: $response")
                wishlist.postValue(handleAddToWishListResponse(response))
            } else {
                wishlist.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {

            when (t) {
                is IOException -> wishlist.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> wishlist.postValue(
                    Resource.Error(
                        getApplication<Application>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleAddToWishListResponse(response: Response<ServerResponse>): Resource<ServerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }else{
            Log.i("TAG", "handleWishListResponse:$response ")
        }
        return Resource.Error(response.message())
    }




}