package com.example.newsapplication.model.project

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity "Project"
@Entity(tableName = "project_table")
data class ProjectModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val projectName: String,
    val projectTime: Long = 0L
)