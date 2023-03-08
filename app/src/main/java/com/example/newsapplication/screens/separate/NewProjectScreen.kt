package com.example.newsapplication.screens.separate

import android.content.Context
import android.os.Build
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.newsapplication.model.project.ProjectModel
import com.example.newsapplication.utils.Routes.PROJECTS_SCREEN
import com.example.newsapplication.viewmodel.ProjectsViewModel

@Composable
fun NewProjectScreen(
    navController: NavHostController,
    context: Context,
    window: Window,
    projectsViewModel: ProjectsViewModel,
) {
    // Project name
    val projectName = rememberSaveable { mutableStateOf("") }
    // Raise the elements above the keyboard
    var shouldResize = false // False will resize
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(shouldResize)
        shouldResize = shouldResize.not()
    } else {
        if (shouldResize.not())
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        else
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(MaterialTheme.colorScheme.background)
            .imePadding(),
        // Parameters set to place the items in center
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon Composable
        Icon(
            imageVector = Icons.Default.Create,
            contentDescription = "newNotification",
            tint = MaterialTheme.colorScheme.surfaceTint
        )
        // Text to Display the current Screen
        Text(text = "Create new project")
        // OutlinedTextField to type the new project name
        OutlinedTextField(
            value = projectName.value,
            isError = projectName.value.isEmpty(),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            onValueChange = { newText -> projectName.value = newText },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Type a project name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, // Keyboard type
                capitalization = KeyboardCapitalization.Sentences, // Letters type
                imeAction = ImeAction.Done // Keyboard action type
            )
        )
        // Displaying information about required field
        if (projectName.value.isEmpty()) {
            Text(
                text = "Required field",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(end = 235.dp)
            )
        }
        // Button, to create project
        Button(onClick = {
            // Check the notification text for emptiness
            if (projectName.value.isNotEmpty()) {
                projectsViewModel.addProject(
                    projectModel = ProjectModel(
                        0,
                        projectName = projectName.value
                    )
                )
                navController.navigate(PROJECTS_SCREEN)
            } else Toast.makeText(context, "Type a project name", Toast.LENGTH_SHORT).show()
        }) { Text(text = "Create") }
        // Cancel button
        Button(onClick = {
            navController.popBackStack()
            navController.navigate(PROJECTS_SCREEN)
        }) {
            Text(text = "Cancel")
        }
    }
}