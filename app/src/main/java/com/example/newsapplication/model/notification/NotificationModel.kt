package com.example.newsapplication.model.notification

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity "Notification"
@Entity(tableName = "notification_table")
data class NotificationModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val notificationTitle: String,
    val notificationText: String,
    val notificationDate: String,
    val notificationTime: String
)