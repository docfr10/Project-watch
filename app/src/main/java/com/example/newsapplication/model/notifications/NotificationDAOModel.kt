package com.example.newsapplication.model.notifications

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotificationDAOModel {
    @Query("SELECT * FROM notification_table")
    fun readAllNotifications(): LiveData<List<NotificationModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNotification(notificationModel: NotificationModel)

    @Delete
    fun deleteNotification(notificationModel: NotificationModel)
}