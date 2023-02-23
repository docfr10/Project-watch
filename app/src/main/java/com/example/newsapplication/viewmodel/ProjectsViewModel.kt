package com.example.newsapplication.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
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

    private var coroutineScope = CoroutineScope(Dispatchers.Main)

    private var formattedTime = mutableStateOf("00:00:00")
    private var isActive = mutableStateOf(false)

    private var timeMillis = 0L
    private var lastTimestamp = 0L

    fun getReadAllProjects(): LiveData<List<ProjectModel>> {
        return readAllProjects
    }

    fun addProject(projectModel: ProjectModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryModel.addProject(projectModel = projectModel)
        }
    }

    private fun formatTime(timeMillis: Long): String {
        val seconds = timeMillis / 1000 % 60
        val minutes = timeMillis / 60000 % 60
        val hours = timeMillis / 3600000
        return "%02d".format(hours) + ":" + "%02d".format(minutes) + ":" + "%02d".format(seconds)
    }

    fun getIsActive(): Boolean {
        return isActive.value
    }

    fun getFormattedTime(): String {
        return formattedTime.value
    }

    fun start() {
        if (isActive.value) return

        coroutineScope.launch {
            lastTimestamp = System.currentTimeMillis()
            this@ProjectsViewModel.isActive.value = true
            while (this@ProjectsViewModel.isActive.value) {
                delay(10L)
                timeMillis += System.currentTimeMillis() - lastTimestamp
                lastTimestamp = System.currentTimeMillis()
                formattedTime.value = formatTime(timeMillis)
            }
        }
    }

    fun pause() {
        isActive.value = false
    }
}