package com.example.newsapplication.screens

import android.app.*
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.newsapplication.viewmodel.HomeViewModel


// Markup of the "Home" screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    activity: Activity,
    context: Context,
    homeViewModel: HomeViewModel,
    navController: NavHostController
) {
    // Text of notification
    val notificationText = remember { mutableStateOf("") }

    // Column Composable,
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp),
        // Parameters set to place the items in center
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon Composable
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "home",
            tint = MaterialTheme.colorScheme.surfaceTint
        )
        // Text to Display the current Screen
        Text(text = "Home")
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
            } else
                Toast.makeText(context, "Type a notification text", Toast.LENGTH_SHORT).show()
        }, modifier = Modifier.padding(10.dp)) {
            Text(text = "Send notification")
        }
        // TODO - FIX
        BackHandler(enabled = true) {
            navController.navigate("splashScreen") // ТО КУДА
        }
    }
}


