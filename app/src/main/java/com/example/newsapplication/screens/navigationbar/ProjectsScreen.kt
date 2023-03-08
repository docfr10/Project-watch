package com.example.newsapplication.screens.navigationbar

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.newsapplication.model.project.ProjectModel
import com.example.newsapplication.utils.Routes.NEW_PROJECT_SCREEN
import com.example.newsapplication.utils.Routes.STOPWATCH_SCREEN
import com.example.newsapplication.viewmodel.ProjectsViewModel
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun ProjectsScreen(
    projectsViewModel: ProjectsViewModel,
    navController: NavHostController,
    projectList: State<List<ProjectModel>>
) {
    Scaffold(content = { padding ->
        // Column Composable,
        Column(
            modifier = Modifier
                .fillMaxSize()
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
) {
    LazyColumn {
        items(projectList.value) {
            val setNewProjectName = SwipeAction(
                onSwipe = {
                    projectsViewModel.setProjectName(name = it.projectName)
                    projectsViewModel.setProjectId(id = it.id)
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
                            navController.navigate(STOPWATCH_SCREEN)
                            projectsViewModel.setProjectId(it.id)
                        }) {
                            Text(text = "Start")
                        }
                    }
                }
            }
        }
    }
}

