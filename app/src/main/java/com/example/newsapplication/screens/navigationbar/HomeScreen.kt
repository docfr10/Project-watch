package com.example.newsapplication.screens.navigationbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.Card
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.newsapplication.model.notification.NotificationModel
import com.example.newsapplication.utils.Routes.NEW_NOTIFICATION_SCREEN
import com.example.newsapplication.viewmodel.HomeViewModel
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

// Markup of the "Home" screen
@Composable
fun HomeScreen(
    navController: NavHostController,
    notificationList: State<List<NotificationModel>>,
    homeViewModel: HomeViewModel
) {
    Scaffold(content = { padding ->
        // Column Composable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(padding),
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
            // Notification list markup
            NotificationList(
                homeViewModel = homeViewModel,
                notificationList = notificationList
            )
        }
    }, floatingActionButton = {
        // Button to go to creating notifications
        FloatingActionButton(shape = CircleShape, onClick = {
            navController.navigate(NEW_NOTIFICATION_SCREEN)
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add a new notification")
        }
    })
}

@Composable
fun NotificationList(
    notificationList: State<List<NotificationModel>>,
    homeViewModel: HomeViewModel
) {
    LazyColumn {
        items(notificationList.value) {
            val deleteNotification = SwipeAction(
                onSwipe = { homeViewModel.deleteNotification(it) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete project"
                    )
                },
                background = MaterialTheme.colorScheme.error
            )

            SwipeableActionsBox(
                swipeThreshold = 200.dp,
                endActions = listOf(deleteNotification)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    shape = RoundedCornerShape(15.dp),
                    backgroundColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    elevation = 5.dp
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = it.notificationDate, fontSize = 25.sp)
                        Text(text = it.notificationTime, fontSize = 25.sp)
                        Text(text = it.notificationTitle, fontSize = 25.sp)
                    }
                }
            }
        }
    }
}


