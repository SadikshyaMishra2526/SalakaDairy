package com.eightpeak.salakafarm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.eightpeak.salakafarm.database.LoggerRepository
import com.eightpeak.salakafarm.database.NotificationDatabase
import com.eightpeak.salakafarm.database.NotificationDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application):AndroidViewModel(application) {

    val fetchAllData : LiveData<List<NotificationDetails>>
    private val repository : LoggerRepository
    init {
        val loggerDao= NotificationDatabase.getLoggerDatabase(application).loggerDao()
        repository = LoggerRepository(loggerDao)
        fetchAllData=repository.getLoggerDetails
    }

    fun addDailyLogger(logger: NotificationDetails){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLoggerDetails(logger)
        }
    }
}