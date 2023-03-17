package com.example.newsapplication.screens.navigationbar

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.newsapplication.utils.Routes.AUTHENTICATION_SCREEN
import com.google.firebase.auth.FirebaseAuth

// Markup of the "Profile" screen
@Composable
fun ProfileScreen(
    auth: FirebaseAuth,
    navController: NavHostController
) {
    val cUser = auth.currentUser
    val context = LocalContext.current

    val touchCounter = rememberSaveable { mutableStateOf(0) }

    // Column Composable
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        // parameters set to place the items in center
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon Composable
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "profile",
            tint = MaterialTheme.colorScheme.surfaceTint,
        )
        // Text to Display the current Screen
        Text(text = "You are logged in as: ${cUser?.email}")
        // Button to logout
        Button(onClick = {
            when (touchCounter.value) {
                0 -> {
                    Toast
                        .makeText(context, "Click again to log out", Toast.LENGTH_SHORT)
                        .show()
                    touchCounter.value++
                }
                1 -> {
                    auth.signOut()
                    navController.navigate(AUTHENTICATION_SCREEN) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            }
        }, modifier = Modifier.padding(5.dp)) { Text(text = "Log out") }
    }
}
