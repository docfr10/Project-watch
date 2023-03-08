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

    private var projectID: Int = 0
    private var projectName: String = ""
    private var projectTime: Long = 0L

    fun setProjectName(name: String) {
        projectName = name
    }

    fun getProjectName(): String {
        return projectName
    }

    fun setProjectId(id: Int) {
        projectID = id
    }

    fun getProjectId(): Int {
        return projectID
    }

    fun setTime(time:Long) {
        projectTime = time
    }

    fun getTime(): Long {
        return projectTime
    }

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

    fun setProjectTime(id: Int, newProjectTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryModel.setProjectTime(id = id, newProjectTime = newProjectTime)
        }
    }

    fun deleteProject(projectModel: ProjectModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryModel.deleteProject(projectModel = projectModel)
        }
    }

    fun formatTime(timeMillis: Long): String {
        projectTime = timeMillis
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

        var timeMillis = 0L
        var lastTimestamp = 0L

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