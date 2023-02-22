package com.example.newsapplication.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class ProjectsViewModel : ViewModel() {
    private var formattedTime = mutableStateOf("00:00:00")
    private var isActive = mutableStateOf(false)
    private var coroutineScope = CoroutineScope(Dispatchers.Main)

    private var timeMillis = 0L
    private var lastTimestamp = 0L

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