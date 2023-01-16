package com.example.newsapplication.screens

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.newsapplication.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNotification(
    activity: Activity,
    context: Context,
    homeViewModel: HomeViewModel,
    navController: NavHostController,
) {
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
            imageVector = Icons.Default.Notifications,
            contentDescription = "newNotification",
            tint = MaterialTheme.colorScheme.surfaceTint
        )
        // Text to Display the current Screen
        Text(text = "Create new notification")
        // Text of notification
        val notificationText = remember { mutableStateOf("") }
        // OutlinedTextField to type the new notification
        OutlinedTextField(
            value = notificationText.value,
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            onValueChange = { newText -> notificationText.value = newText },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Type a notification text") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, // Keyboard type
                capitalization = KeyboardCapitalization.Sentences, // Letters type
                imeAction = ImeAction.Done // Keyboard action type
            )
        )
        // Button, to send notification
        Button(onClick = {
            // Check the notification text for emptiness
            if (notificationText.value.isNotEmpty()) {
                homeViewModel.createNotificationChannel(activity = activity)
                homeViewModel.createNotifications(
                    activity = activity,
                    context = context,
                    notificationText = notificationText
                )
                navController.navigate("home")
            } else
                Toast.makeText(context, "Type a notification text", Toast.LENGTH_SHORT).show()
        }, modifier = Modifier.padding(10.dp)) {
            Text(text = "Create notification")
        }
    }
}