package com.example.newsapplication.viewmodel

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.newsapplication.R
import com.example.newsapplication.utils.Routes.HOME_SCREEN
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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
                        navController.navigate(HOME_SCREEN) {
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

    fun getClient(context: Context, signInWithGoogleLauncher: ActivityResultLauncher<Intent>) {
        val singInClient = getClient(context = context)
        signInWithGoogleLauncher.launch(singInClient.signInIntent)
    }

    private fun getClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }
}