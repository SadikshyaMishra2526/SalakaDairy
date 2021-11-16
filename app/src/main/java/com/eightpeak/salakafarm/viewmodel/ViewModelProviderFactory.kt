package com.eightpeak.salakafarm.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eightpeak.salakafarm.repository.AppRepository
import com.eightpeak.salakafarm.views.order.orderview.orderhistory.OrderHistory

class ViewModelProviderFactory(
    val app: Application,
    val appRepository: AppRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(app, appRepository) as T
        }

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(app, appRepository) as T
        }

        if (modelClass.isAssignableFrom(SliderViewModel::class.java)) {
            return SliderViewModel(app, appRepository) as T
        }

        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            return CategoriesViewModel(app, appRepository) as T
        }

        if (modelClass.isAssignableFrom(CategoriesByIdViewModel::class.java)) {
            return CategoriesByIdViewModel(app, appRepository) as T
        }

        if (modelClass.isAssignableFrom(ProductListViewModel::class.java)) {
            return ProductListViewModel(app, appRepository) as T
        }
        if (modelClass.isAssignableFrom(ProductByIdViewModel::class.java)) {
            return ProductByIdViewModel(app, appRepository) as T
        }

        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            return UserProfileViewModel(app, appRepository) as T
        }
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(app, appRepository) as T
        }

        if (modelClass.isAssignableFrom(GetResponseViewModel::class.java)) {
            return GetResponseViewModel(app, appRepository) as T
        }

        if (modelClass.isAssignableFrom(SubscriptionViewModel::class.java)) {
            return SubscriptionViewModel(app, appRepository) as T
        }
        if (modelClass.isAssignableFrom(CompareViewModel::class.java)) {
            return CompareViewModel(app, appRepository) as T
        }

        if (modelClass.isAssignableFrom(CheckOutViewModel::class.java)) {
            return CheckOutViewModel(app, appRepository) as T
        }
  if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            return OrderViewModel(app, appRepository) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }

}