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

    fun setProjectTime(projectModel: ProjectModel) {
        projectDAOModel.setProjectTime(projectModel = projectModel)
    }

    fun deleteProject(projectModel: ProjectModel) {
        projectDAOModel.deleteProject(projectModel = projectModel)
    }
}