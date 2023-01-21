package com.example.newsapplication.viewmodel

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.newsapplication.R
import com.example.newsapplication.model.notifications.NotificationsModel

// ViewModel class of Home screen
class NewNotificationViewModel : ViewModel() {
    fun createNotifications(
        activity: Activity,
        context: Context,
        notificationText: MutableState<String>
    ) {
        val title = "New notification" // Title of notification
        val intent = Intent(context, NotificationsModel::class.java)
        // Remembering the date when it is necessary to send a notification
        val alarmManager =
            activity.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        // Passing the name and text of the notification to Notifications
        intent.putExtra(R.string.titleExtra.toString(), title)
        intent.putExtra(R.string.messageExtra.toString(), notificationText.value)

        // Creating a broadcast signal to send a notification
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                System.currentTimeMillis().toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 5000,
            pendingIntent
        )
    }

    fun createNotificationChannel(activity: Activity) {
        val notificationManager =
            activity.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

        val name = "Notification Channel"
        val desc = "A Description of the Channel"
        val importance = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationManager.IMPORTANCE_HIGH
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(R.string.channelID.toString(), name, importance)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        channel.enableVibration(true)
        channel.description = desc
        notificationManager.createNotificationChannel(channel)
    }
}