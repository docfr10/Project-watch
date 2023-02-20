package com.example.newsapplication.screens.navigationbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.newsapplication.R
import com.example.newsapplication.viewmodel.ProjectsViewModel

@Composable
fun ProjectsScreen(projectsViewModel: ProjectsViewModel) {
    // Column Composable,
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
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
}

@Composable
fun Stopwatch(projectsViewModel: ProjectsViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Project name")
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
            //Button(onClick = { projectsViewModel.start() }) { Text("Start") }
        }
    }
}
