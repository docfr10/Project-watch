package com.example.newsapplication.model.project

import androidx.lifecycle.LiveData
import androidx.room.*

// Interface provides the methods that the rest of the app uses to interact with data in the project table
@Dao
interface ProjectDAOModel {
    @Query("SELECT * FROM project_table")
    fun readAllProjects(): LiveData<List<ProjectModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addProject(projectModel: ProjectModel)

    @Update
    fun setNewProjectName(projectModel: ProjectModel)

    @Delete
    fun deleteProject(projectModel: ProjectModel)
}