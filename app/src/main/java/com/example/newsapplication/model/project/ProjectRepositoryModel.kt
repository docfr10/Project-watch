package com.example.newsapplication.model.project

import androidx.lifecycle.LiveData

//A class that connects the database and the code
class ProjectRepositoryModel(private val projectDAOModel: ProjectDAOModel) {
    fun readAllProjects(): LiveData<List<ProjectModel>> {
        return projectDAOModel.readAllProjects()
    }

    fun addProject(projectModel: ProjectModel) {
        projectDAOModel.addProject(projectModel = projectModel)
    }

    fun setNewProjectName(projectModel: ProjectModel) {
        projectDAOModel.setNewProjectName(projectModel = projectModel)
    }

    fun setProjectTime(id: Int, newProjectTime: Long) {
        projectDAOModel.setProjectTime(id = id, newProjectTime = newProjectTime)
    }

    fun deleteProject(projectModel: ProjectModel) {
        projectDAOModel.deleteProject(projectModel = projectModel)
    }
}