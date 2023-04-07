package com.example.newsapplication.model.notification

import androidx.lifecycle.LiveData

// A class that connects the database and the code
class NotificationRepositoryModel(private val notificationDAOModel: NotificationDAOModel) {
    fun readAllNotifications(): LiveData<List<NotificationModel>> {
        return notificationDAOModel.readAllNotifications()
    }

    fun addNotification(notificationModel: NotificationModel) {
        notificationDAOModel.addNotification(notificationModel = notificationModel)
    }

    fun deleteNotification(notificationModel: NotificationModel) {
        notificationDAOModel.deleteNotification(notificationModel = notificationModel)
    }
}