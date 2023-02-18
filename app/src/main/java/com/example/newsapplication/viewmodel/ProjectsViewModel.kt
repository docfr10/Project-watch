package com.example.newsapplication.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ProjectsViewModel : ViewModel() {
    private var formattedTime = mutableStateOf("00:00:00")
    private var coroutineScope = CoroutineScope(Dispatchers.Main)

    private var timeMillis = 0L
    private var lastTimestamp = 0L
    private var isActive = false

    private fun formatTime(timeMillis: Long): String {
        val timeFormat: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return timeFormat.format(timeMillis).toString()
    }

    fun getFormattedTime(): String {
        return formattedTime.value
    }

    fun start() {
        if (isActive) return

        coroutineScope.launch {
            lastTimestamp = System.currentTimeMillis()
            this@ProjectsViewModel.isActive = true
            while (this@ProjectsViewModel.isActive) {
                delay(10L)
                timeMillis += System.currentTimeMillis() - lastTimestamp
                lastTimestamp = System.currentTimeMillis()
                formattedTime.value = formatTime(timeMillis)
            }
        }
    }

    fun pause() {
        isActive = false
    }

    fun reset() {
        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
        timeMillis = 0L
        lastTimestamp = 0L
        formattedTime.value = "00:00:00"
        isActive = false
    }
}