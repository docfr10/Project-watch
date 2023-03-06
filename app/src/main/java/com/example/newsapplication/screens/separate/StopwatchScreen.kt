package com.example.newsapplication.screens.separate

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.newsapplication.R
import com.example.newsapplication.model.project.ProjectModel
import com.example.newsapplication.utils.Routes.PROJECTS_SCREEN
import com.example.newsapplication.viewmodel.ProjectsViewModel

@Composable
fun StopwatchScreen(
    projectsViewModel: ProjectsViewModel,
    projectList: State<List<ProjectModel>>,
    navController: NavHostController
) {
    val isActive = remember { mutableStateOf(false) }
    val formattedTime = remember { mutableStateOf("00:00:00") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = formattedTime.value,
            fontWeight = FontWeight.Bold,
            fontSize = 50.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
        if (!isActive.value)
            Icon(
                painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                contentDescription = "Play",
                modifier = Modifier
                    .clickable {
                        projectsViewModel.start(isActive, formattedTime)
                    }
                    .size(30.dp)
            )
        else
            Icon(
                painter = painterResource(id = R.drawable.baseline_pause_24),
                contentDescription = "Pause",
                modifier = Modifier
                    .clickable {
                        projectsViewModel.pause(isActive = isActive)
                    }
                    .size(30.dp)
            )
    }
    BackHandler(enabled = true) {
        for (p in projectList.value) {
            projectsViewModel.setProjectTime(
                ProjectModel(
                    id = p.id,
                    projectName = p.projectName,
                    projectTime = formattedTime.value
                )
            )
        }
        navController.popBackStack()
        navController.navigate(PROJECTS_SCREEN)
    }
}