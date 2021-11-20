package com.eightpeak.salakafarm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database( entities = [NotificationDetails::class],version = 1,exportSchema = false)

abstract class NotificationDatabase : RoomDatabase() {

    abstract fun loggerDao(): NotificationDao
    companion object{
        @Volatile
        private var INSTANCE :NotificationDatabase?=null
        fun getLoggerDatabase(context: Context):NotificationDatabase{
            var tempInstance= INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    NotificationDatabase::class.java,
                    "notification_database"
                ).build()
                INSTANCE=instance
                return instance
            }
        }
    }
}