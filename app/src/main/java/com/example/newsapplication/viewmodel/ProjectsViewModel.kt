package com.example.newsapplication.viewmodel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapplication.model.AppDatabaseModel
import com.example.newsapplication.model.project.ProjectModel
import com.example.newsapplication.model.project.ProjectRepositoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*

class ProjectsViewModel(application: Application) : AndroidViewModel(application) {
    private val databaseReference =
        FirebaseDatabase.getInstance().getReference("USERS/${FirebaseAuth.getInstance().uid}")

    private val projectDAOModel = AppDatabaseModel.getDatabase(context = application).projectDAO()
    private val repositoryModel = ProjectRepositoryModel(projectDAOModel = projectDAOModel)
    private val readAllProjects: LiveData<List<ProjectModel>> = repositoryModel.readAllProjects()

    private var timeMillis = 0L
    private var lastTimestamp = 0L
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

    fun setProjectTime(time: Long) {
        projectTime = time
    }

    fun getProjectTime(): Long {
        return projectTime
    }

    fun setTime(time: Long) {
        projectTime = time
        timeMillis = time
    }

    fun addProjectsToFirebase() {
        viewModelScope.launch(Dispatchers.IO) {
            readAllProjects.value!!.forEach {
                databaseReference.child(it.id.toString()).setValue(it)
            }
        }
    }

    fun setProjectsToFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (projectSnapshot in snapshot.children) {
                    val project = projectSnapshot.getValue(ProjectModel::class.java)
                    viewModelScope.launch(Dispatchers.IO) {
                        if (project != null) {
                            projectDAOModel.addProject(
                                ProjectModel(
                                    project.id,
                                    project.projectName,
                                    project.projectTime
                                )
                            )
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
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
            databaseReference.child(projectModel.id.toString()).removeValue()
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