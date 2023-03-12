package com.example.newsapplication.screens.separate

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.view.Window
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.newsapplication.model.notifications.NotificationModel
import com.example.newsapplication.utils.Routes.HOME_SCREEN
import com.example.newsapplication.viewmodel.HomeViewModel
import com.example.newsapplication.viewmodel.NewNotificationViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NewNotificationScreen(
    activity: Activity,
    context: Context,
    newNotificationViewModel: NewNotificationViewModel,
    navController: NavHostController,
    window: Window,
    sharedPreference: SharedPreferences,
    homeViewModel: HomeViewModel,
) {
    // Checking for permission to send notifications for Android 13+
    val hasNotificationPermission = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }
    // Launcher for checking permission to send notifications
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasNotificationPermission.value = isGranted }
    )
    // Text of notification
    val notificationText = rememberSaveable { mutableStateOf("") }
    // Title of notification
    val notificationTitle = rememberSaveable { mutableStateOf("") }
    // String for writing selected date
    val selectedDate = rememberSaveable { mutableStateOf("") }
    // String for writing selected time
    val selectedTime = rememberSaveable { mutableStateOf("") }
    // Raise the elements above the keyboard
    var shouldResize = false // False will resize
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(shouldResize)
        shouldResize = shouldResize.not()
    } else {
        if (shouldResize.not())
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        else
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

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
            onValueChange = { notificationTitle.value = it },
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
            onValueChange = { notificationText.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Type a notification text") },
            singleLine = false,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, // Keyboard type
                capitalization = KeyboardCapitalization.Sentences, // Letters type
                imeAction = ImeAction.Done // Keyboard action type
            )
        )
        // Date and Time creation function from datePicker and timePicker with dropdown menu
        ShowDataAndTimeDropdownMenu(
            context = context,
            newNotificationViewModel = newNotificationViewModel,
            selectedDate = selectedDate,
            selectedTime = selectedTime
        )
        // Button, to send notification
        Button(onClick = {
            // Check the notification text for emptiness
            if (notificationTitle.value.isNotEmpty()) {
                // Check the permission to send notifications
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                if (hasNotificationPermission.value) {
                    // Check Android version, if version >= Android 8 then create a notification channel
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        newNotificationViewModel.createNotificationChannel(activity = activity)
                        newNotificationViewModel.createNotification(
                            activity = activity,
                            context = context,
                            sharedPreference = sharedPreference,
                            notificationTitle = notificationTitle,
                            notificationText = notificationText
                        )
                        homeViewModel.addNotification(
                            notificationModel = NotificationModel(
                                notificationTitle = notificationTitle.value,
                                notificationText = notificationText.value,
                                notificationDate = selectedDate.value,
                                notificationTime = selectedTime.value
                            )
                        )
                    }
                    // Else don't create
                    else
                        newNotificationViewModel.createNotification(
                            activity = activity,
                            context = context,
                            sharedPreference = sharedPreference,
                            notificationTitle = notificationTitle,
                            notificationText = notificationText
                        )
                    navController.navigate(HOME_SCREEN)
                }
            } else
                Toast.makeText(context, "Type a notification text", Toast.LENGTH_SHORT).show()
        }) { Text(text = "Create notification") }
        // Cancel button
        Button(onClick = {
            navController.popBackStack()
            navController.navigate(HOME_SCREEN)
        }) {
            Text(text = "Cancel")
        }
    }
}

@Composable
fun ShowDataAndTimeDropdownMenu(
    context: Context,
    newNotificationViewModel: NewNotificationViewModel,
    selectedDate: MutableState<String>,
    selectedTime: MutableState<String>
) {
    // Initializing a Calendar
    val calendar = Calendar.getInstance()
    // Setting the current date
    calendar.time = Date()

    // Date format to display in OutlinedTextField
    val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    // Time format to display in OutlinedTextField
    val timeFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    // Checking whether the button is pressed in DropDownMenu for date
    val expandedDate = rememberSaveable { mutableStateOf(false) }
    // Checking whether the button is pressed in DropDownMenu for time
    val expandedTime = rememberSaveable { mutableStateOf(false) }

    // List with possible date
    val date = listOf("Today", "Tomorrow", "Another date")
    // List with possible time
    val time = listOf("Morning", "Afternoon", "Evening", "Another time")

    // Size of DropDownMenu for date
    val textDateFieldSize = remember { mutableStateOf(Size.Zero) }
    // Size of DropDownMenu for time
    val textTimeFieldSize = remember { mutableStateOf(Size.Zero) }

    // Icon in OutlinedTextField for date
    val iconForDatePicker = if (expandedDate.value)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    // Icon in OutlinedTextField for time
    val iconForTimePicker = if (expandedTime.value)
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