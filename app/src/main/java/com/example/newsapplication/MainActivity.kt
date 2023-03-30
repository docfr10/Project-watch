package com.example.newsapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.example.newsapplication.screens.AppScreen
import com.example.newsapplication.screens.navigationbar.*
import com.example.newsapplication.ui.theme.NewsApplicationTheme
import com.example.newsapplication.viewmodel.*
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    // Objects for working with Firebase
    private val auth = FirebaseAuth.getInstance()
    private var cUser = auth.currentUser
    private val signInWithGoogleLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                Log.d("LogApiException", e.toString())
            }
        }

    // ViewModel objects
    private lateinit var newNotificationViewModel: NewNotificationViewModel
    private lateinit var authenticationViewModel: AuthenticationViewModel
    private lateinit var projectsViewModel: ProjectsViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var newProjectViewModel: NewProjectViewModel

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NewsApplicationTheme {
                val activity = LocalContext.current as Activity
                val context = LocalContext.current

                val provider = ViewModelProvider(this)
                newNotificationViewModel = provider[NewNotificationViewModel::class.java]
                authenticationViewModel = provider[AuthenticationViewModel::class.java]
                projectsViewModel = provider[ProjectsViewModel::class.java]
                homeViewModel = provider[HomeViewModel::class.java]
                newProjectViewModel = provider[NewProjectViewModel::class.java]

                // Saving a list with project data
                val projectList =
                    projectsViewModel.getReadAllProjects().observeAsState(initial = listOf())
                // Saving a list with notification data
                val notificationList =
                    homeViewModel.getReadAllNotifications().observeAsState(initial = listOf())

                // Loading projects from Firebase
                projectsViewModel.setProjectsToFirebase()

                // Remember navController so it does not
                // Get recreated on recomposition
                val navController = rememberAnimatedNavController()

                // Shared preference for app settings
                val sharedPreference = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)

                AppScreen(
                    activity = activity,
                    context = context,
                    cUser = cUser,
                    navController = navController,
                    signInWithGoogleLauncher = signInWithGoogleLauncher,
                    sharedPreference = sharedPreference,
                    projectList = projectList,
                    notificationList = notificationList,
                    homeViewModel = homeViewModel,
                    auth = auth,
                    authenticationViewModel = authenticationViewModel,
                    newProjectViewModel = newProjectViewModel,
                    newNotificationViewModel = newNotificationViewModel,
                    projectsViewModel = projectsViewModel,
                    window = window
                )
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                cUser = auth.currentUser
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}