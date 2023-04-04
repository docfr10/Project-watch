package com.example.newsapplication.screens.separate

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.newsapplication.utils.Routes.AUTHENTICATION_SCREEN
import com.example.newsapplication.utils.Routes.HOME_SCREEN
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay

// Splash Screen
@Composable
fun AnimatedSplashScreen(navController: NavHostController, cUser: FirebaseUser?) {
    val startAnimation = remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation.value) 1f else 0f,
        animationSpec = tween(durationMillis = 3000)
    )

    LaunchedEffect(key1 = true) {
        startAnimation.value = true
        delay(4000)
        navController.popBackStack()
        if (cUser != null)
            navController.navigate(HOME_SCREEN)
        else
            navController.navigate(AUTHENTICATION_SCREEN)
    }
    SplashScreen(alphaAnim = alphaAnim.value)
}

@Composable
fun SplashScreen(alphaAnim: Float) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .size(120.dp)
                .alpha(alpha = alphaAnim),
            imageVector = Icons.Default.Create,
            contentDescription = "Logo icon",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}