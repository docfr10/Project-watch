package com.example.newsapplication.screens.navigationbar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        // Switch changed showing icon label
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "Icon labels at the bottom")
            Switch(
                checked = sharedPreference.getBoolean("show", true),
                onCheckedChange = {
                    sharedPreference.edit().putBoolean("show", it).apply()
                    context.startActivity(Intent(context, MainActivity::class.java))
                })
        }
    }
}