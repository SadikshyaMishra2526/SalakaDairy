package com.eightpeak.salakafarm.database

import androidx.lifecycle.LiveData

class LoggerRepository(private val loggerDao:NotificationDao) {

    val getLoggerDetails: LiveData<List<NotificationDetails>> = loggerDao.getAllDetailsOfLog()

   suspend fun addLoggerDetails(dailyLogger:NotificationDetails){
        loggerDao.addLogDetails(dailyLogger)
    }
}