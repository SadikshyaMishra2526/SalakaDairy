package com.eightpeak.salakafarm.repository

import androidx.lifecycle.LiveData
import com.eightpeak.salakafarm.database.NotificationDao
import com.eightpeak.salakafarm.database.NotificationDetails

class NotificationRepository(private val loggerDao:NotificationDao) {

    val getLoggerDetails: LiveData<List<NotificationDetails>> = loggerDao.getAllDetailsOfLog()


   suspend fun deleteDetails(id:String){
        loggerDao.deleteByUserId(id)
    }
   suspend fun addLoggerDetails(dailyLogger:NotificationDetails){
        loggerDao.addLogDetails(dailyLogger)
    }
}