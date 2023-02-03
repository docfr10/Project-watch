package com.example.newsapplication.screens.separate

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.view.Window
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import com.example.newsapplication.viewmodel.NewNotificationViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNotificationScreen(
    activity: Activity,
    context: Context,
    newNotificationViewModel: NewNotificationViewModel,
    navController: NavHostController,
    window: Window,
    sharedPreference: SharedPreferences,
) {
    // Text of notification
    val notificationText = remember { mutableStateOf("") }
    // Title of notification
    val notificationTitle = remember { mutableStateOf("") }
    // Raise the elements above the keyboard
    window.setDecorFitsSystemWindows(false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(MaterialTheme.colorScheme.background)
            .imePadding(),
        // Parameters set to place the items in center
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon Composable
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "newNotification",
            tint = MaterialTheme.colorScheme.surfaceTint
        )
        // Text to Display the current Screen
        Text(text = "Create new notification")
        // OutlinedTextField to type the new notification title
        OutlinedTextField(
            value = notificationTitle.value,
            isError = notificationTitle.value.isEmpty(),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            onValueChange = { newText -> notificationTitle.value = newText },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Type a notification title") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, // Keyboard type
                capitalization = KeyboardCapitalization.Sentences, // Letters type
                imeAction = ImeAction.Next // Keyboard action type
            )
        )
        // Displaying information about required field
        if (notificationTitle.value.isEmpty()) {
            Text(
                text = "Required field",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(end = 235.dp)
            )
        }
        // OutlinedTextField to type the new notification text
        OutlinedTextField(
            value = notificationText.value,
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            onValueChange = { newText -> notificationText.value = newText },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Type a notification text") },
            singleLine = false,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, // Keyboard type
                capitalization = KeyboardCapitalization.Sentences, // Letters type
                imeAction = ImeAction.Next // Keyboard action type
            )
        )
        // Date and Time creation function from datePicker and timePicker with dropdown menu
        ShowDataAndTimeDropdownMenu(
            context = context,
            newNotificationViewModel = newNotificationViewModel
        )
        // Button, to send notification
        Button(onClick = {
            // Check the notification text for emptiness
            if (notificationTitle.value.isNotEmpty()) {
                newNotificationViewModel.createNotificationChannel(activity = activity)
                newNotificationViewModel.createNotifications(
                    activity = activity,
                    context = context,
                    sharedPreference = sharedPreference,
                    notificationTitle = notificationTitle,
                    notificationText = notificationText
                )
                navController.navigate("home")
            } else
                Toast.makeText(context, "Type a notification text", Toast.LENGTH_SHORT).show()
        }) { Text(text = "Create notification") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDataAndTimeDropdownMenu(
    context: Context,
    newNotificationViewModel: NewNotificationViewModel
) {
    // Initializing a Calendar
    val calendar = Calendar.getInstance()
    // Setting the current date
    calendar.time = Date()

    val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val expandedTime = remember { mutableStateOf(false) }
    val expandedDate = remember { mutableStateOf(false) }

    val time = listOf("Morning", "Afternoon", "Evening", "Another time")
    val date = listOf("Today", "Tomorrow", "Another date")

    val selectedTime = remember { mutableStateOf("") }
    val selectedDate = remember { mutableStateOf("") }

    val textTimeFieldSize = remember { mutableStateOf(Size.Zero) }
    val textDateFieldSize = remember { mutableStateOf(Size.Zero) }

    val iconForTimePicker = if (expandedTime.value)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val iconForDatePicker = if (expandedDate.value)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // DropdownMenu with date
        Box(modifier = Modifier.size(width = 165.dp, height = 85.dp)) {
            OutlinedTextField(
                value = selectedDate.value,
                isError = selectedDate.value.isEmpty(),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                singleLine = true,
                onValueChange = { selectedDate.value = it },
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        // This value is used to assign to the DropDown the same width
                        textDateFieldSize.value = coordinates.size.toSize()
                    },
                label = { Text(text = "Type a date") },
                trailingIcon = {
                    Icon(
                        iconForDatePicker,
                        "contentDescription",
                        Modifier.clickable { expandedDate.value = !expandedDate.value })
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, // Keyboard type
                    imeAction = ImeAction.Next // Keyboard action type
                )
            )
            DropdownMenu(
                expanded = expandedDate.value,
                onDismissRequest = { expandedDate.value = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textDateFieldSize.value.width.toDp() })
                    .fillMaxWidth(0.5f)
            ) {
                date.forEach { label ->
                    DropdownMenuItem(onClick = {
                        when (label) {
                            "Today" -> {
                                selectedDate.value = dateFormat.format(calendar.time).toString()
                                newNotificationViewModel.setDate(calendar = calendar)
                            }
                            "Tomorrow" -> {
                                calendar.add(Calendar.DAY_OF_MONTH, 1)
                                selectedDate.value = dateFormat.format(calendar.time).toString()
                                newNotificationViewModel.setDate(calendar = calendar)
                            }
                            "Another date" -> DatePickerDialog(
                                context,
                                { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                                    calendar.set(mYear, mMonth, mDayOfMonth)
                                    selectedDate.value = dateFormat.format(calendar.time).toString()
                                    newNotificationViewModel.setDate(calendar = calendar)
                                },
                                calendar[Calendar.YEAR],
                                calendar[Calendar.MONTH],
                                calendar[Calendar.DAY_OF_MONTH]
                            ).show()
                        }
                        expandedDate.value = false
                    }, text = { Text(text = label) })
                }
            }
            // Displaying information about required field
            if (selectedDate.value.isEmpty()) {
                Text(
                    text = "Required field",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 67.dp, start = 15.dp)
                )
            }
        }
        // DropdownMenu with time
        Box(modifier = Modifier.size(width = 165.dp, height = 85.dp)) {
            OutlinedTextField(
                value = selectedTime.value,
                isError = selectedTime.value.isEmpty(),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                singleLine = true,
                onValueChange = { selectedTime.value = it },
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        // This value is used to assign to the DropDown the same width
                        textTimeFieldSize.value = coordinates.size.toSize()
                    },
                label = { Text(text = "Type a time") },
                trailingIcon = {
                    Icon(
                        iconForTimePicker,
                        "contentDescription",
                        Modifier.clickable { expandedTime.value = !expandedTime.value })
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, // Keyboard type
                    imeAction = ImeAction.Done // Keyboard action type
                )
            )
            DropdownMenu(
                expanded = expandedTime.value,
                onDismissRequest = { expandedTime.value = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textTimeFieldSize.value.width.toDp() })
                    .fillMaxWidth(0.5f)
            ) {
                time.forEach { label ->
                    DropdownMenuItem(onClick = {
                        when (label) {
                            "Morning" -> {
                                calendar.set(Calendar.HOUR_OF_DAY, 7)
                                calendar.set(Calendar.MINUTE, 0)
                                selectedTime.value = timeFormat.format(calendar.time).toString()
                                newNotificationViewModel.setTime(calendar = calendar)
                            }
                            "Afternoon" -> {
                                calendar.set(Calendar.HOUR_OF_DAY, 13)
                                calendar.set(Calendar.MINUTE, 0)
                                selectedTime.value = timeFormat.format(calendar.time).toString()
                                newNotificationViewModel.setTime(calendar = calendar)
                            }
                            "Evening" -> {
                                calendar.set(Calendar.HOUR_OF_DAY, 19)
                                calendar.set(Calendar.MINUTE, 0)
                                selectedTime.value = timeFormat.format(calendar.time).toString()
                                newNotificationViewModel.setTime(calendar = calendar)
                            }
                            "Another time" -> {
                                TimePickerDialog(
                                    context,
                                    0,
                                    { _, mHour: Int, mMinute: Int ->
                                        calendar.set(Calendar.HOUR_OF_DAY, mHour)
                                        calendar.set(Calendar.MINUTE, mMinute)
                                        selectedTime.value =
                                            timeFormat.format(calendar.time).toString()
                                        newNotificationViewModel.setTime(calendar = calendar)
                                    },
                                    calendar[Calendar.HOUR_OF_DAY],
                                    calendar[Calendar.MINUTE],
                                    true
                                ).show()
                            }
                        }
                        expandedTime.value = false
                    }, text = { Text(text = label) })
                }
            }
            // Displaying information about required fields
            if (selectedTime.value.isEmpty()) {
                Text(
                    text = "Required field",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 67.dp, start = 15.dp)
                )
            }
        }
    }
}