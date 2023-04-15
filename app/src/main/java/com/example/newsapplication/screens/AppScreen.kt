package com.example.newsapplication.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.view.Window
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.newsapplication.R
import com.example.newsapplication.model.navigationbar.BottomNavItemModel
import com.example.newsapplication.model.notification.NotificationModel
import com.example.newsapplication.model.project.ProjectModel
import com.example.newsapplication.screens.navigationbar.*
import com.example.newsapplication.screens.separate.*
import com.example.newsapplication.utils.Routes.ABOUT_SCREEN
import com.example.newsapplication.utils.Routes.AUTHENTICATION_SCREEN
import com.example.newsapplication.utils.Routes.HOME_SCREEN
import com.example.newsapplication.utils.Routes.NEW_NOTIFICATION_SCREEN
import com.example.newsapplication.utils.Routes.NEW_PROJECT_SCREEN
import com.example.newsapplication.utils.Routes.PROFILE_SCREEN
import com.example.newsapplication.utils.Routes.PROJECTS_SCREEN
import com.example.newsapplication.utils.Routes.SETTINGS_SCREEN
import com.example.newsapplication.utils.Routes.SPLASH_SCREEN
import com.example.newsapplication.utils.Routes.STOPWATCH_SCREEN
import com.example.newsapplication.viewmodel.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun AppScreen(
    navController: NavHostController,
    auth: FirebaseAuth,
    newNotificationViewModel: NewNotificationViewModel,
    activity: Activity,
    context: Context,
    authenticationViewModel: AuthenticationViewModel,
    window: Window,
    cUser: FirebaseUser?,
    sharedPreference: SharedPreferences,
    projectsViewModel: ProjectsViewModel,
    projectList: State<List<ProjectModel>>,
    notificationList: State<List<NotificationModel>>,
    homeViewModel: HomeViewModel,
    signInWithGoogleLauncher: ActivityResultLauncher<Intent>,
    newProjectViewModel: NewProjectViewModel,
) {
    // Hiding the bottom bar
    val isShowBottomBar = remember { mutableStateOf(false) }

    Surface(color = MaterialTheme.colorScheme.surface) {
        // Scaffold Component
        Scaffold(
            // Bottom navigation
            bottomBar = {
                if (isShowBottomBar.value) BottomNavigationBar(
                    context = context,
                    navController = navController,
                    sharedPreference = sharedPreference
                )
            },
            content = { padding ->
                NavHostContainer(
                    activity = activity,
                    context = context,
                    cUser = cUser,
                    navController = navController,
                    signInWithGoogleLauncher = signInWithGoogleLauncher,
                    sharedPreference = sharedPreference,
                    padding = padding,
                    projectList = projectList,
                    notificationList = notificationList,
                    homeViewModel = homeViewModel,
                    auth = auth,
                    authenticationViewModel = authenticationViewModel,
                    newProjectViewModel = newProjectViewModel,
                    newNotificationViewModel = newNotificationViewModel,
                    projectsViewModel = projectsViewModel,
                    isShowBottomBar = isShowBottomBar,
                    window = window
                )
            }
        )
    }
}

// Screen Navigation
@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.R)
@Composable
private fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues,
    auth: FirebaseAuth,
    isShowBottomBar: MutableState<Boolean>,
    newNotificationViewModel: NewNotificationViewModel,
    activity: Activity,
    context: Context,
    window: Window,
    authenticationViewModel: AuthenticationViewModel,
    cUser: FirebaseUser?,
    sharedPreference: SharedPreferences,
    projectsViewModel: ProjectsViewModel,
    projectList: State<List<ProjectModel>>,
    notificationList: State<List<NotificationModel>>,
    homeViewModel: HomeViewModel,
    signInWithGoogleLauncher: ActivityResultLauncher<Intent>,
    newProjectViewModel: NewProjectViewModel,
) {
    AnimatedNavHost(
        navController = navController,
        // Set the start destination as splash screen
        startDestination = SPLASH_SCREEN,
        // Set the padding provided by scaffold
        modifier = Modifier.padding(paddingValues = padding),
        builder = {
            // route : Splash screen
            composable(
                route = SPLASH_SCREEN,
                exitTransition = { scaleOut(animationSpec = tween(500)) }) {
                AnimatedSplashScreen(navController = navController, cUser = cUser)
                isShowBottomBar.value = false
            }
            // route : Authentication
            composable(
                route = AUTHENTICATION_SCREEN,
                enterTransition = { slideInVertically(animationSpec = tween(250)) },
                exitTransition = { slideOutVertically(animationSpec = tween(250)) }
            ) {
                AuthenticationScreen(
                    context = context,
                    navController = navController,
                    signInWithGoogleLauncher = signInWithGoogleLauncher,
                    window = window,
                    authenticationViewModel = authenticationViewModel,
                    auth = auth
                )
                isShowBottomBar.value = false
            }
            // route : Home
            composable(route = HOME_SCREEN) {
                HomeScreen(
                    context = context,
                    navController = navController,
                    homeViewModel = homeViewModel,
                    notificationList = notificationList
                )
                isShowBottomBar.value = true
            }
            // route : Projects
            composable(route = PROJECTS_SCREEN) {
                ProjectsScreen(
                    context = context,
                    newProjectViewModel = newProjectViewModel,
                    projectsViewModel = projectsViewModel,
                    projectList = projectList,
                    navController = navController
                )
                isShowBottomBar.value = true
            }
            // route : Profile
            composable(route = PROFILE_SCREEN) {
                ProfileScreen(
                    auth = auth,
                    cUser = cUser,
                    context = context,
                    navController = navController
                )
                isShowBottomBar.value = true
            }
            // route : About
            composable(route = ABOUT_SCREEN) {
                AboutScreen(context = context)
                isShowBottomBar.value = true
            }
            // route : Settings
            composable(route = SETTINGS_SCREEN) {
                SettingsScreen(
                    context = context,
                    sharedPreference = sharedPreference
                )
                isShowBottomBar.value = true
            }
            // route : New notification
            composable(
                route = NEW_NOTIFICATION_SCREEN,
                enterTransition = { slideInVertically(animationSpec = tween(250)) },
                exitTransition = { slideOutVertically(animationSpec = tween(250)) }
            ) {
                NewNotificationScreen(
                    activity = activity,
                    navController = navController,
                    homeViewModel = homeViewModel,
                    sharedPreference = sharedPreference,
                    context = context,
                    newNotificationViewModel = newNotificationViewModel,
                    window = window
                )
                isShowBottomBar.value = false
            }
            // route : New project
            composable(
                route = NEW_PROJECT_SCREEN,
                enterTransition = { slideInVertically(animationSpec = tween(250)) },
                exitTransition = { slideOutVertically(animationSpec = tween(250)) }
            ) {
                NewProjectScreen(
                    newProjectViewModel = newProjectViewModel,
                    navController = navController,
                    projectsViewModel = projectsViewModel,
                    context = context,
                    window = window
                )
                isShowBottomBar.value = false
            }
            // route : Stopwatch
            composable(route = STOPWATCH_SCREEN,
                enterTransition = { slideInVertically(animationSpec = tween(250)) },
                exitTransition = { slideOutVertically(animationSpec = tween(250)) }
            ) {
                StopwatchScreen(
                    context = context,
                    navController = navController,
                    projectsViewModel = projectsViewModel
                )
                isShowBottomBar.value = false
            }
        })
}

// Output of all screen icons
@Composable
private fun BottomNavigationBar(
    navController: NavHostController,
    sharedPreference: SharedPreferences,
    context: Context
) {
    NavigationBar(
        // Set background color
        containerColor = NavigationBarDefaults.containerColor,
        contentColor = MaterialTheme.colorScheme.contentColorFor(BottomAppBarDefaults.containerColor),
        tonalElevation = NavigationBarDefaults.Elevation,
    ) {
        // An list containing information about all NavigationBar icons
        val bottomNavItems = listOf(
            BottomNavItemModel(
                label = context.getString(R.string.home),
                icon = Icons.Filled.Home,
                route = "home"
            ),
            BottomNavItemModel(
                label = context.getString(R.string.projects),
                icon = Icons.Filled.Create,
                route = "projects"
            ),
            BottomNavItemModel(
                label = context.getString(R.string.profile),
                icon = Icons.Filled.Person,
                route = "profile"
            ),
            BottomNavItemModel(
                label = context.getString(R.string.settings),
                icon = Icons.Filled.Settings,
                route = "settings"
            ),
            BottomNavItemModel(
                label = context.getString(R.string.about),
                icon = Icons.Filled.Info,
                route = "about"
            ),
        )
        // Observe the backstack
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        // Observe current route to change the icon
        // Color,label color when navigated
        val currentRoute = navBackStackEntry?.destination?.route
        // Bottom nav items we declared
        bottomNavItems.forEach { navItem ->
            // Place the bottom nav items
            NavigationBarItem(
                // It currentRoute is equal then its selected route
                selected = currentRoute == navItem.route,
                // Navigate on click
                onClick = { navController.navigate(navItem.route) },
                // Icon of navItem
                icon = { Icon(imageVector = navItem.icon, contentDescription = navItem.label) },
                // Label
                label = { Text(text = navItem.label) },
                alwaysShowLabel = sharedPreference.getBoolean("showIconLabels", true)
            )
        }
    }
}