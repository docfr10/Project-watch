package com.example.newsapplication.viewmodel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapplication.model.AppDatabaseModel
import com.example.newsapplication.model.project.ProjectModel
import com.example.newsapplication.model.project.ProjectRepositoryModel
import kotlinx.coroutines.*

class ProjectsViewModel(application: Application) : AndroidViewModel(application) {
    private val projectDAOModel = AppDatabaseModel.getDatabase(context = application).projectDAO()
    private val repositoryModel = ProjectRepositoryModel(projectDAOModel = projectDAOModel)
    private val readAllProjects: LiveData<List<ProjectModel>> = repositoryModel.readAllProjects()

    var timeMillis = 0L
    var lastTimestamp = 0L

    fun getReadAllProjects(): LiveData<List<ProjectModel>> {
        return readAllProjects
    }

    fun addProject(projectModel: ProjectModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryModel.addProject(projectModel = projectModel)
        }
    }

    fun setNewProjectName(projectModel: ProjectModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryModel.setNewProjectName(projectModel = projectModel)
        }
    }

    fun setProjectTime(projectModel: ProjectModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryModel.setProjectTime(projectModel = projectModel)
        }
    }

    fun deleteProject(projectModel: ProjectModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryModel.deleteProject(projectModel = projectModel)
        }
    }

    private fun formatTime(timeMillis: Long): String {
        val seconds = timeMillis / 1000 % 60
        val minutes = timeMillis / 60000 % 60
        val hours = timeMillis / 3600000
        return "%02d".format(hours) + ":" + "%02d".format(minutes) + ":" + "%02d".format(seconds)
    }

    fun start(
        isActive: MutableState<Boolean>,
        formattedTime: MutableState<String>
    ) {
        if (isActive.value) return

        viewModelScope.launch(Dispatchers.IO) {
            lastTimestamp = System.currentTimeMillis()
            isActive.value = true
            while (isActive.value) {
                delay(10L)
                timeMillis += System.currentTimeMillis() - lastTimestamp
                lastTimestamp = System.currentTimeMillis()
                formattedTime.value = formatTime(timeMillis)
            }
        }
    }

    fun pause(isActive: MutableState<Boolean>) {
        isActive.value = false
    }
}