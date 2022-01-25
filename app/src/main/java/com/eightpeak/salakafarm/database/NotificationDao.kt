package com.eightpeak.salakafarm.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotificationDao {


//   for storing and fetching users contains
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLogDetails(logger:NotificationDetails)

    @Query("SELECT * FROM notification_details ORDER BY id ASC")
    fun getAllDetailsOfLog():LiveData<List<NotificationDetails>>

    @Query("DELETE FROM notification_details WHERE id = :userId")
    fun deleteByUserId(userId: String)
}