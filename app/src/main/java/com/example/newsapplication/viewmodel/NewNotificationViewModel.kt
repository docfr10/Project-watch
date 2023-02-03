package com.example.newsapplication.viewmodel

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.newsapplication.R
import com.example.newsapplication.model.notifications.NotificationsModel
import java.util.*

// ViewModel class of Home screen
class NewNotificationViewModel : ViewModel() {
    private var notificationDateAndTime: Calendar = Calendar.getInstance()

    fun createNotifications(
        activity: Activity,
        context: Context,
        notificationText: MutableState<String>,
        sharedPreference: SharedPreferences,
        notificationTitle: MutableState<String>
    ) {
        val intent = Intent(context, NotificationsModel::class.java)
        // Remembering the date when it is necessary to send a notification
        val alarmManager =
            activity.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        // Passing the name and text of the notification to Notifications
        intent.putExtra(R.string.titleExtra.toString(), notificationTitle.value)
        intent.putExtra(R.string.messageExtra.toString(), notificationText.value)
        intent.putExtra(
            R.bool.setAutoCancel.toString(),
            sharedPreference.getBoolean("closeNotification", false)
        )

        // Creating a broadcast signal to send a notification
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            notificationDateAndTime.timeInMillis,
            pendingIntent
        )
        Log.d("CALENDAR", notificationDateAndTime.timeInMillis.toString())
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

    fun setDate(calendar: Calendar) {
        notificationDateAndTime.set(Calendar.YEAR, calendar[Calendar.YEAR])
        notificationDateAndTime.set(Calendar.MONTH, calendar[Calendar.MONTH])
        notificationDateAndTime.set(Calendar.DAY_OF_MONTH, calendar[Calendar.DAY_OF_MONTH])
    }

    fun setTime(calendar: Calendar) {
        notificationDateAndTime.set(Calendar.HOUR_OF_DAY, calendar[Calendar.HOUR_OF_DAY])
        notificationDateAndTime.set(Calendar.MINUTE, calendar[Calendar.MINUTE])
    }
}