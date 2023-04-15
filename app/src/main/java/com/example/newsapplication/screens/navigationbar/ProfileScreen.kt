package com.example.newsapplication.screens.navigationbar

import android.content.Context
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.newsapplication.R
import com.example.newsapplication.utils.Routes.AUTHENTICATION_SCREEN
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

// Markup of the "Profile" screen
@Composable
fun ProfileScreen(
    navController: NavHostController,
    context: Context,
    cUser: FirebaseUser?,
    auth: FirebaseAuth
) {
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
        Text(text = context.getString(R.string.you_logged))
        Text(text = "${cUser?.email}")
        // Button to logout
        Button(onClick = {
            when (touchCounter.value) {
                0 -> {
                    Toast
                        .makeText(
                            context,
                            context.getString(R.string.click_again),
                            Toast.LENGTH_SHORT
                        )
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
        }, modifier = Modifier.padding(5.dp)) { Text(text = context.getString(R.string.log_out)) }
    }
}
