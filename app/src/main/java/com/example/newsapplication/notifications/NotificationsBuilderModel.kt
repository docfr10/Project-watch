package com.example.newsapplication.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.newsapplication.*

// Class responsible for creating notifications
class NotificationsBuilderModel : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Defining the notification ID like a current time
        val notificationID = System.currentTimeMillis().toInt()
        // Creating a login Intent in the app
        val intent1 = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        // Creating a PendingIntent to login to the app
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_IMMUTABLE)
        // Creating a Notification
        val notification = NotificationCompat.Builder(context, R.string.channelIDNotify.toString())
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(intent.getStringExtra(R.string.titleExtra.toString()))
            .setContentText(intent.getStringExtra(R.string.messageExtra.toString()))
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentIntent(pendingIntent)
            .setAutoCancel(intent.getBooleanExtra(R.bool.setAutoCancel.toString(), false))
            .build()
        // Sending a notification to a channel
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)
    }
}