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
}