package com.example.newsapplication.viewmodel

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.newsapplication.R
import com.example.newsapplication.notifications.NotificationsBuilderModel
import java.util.*

// ViewModel class of Home screen
class NewNotificationViewModel : ViewModel() {
    // Calendar for saving notification date and time
    private var notificationDateAndTime: Calendar = Calendar.getInstance()

    fun createNotification(
        activity: Activity,
        context: Context,
        notificationText: MutableState<String>,
        sharedPreference: SharedPreferences,
        notificationTitle: MutableState<String>
    ) {
        val intent = Intent(context, NotificationsBuilderModel::class.java)
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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(activity: Activity) {
        val notificationManager =
            activity.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

        val name = "Notification Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(R.string.channelID.toString(), name, importance)
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