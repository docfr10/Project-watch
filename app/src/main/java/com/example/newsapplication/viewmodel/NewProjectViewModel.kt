package com.example.newsapplication.viewmodel

import androidx.lifecycle.ViewModel

// ViewModel class of New project screen
class NewProjectViewModel : ViewModel() {
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
}