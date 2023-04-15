package com.example.newsapplication.screens.separate

import android.content.Context
import android.os.Build
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.newsapplication.R
import com.example.newsapplication.model.project.ProjectModel
import com.example.newsapplication.utils.Routes
import com.example.newsapplication.utils.Routes.PROJECTS_SCREEN
import com.example.newsapplication.viewmodel.NewProjectViewModel
import com.example.newsapplication.viewmodel.ProjectsViewModel

// Markup of the "New project" screen
@Composable
fun NewProjectScreen(
    navController: NavHostController,
    context: Context,
    window: Window,
    projectsViewModel: ProjectsViewModel,
    newProjectViewModel: NewProjectViewModel,
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

    Scaffold(topBar = {
        IconButton(onClick = {
            navController.popBackStack()
            navController.navigate(Routes.HOME_SCREEN)
        }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back button")
        }
    }, content = { padding ->
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
                contentDescription = "newProject",
                tint = MaterialTheme.colorScheme.surfaceTint
            )
            if (newProjectViewModel.getProjectName().isEmpty()) {
                // Text to Display the current Screen
                Text(text = context.getString(R.string.create_new_project))
                // OutlinedTextField to type the new project name
                OutlinedTextField(
                    value = projectName.value,
                    isError = projectName.value.isEmpty(),
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                    onValueChange = { projectName.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = context.getString(R.string.type_project_name)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, // Keyboard type
                        capitalization = KeyboardCapitalization.Sentences, // Letters type
                        imeAction = ImeAction.Done // Keyboard action type
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            // Check the project text for emptiness
                            if (projectName.value.isNotEmpty()) {
                                projectsViewModel.addProject(
                                    projectModel = ProjectModel(projectName = projectName.value)
                                )
                                navController.popBackStack()
                                navController.navigate(PROJECTS_SCREEN)
                            } else Toast.makeText(
                                context,
                                context.getText(R.string.type_project_name),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                )
                // Displaying information about required field
                if (projectName.value.isEmpty()) {
                    Text(
                        text = context.getString(R.string.required_field),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(end = 235.dp)
                    )
                }
                // Button, to create project
                Button(onClick = {
                    // Check the project text for emptiness
                    if (projectName.value.isNotEmpty()) {
                        projectsViewModel.addProject(
                            projectModel = ProjectModel(projectName = projectName.value)
                        )
                        navController.popBackStack()
                        navController.navigate(PROJECTS_SCREEN)
                    } else Toast.makeText(
                        context,
                        context.getString(R.string.type_project_name),
                        Toast.LENGTH_SHORT
                    ).show()
                }) { Text(text = context.getString(R.string.create)) }
            } else {
                // Text to Display the current Screen
                Text(text = context.getString(R.string.change_project_name))
                // OutlinedTextField to type the new project name
                OutlinedTextField(
                    value = projectName.value,
                    isError = projectName.value.isEmpty(),
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                    onValueChange = { projectName.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = context.getString(R.string.type_new_project_name)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, // Keyboard type
                        capitalization = KeyboardCapitalization.Sentences, // Letters type
                        imeAction = ImeAction.Done // Keyboard action type
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            // Check the project text for emptiness
                            if (projectName.value.isNotEmpty()) {
                                projectsViewModel.setNewProjectName(
                                    projectModel = ProjectModel(
                                        id = newProjectViewModel.getProjectId(),
                                        projectName = projectName.value,
                                        projectTime = newProjectViewModel.getProjectTime()
                                    )
                                )
                                newProjectViewModel.setProjectId(id = 0)
                                newProjectViewModel.setProjectName(name = "")
                                navController.popBackStack()
                                navController.navigate(PROJECTS_SCREEN)
                            } else Toast.makeText(
                                context,
                                context.getText(R.string.type_project_name),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                )
                // Displaying information about required field
                if (projectName.value.isEmpty()) {
                    Text(
                        text = context.getString(R.string.required_field),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(end = 235.dp)
                    )
                }
                // Button, to change the project name
                Button(onClick = {
                    // Check the project text for emptiness
                    if (projectName.value.isNotEmpty()) {
                        projectsViewModel.setNewProjectName(
                            projectModel = ProjectModel(
                                id = newProjectViewModel.getProjectId(),
                                projectName = projectName.value,
                                projectTime = newProjectViewModel.getProjectTime()
                            )
                        )
                        newProjectViewModel.setProjectId(id = 0)
                        newProjectViewModel.setProjectName(name = "")
                        navController.popBackStack()
                        navController.navigate(PROJECTS_SCREEN)
                    } else Toast.makeText(
                        context,
                        context.getText(R.string.type_project_name),
                        Toast.LENGTH_SHORT
                    ).show()
                }) { Text(text = context.getString(R.string.change)) }
            }
            // Cancel button
            Button(onClick = {
                newProjectViewModel.setProjectId(id = 0)
                newProjectViewModel.setProjectName(name = "")
                navController.popBackStack()
                navController.navigate(PROJECTS_SCREEN)
            }) { Text(text = context.getString(R.string.cancel)) }
            BackHandler(enabled = true) {
                newProjectViewModel.setProjectId(id = 0)
                newProjectViewModel.setProjectName(name = "")
                navController.popBackStack()
                navController.navigate(PROJECTS_SCREEN)
            }
        }
    })
}