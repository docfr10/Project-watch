package com.example.newsapplication.model.notification

import androidx.lifecycle.LiveData
import androidx.room.*

// Interface provides the methods that the rest of the app uses to interact with data in the "Notification" table
@Dao
interface NotificationDAOModel {
    @Query("SELECT * FROM notification_table")
    fun readAllNotifications(): LiveData<List<NotificationModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNotification(notificationModel: NotificationModel)

    @Delete
    fun deleteNotification(notificationModel: NotificationModel)
}