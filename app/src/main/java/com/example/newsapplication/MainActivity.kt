package com.example.newsapplication

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.BottomAppBarDefaults.containerColor
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.newsapplication.screens.navigationbar.*
import com.example.newsapplication.screens.separate.AnimatedSplashScreen
import com.example.newsapplication.screens.separate.AuthenticationScreen
import com.example.newsapplication.screens.separate.NewNotification
import com.example.newsapplication.ui.theme.NewsApplicationTheme
import com.example.newsapplication.utils.Constants
import com.example.newsapplication.viewmodel.AuthenticationViewModel
import com.example.newsapplication.viewmodel.HomeViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    // Objects for working with Firebase
    private val auth = FirebaseAuth.getInstance()
    private val cUser = auth.currentUser

    // ViewModel objects
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var authenticationViewModel: AuthenticationViewModel

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsApplicationTheme {
                val activity = LocalContext.current as Activity
                val context = LocalContext.current

                val provider = ViewModelProvider(this)
                homeViewModel = provider[HomeViewModel::class.java]
                authenticationViewModel = provider[AuthenticationViewModel::class.java]

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
                    auth = auth,
                    authenticationViewModel = authenticationViewModel,
                    homeViewModel = homeViewModel,
                    window = window
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    navController: NavHostController,
    auth: FirebaseAuth,
    homeViewModel: HomeViewModel,
    activity: Activity,
    context: Context,
    authenticationViewModel: AuthenticationViewModel,
    window: Window,
    cUser: FirebaseUser?,
    sharedPreference: SharedPreferences
) {
    // Hiding the bottom bar
    val isShowBottomBar = remember { mutableStateOf(false) }

    Surface(color = MaterialTheme.colorScheme.surface) {
        // Scaffold Component
        Scaffold(
            // Bottom navigation
            bottomBar = {
                if (isShowBottomBar.value) BottomNavigationBar(
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
                    sharedPreference = sharedPreference,
                    padding = padding,
                    auth = auth,
                    authenticationViewModel = authenticationViewModel,
                    homeViewModel = homeViewModel,
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
    homeViewModel: HomeViewModel,
    activity: Activity,
    context: Context,
    window: Window,
    authenticationViewModel: AuthenticationViewModel,
    cUser: FirebaseUser?,
    sharedPreference: SharedPreferences
) {
    AnimatedNavHost(
        navController = navController,
        // Set the start destination as splash screen
        startDestination = "splashScreen",
        // Set the padding provided by scaffold
        modifier = Modifier.padding(paddingValues = padding),
        builder = {
            // route : Splash screen
            composable(
                route = "splashScreen",
                exitTransition = { scaleOut(animationSpec = tween(500)) }) {
                AnimatedSplashScreen(navController = navController, cUser = cUser)
                isShowBottomBar.value = false
            }
            // route : Authentication
            composable(route = "authentication") {
                AuthenticationScreen(
                    context = context,
                    navController = navController,
                    window = window,
                    authenticationViewModel = authenticationViewModel,
                    auth = auth
                )
            }
            // route : Home
            composable(route = "home") {
                HomeScreen(navController = navController)
                isShowBottomBar.value = true
            }
            // route : Work
            composable(route = "projects") {
                ProjectsScreen()
                isShowBottomBar.value = true
            }
            // route : Profile
            composable(route = "profile") {
                ProfileScreen(auth = auth)
                isShowBottomBar.value = true
            }
            // route : About
            composable(route = "about") {
                AboutScreen()
                isShowBottomBar.value = true
            }
            // route : Settings
            composable(route = "settings") {
                SettingsScreen(
                    context = context,
                    sharedPreference = sharedPreference
                )
                isShowBottomBar.value = true
            }
            // route : New notification
            composable(
                route = "newNotification",
                enterTransition = { slideInVertically(animationSpec = tween(250)) },
                exitTransition = { slideOutVertically(animationSpec = tween(250)) }
            ) {
                NewNotification(
                    activity = activity,
                    navController = navController,
                    context = context,
                    homeViewModel = homeViewModel,
                    window = window
                )
                isShowBottomBar.value = false
            }
        })
}

// Output of all screen icons
@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    sharedPreference: SharedPreferences
) {
    NavigationBar(
        // Set background color
        containerColor = NavigationBarDefaults.containerColor,
        contentColor = MaterialTheme.colorScheme.contentColorFor(containerColor),
        tonalElevation = NavigationBarDefaults.Elevation,
    ) {
        // Observe the backstack
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        // Observe current route to change the icon
        // Color,label color when navigated
        val currentRoute = navBackStackEntry?.destination?.route
        // Bottom nav items we declared
        Constants.BottomNavItems.forEach { navItem ->
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
                alwaysShowLabel = sharedPreference.getBoolean("show", true)
            )
        }
    }
}