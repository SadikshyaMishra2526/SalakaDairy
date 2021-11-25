package com.eightpeak.salakafarm.database

import androidx.lifecycle.LiveData

class NotificationRepository(private val notificationDao:NotificationDao) {

    val getLoggerDetails: LiveData<List<NotificationDetails>> = notificationDao.getAllDetailsOfLog()

   suspend fun addLoggerDetails(dailyLogger:NotificationDetails){
        notificationDao.addLogDetails(dailyLogger)
    }
}