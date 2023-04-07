package com.example.newsapplication.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.newsapplication.MainActivity
import com.example.newsapplication.R

class StopwatchService : Service() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val manager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancelAll()
    }

    override fun onStartCommand(intent: Intent?, _flags: Int, startId: Int): Int {
        // Creating a login Intent in the app
        val intent1 = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        // Creating a PendingIntent to login to the app
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_IMMUTABLE)
        // Creating a Notification
        val notification = NotificationCompat.Builder(this, R.string.channelIDService.toString())
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(intent?.getStringExtra("projectName"))
            .setContentText(intent?.getStringExtra("projectTime"))
            .setDefaults(Notification.DEFAULT_ALL)
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setAutoCancel(false)
            .build()
        // Sending a notification to a channel
        val manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1000, notification)
        return super.onStartCommand(intent, _flags, startId)
    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(stopwatchService: StopwatchService) {
        val notificationManager =
            stopwatchService.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

        val name = "Notification Channel for stopwatch"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(R.string.channelIDService.toString(), name, importance)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        channel.enableVibration(false)
        channel.setSound(null, null)
        channel.description = desc
        notificationManager.createNotificationChannel(channel)
    }
}