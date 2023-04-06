package com.example.newsapplication.screens.navigationbar

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.newsapplication.model.project.ProjectModel
import com.example.newsapplication.utils.Routes.NEW_PROJECT_SCREEN
import com.example.newsapplication.utils.Routes.STOPWATCH_SCREEN
import com.example.newsapplication.viewmodel.NewProjectViewModel
import com.example.newsapplication.viewmodel.ProjectsViewModel
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun ProjectsScreen(
    projectsViewModel: ProjectsViewModel,
    navController: NavHostController,
    projectList: State<List<ProjectModel>>,
    newProjectViewModel: NewProjectViewModel,
    context: Context
) {
    Scaffold(content = { padding ->
        // Column Composable,
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
                imageVector = Icons.Default.Create,
                contentDescription = "projects",
                tint = MaterialTheme.colorScheme.surfaceTint
            )
            // Text to Display the current Screen
            Text(text = "Projects")
            // Projects list markup
            ProjectsList(
                context = context,
                newProjectViewModel = newProjectViewModel,
                projectsViewModel = projectsViewModel,
                projectList = projectList,
                navController = navController
            )
        }
    }, floatingActionButton = {
        // Button to go to creating notifications
        FloatingActionButton(shape = CircleShape, onClick = {
            navController.navigate(NEW_PROJECT_SCREEN)
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add a new notification")
        }
    })
}

@Composable
fun ProjectsList(
    projectsViewModel: ProjectsViewModel,
    projectList: State<List<ProjectModel>>,
    navController: NavHostController,
    newProjectViewModel: NewProjectViewModel,
    context: Context,
) {
    // Checking for permission to send notifications for Android 13+
    val hasNotificationPermission = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }
    // Launcher for checking permission to send notifications
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasNotificationPermission.value = isGranted }
    )
    LazyColumn {
        if (projectList.value.isNotEmpty())
            projectsViewModel.addProjectsToFirebase()

        items(projectList.value) {
            val setNewProjectName = SwipeAction(
                onSwipe = {
                    newProjectViewModel.setProjectName(name = it.projectName)
                    newProjectViewModel.setProjectId(id = it.id)
                    newProjectViewModel.setProjectTime(time = it.projectTime)
                    navController.navigate(NEW_PROJECT_SCREEN)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Set new project name"
                    )
                },
                background = MaterialTheme.colorScheme.surfaceTint
            )
            val deleteProject = SwipeAction(
                onSwipe = { projectsViewModel.deleteProject(it) },
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
                startActions = listOf(setNewProjectName),
                endActions = listOf(deleteProject)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = it.projectName)
                        Text(
                            text = projectsViewModel.formatTime(it.projectTime),
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Button(onClick = {
                            // Check the permission to send notifications
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            if (hasNotificationPermission.value) {
                                navController.navigate(STOPWATCH_SCREEN)
                                projectsViewModel.setProjectName(it.projectName)
                                projectsViewModel.setProjectId(it.id)
                            }
                        }) { Text(text = "Start") }
                    }
                }
            }
            Divider(color = MaterialTheme.colorScheme.onBackground, thickness = 1.dp)
        }
    }
}

