package com.example.newsapplication.screens.navigationbar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newsapplication.MainActivity

// Layout of the settings screen
@Composable
fun SettingsScreen(
    sharedPreference: SharedPreferences,
    context: Context
) {
    // Column Composable,
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        // Parameters set to place the items in center
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon Composable
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "settings",
            tint = MaterialTheme.colorScheme.surfaceTint
        )
        // Text to Display the current Screen
        Text(text = "Settings")
        // Card with the function of changing the display of icon labels
        ShowIconLabels(context = context, sharedPreference = sharedPreference)
        // Card with the function of closing the notification when you click on it
        CloseNotificationOnPress(context = context, sharedPreference = sharedPreference)
    }
}

@Composable
fun CloseNotificationOnPress(context: Context, sharedPreference: SharedPreferences) {
    Card(
        modifier = Modifier.padding(5.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Close notification on press")
            Switch(
                checked = sharedPreference.getBoolean("closeNotification", false),
                onCheckedChange = {
                    sharedPreference.edit().putBoolean("closeNotification", it).apply()
                    context.startActivity(Intent(context, MainActivity::class.java))
                })
        }
    }}

@Composable
fun ShowIconLabels(sharedPreference: SharedPreferences, context: Context) {
    Card(
        modifier = Modifier.padding(5.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Icon labels at the bottom")
            Switch(
                checked = sharedPreference.getBoolean("showIconLabels", true),
                onCheckedChange = {
                    sharedPreference.edit().putBoolean("showIconLabels", it).apply()
                    context.startActivity(Intent(context, MainActivity::class.java))
                })
        }
    }
}
