package com.example.newsapplication.screens.navigationbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.newsapplication.viewmodel.ProjectsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(projectsViewModel: ProjectsViewModel, navController: NavHostController) {
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
            // Stopwatch markup
            Stopwatch(projectsViewModel = projectsViewModel)
        }
    }, floatingActionButton = {
        // Button to go to creating notifications
        FloatingActionButton(shape = CircleShape, onClick = {
            navController.navigate("newProject")
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add a new notification")
        }
    })
}

@Composable
fun Stopwatch(projectsViewModel: ProjectsViewModel) {

    val projectList = projectsViewModel.getReadAllProjects().observeAsState(initial = listOf())

    LazyColumn {
        items(projectList.value) {
            Text(text = it.projectName)
        }
    }

    /*
    LazyColumn(content = {
        items(
            1
            //projectsViewModel.getReadAllProjects().value!!.size
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

                    //Text(text = projectsViewModel.getReadAllProjects().value!![0].projectName)

                    /*
                    Text(text = projectsViewModel.getReadAllProjects().value.toString())
                    Text(
                        text = projectsViewModel.getFormattedTime(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    if (!projectsViewModel.getIsActive())
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                            contentDescription = "Play",
                            modifier = Modifier.clickable { projectsViewModel.start() }
                        )
                    else
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_pause_24),
                            contentDescription = "Pause",
                            modifier = Modifier.clickable { projectsViewModel.pause() }
                        )

                     */
                }
            }
        }
    })
     */
}
