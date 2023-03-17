package com.example.newsapplication.viewmodel

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.newsapplication.MainActivity
import com.example.newsapplication.utils.Routes
import com.google.firebase.auth.FirebaseAuth

// ViewModel class of Authentication screen
class AuthenticationViewModel : ViewModel() {
    fun checkRegistration(
        context: Context,
        auth: FirebaseAuth,
        email: MutableState<String>,
        password: MutableState<String>
    ) {
        if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email.value, password.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        Toast.makeText(context, "User successful authorized", Toast.LENGTH_SHORT)
                            .show()
                    else
                        Toast.makeText(context, "User is already exist", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(
                context,
                "Please enter an email address and a password",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun checkAuthorized(
        context: Context,
        auth: FirebaseAuth,
        email: MutableState<String>,
        password: MutableState<String>,
        navController: NavHostController
    ) {
        if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email.value, password.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navController.navigate(Routes.HOME_SCREEN) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    } else
                        Toast.makeText(
                            context,
                            "Please check that your email address and password are correct",
                            Toast.LENGTH_SHORT
                        ).show()
                }
        } else {
            Toast.makeText(
                context, "Please enter an email address and a password", Toast.LENGTH_SHORT
            ).show()
        }
    }
}