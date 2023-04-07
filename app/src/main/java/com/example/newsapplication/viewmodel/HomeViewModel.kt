package com.example.newsapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapplication.model.AppDatabaseModel
import com.example.newsapplication.model.notification.NotificationModel
import com.example.newsapplication.model.notification.NotificationRepositoryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// ViewModel class of Home screen
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val notificationDAOModel =
        AppDatabaseModel.getDatabase(context = application).notificationDAO()
    private val repositoryModel =
        NotificationRepositoryModel(notificationDAOModel = notificationDAOModel)
    private val readAllNotifications: LiveData<List<NotificationModel>> =
        repositoryModel.readAllNotifications()

    fun getReadAllNotifications(): LiveData<List<NotificationModel>> {
        return readAllNotifications
    }

    fun addNotification(notificationModel: NotificationModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryModel.addNotification(notificationModel = notificationModel)
        }
    }

    fun deleteNotification(notificationModel: NotificationModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryModel.deleteNotification(notificationModel = notificationModel)
        }
    }
}