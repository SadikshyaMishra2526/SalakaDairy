package com.eightpeak.salakafarm.repository

import androidx.lifecycle.LiveData
import com.eightpeak.salakafarm.database.NotificationDetails
import com.eightpeak.salakafarm.serverconfig.RequestBodies
import com.eightpeak.salakafarm.serverconfig.RetrofitInstance
import com.eightpeak.salakafarm.serverconfig.network.TokenManager

class AppRepository {

    suspend fun loginUser(body: RequestBodies.LoginBody) =
        RetrofitInstance.useApiWithoutToken.loginCustomer(body)

    suspend fun registerUser(body: RequestBodies.RegisterBody) =
        RetrofitInstance.useApiWithoutToken.registerCustomer(body)


    suspend fun fetchSlider() = RetrofitInstance.useApiWithoutToken.getSlider()

    suspend fun getCategoriesList() = RetrofitInstance.useApiWithoutToken.getCategoriesList()


    suspend fun getCategoriesListById(id: String) =
        RetrofitInstance.useApiWithoutToken.getCategoriesById(id)

    suspend fun getProductList() = RetrofitInstance.useApiWithoutToken.getProductList()


    suspend fun getProductListById(id: String) =
        RetrofitInstance.useApiWithoutToken.getProductById(id)


    suspend fun getUserDetails(tokenManager: TokenManager) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).userDetails()


    suspend fun getSearchResponse(keyboard: String, filterSort: String) =
        RetrofitInstance.useApiWithoutToken.getSearchResponse(keyboard, filterSort)


    suspend fun addToCart(
        tokenManager: TokenManager,
        product_id: String,
        qty: String,
        options: String
    ) = RetrofitInstance.useApiWithAccessToken(tokenManager).addToCart(product_id, qty, options)


    suspend fun addToWishList(tokenManager: TokenManager, product_id: String) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).addToWishList(product_id)


    suspend fun getCartList(tokenManager: TokenManager) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).getCartList()

    suspend fun getWishList(tokenManager: TokenManager) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).getWishList()

    suspend fun deleteWishListItem(tokenManager: TokenManager, product_id: String) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).deleteWishlistItem(product_id)


    suspend fun deleteWishListAll(tokenManager: TokenManager) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).deleteWishlist()

    suspend fun deleteCartItem(tokenManager: TokenManager, product_id: String) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).deleteCartItem(product_id)


    suspend fun deleteCart(tokenManager: TokenManager) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).deleteCart()

    suspend fun getCompareProduct(tokenManager: TokenManager, productId: String) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).getCompareProduct(productId)


    suspend fun getBranchList(tokenManager: TokenManager) =
        RetrofitInstance.useApiWithoutToken.getBranches()

    suspend fun getSubscriptionItemList(tokenManager: TokenManager) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).getSubscriptionItem()

    suspend fun getSubscriptionPackage(tokenManager: TokenManager, sub_item_id: Int) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).getSubscriptionPackage(sub_item_id)

    suspend fun addSubscription(tokenManager: TokenManager, body: RequestBodies.AddSubscription) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).addSubscription(body)


    suspend fun getCheckoutDetails(tokenManager: TokenManager) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).getCheckoutDetails()


    suspend fun getAddressList(tokenManager: TokenManager) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).getAddressList()

    suspend fun getUserProfile(tokenManager: TokenManager, body: RequestBodies.UserProfile) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).getUserProfile(body)


    suspend fun updateAddressList(
        tokenManager: TokenManager,
        body: RequestBodies.UpdateAddressList
    ) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).updateAddressList(body)

    suspend fun updatePassword(tokenManager: TokenManager, body: RequestBodies.UpdatePassword) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).updatePassword(body)

    suspend fun getOrderList(tokenManager: TokenManager) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).getOrderHistoryList()


    suspend fun getOrderHistoryDetails(tokenManager: TokenManager, id: String) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).getOrderHistoryDetails(id)


    suspend fun getPageDetails(id: String) =
        RetrofitInstance.useApiWithoutToken.getPageDetails(id)


    suspend fun getRandomList() =
        RetrofitInstance.useApiWithoutToken.getRandomList()


    suspend fun updateCart(tokenManager: TokenManager, id: String, qty: String) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).updateCart(id, qty)


    suspend fun addComplain(tokenManager: TokenManager, body: RequestBodies.AddComplain) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).addComplain(body)

    suspend fun addOrder(tokenManager: TokenManager, body: RequestBodies.AddOrder) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).addOrder(body)


    suspend fun addNewAddress(tokenManager: TokenManager, body: RequestBodies.AddAddress) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).addNewAddress(body)


    suspend fun deleteAddress(tokenManager: TokenManager, addressId: String) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).deleteAddress(addressId)


    suspend fun getPopUp(tokenManager: TokenManager) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).getPopUp()


    suspend fun getEmpLatLng(tokenManager: TokenManager, body: RequestBodies.EmpLatlng) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).getEmployeeLatLng(body)

    suspend fun getCustomerSubscription(tokenManager: TokenManager) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).getCustomerSubscription()


    suspend fun subscriptionAlteration(tokenManager: TokenManager, body: RequestBodies.AddAlteration) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).subscriptionAlteration(body)


    suspend fun getProductRating(product_id: String) =
        RetrofitInstance.useApiWithoutToken.getRate(product_id)

}