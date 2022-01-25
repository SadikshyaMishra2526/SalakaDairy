package com.eightpeak.salakafarm.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notification_details")
data class NotificationDetails(
    @PrimaryKey(autoGenerate = true)
    var id:Long,
    var notification_title:String,
    var notification_description:String,
    var notification_image:String,
    var notification_date:String,
)

