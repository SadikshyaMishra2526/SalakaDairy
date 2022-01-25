package com.eightpeak.salakafarm.database.notifications

import android.app.Application
import androidx.lifecycle.*
import com.eightpeak.salakafarm.database.NotificationDatabase
import com.eightpeak.salakafarm.database.NotificationDetails
import com.eightpeak.salakafarm.repository.NotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

 class NotificationViewModel(application: Application): AndroidViewModel(application) {

      val fetchRegisterLogger : LiveData<List<NotificationDetails>>
    private val repository : NotificationRepository
    init {
        val loggerDao= NotificationDatabase.getLoggerDatabase(application).loggerDao()
        repository = NotificationRepository(loggerDao)
        fetchRegisterLogger=repository.getLoggerDetails
    }
    fun addNotificationDetails(notificationDetails: NotificationDetails){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLoggerDetails(notificationDetails)
        }
    }

     fun deleteNotificationDetails(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDetails(id)
        }
    }

}