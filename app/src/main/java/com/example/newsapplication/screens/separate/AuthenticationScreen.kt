package com.example.newsapplication.screens.separate

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.Window
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.newsapplication.R
import com.example.newsapplication.viewmodel.AuthenticationViewModel
import com.google.firebase.auth.FirebaseAuth

// Markup of the "Authentication" screen
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun AuthenticationScreen(
    auth: FirebaseAuth,
    authenticationViewModel: AuthenticationViewModel,
    window: Window,
    context: Context,
    signInWithGoogleLauncher: ActivityResultLauncher<Intent>
) {
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

    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(MaterialTheme.colorScheme.background)
            .imePadding(),
        // parameters set to place the items in center
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon Composable
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "authentication",
            tint = MaterialTheme.colorScheme.surfaceTint
        )
        // Text to Display the current Screen
        Text(
            text = context.getString(R.string.authentication),
            color = MaterialTheme.colorScheme.onSurface
        )
        // OutlinedTextField to type the Email
        OutlinedTextField(
            value = email.value,
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            onValueChange = { email.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp)
                .background(MaterialTheme.colorScheme.background),
            label = { Text(text = context.getString(R.string.email)) },
            placeholder = {
                Text(
                    text = context.getString(R.string.example_mail),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        // OutlinedTextField to type the password
        OutlinedTextField(
            value = password.value,
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            onValueChange = { password.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp)
                .background(MaterialTheme.colorScheme.background),
            label = { Text(text = context.getString(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
        // Registration button
        Button(
            onClick = {
                // Check the registration
                authenticationViewModel.checkRegistration(
                    context = context,
                    auth = auth,
                    email = email,
                    password = password
                )
            },
            modifier = Modifier
                .padding(top = 10.dp, start = 5.dp, end = 5.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge
        ) { Text(text = context.getString(R.string.registered)) }
        // SignIn button
        Button(
            onClick = { // Authorized user login
                authenticationViewModel.checkAuthorized(
                    context = context,
                    auth = auth,
                    email = email,
                    password = password,
                )
            },
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge
        ) { Text(text = context.getString(R.string.sign_in)) }
        // SignIn with Google button
        Button(
            onClick = { // Authorized user with Google login
                authenticationViewModel.getClient(
                    context = context,
                    signInWithGoogleLauncher = signInWithGoogleLauncher
                )
            },
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icons8_google),
                contentDescription = "Google logo"
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(text = context.getString(R.string.google_sign_in))
        }
    }
}