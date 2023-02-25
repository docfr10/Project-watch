package com.example.newsapplication

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.newsapplication.viewmodel.AuthenticationViewModel
import com.example.newsapplication.viewmodel.NewNotificationViewModel
import com.example.newsapplication.viewmodel.ProjectsViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    // Objects for working with Firebase
    private val auth = FirebaseAuth.getInstance()
    private val cUser = auth.currentUser

    // ViewModel objects
    private lateinit var newNotificationViewModel: NewNotificationViewModel
    private lateinit var authenticationViewModel: AuthenticationViewModel
    private lateinit var projectsViewModel: ProjectsViewModel

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

                // Saving a list with project data
                val projectList =
                    projectsViewModel.getReadAllProjects().observeAsState(initial = listOf())

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
                    sharedPreference = sharedPreference,
                    projectList = projectList,
                    auth = auth,
                    authenticationViewModel = authenticationViewModel,
                    newNotificationViewModel = newNotificationViewModel,
                    projectsViewModel = projectsViewModel,
                    window = window
                )
            }
        }
    }
}