package com.eightpeak.salakafarm.repository

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


    suspend fun getCompareProduct(productId: List<String>) =
        RetrofitInstance.useApiWithoutToken.getCompareProduct(productId)



    suspend fun getBranchList(tokenManager: TokenManager) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).getBranches()

    suspend fun getSubscriptionItemList(tokenManager: TokenManager) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).getSubscriptionItem()

    suspend fun getSubscriptionPackage(tokenManager: TokenManager, sub_item_id: String) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).getSubscriptionPackage(sub_item_id)

    suspend fun addSubscription(tokenManager: TokenManager , body: RequestBodies.AddSubscription) =
        RetrofitInstance.useApiWithAccessToken(tokenManager).addSubscription(body)


}