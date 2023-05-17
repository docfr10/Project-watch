package com.example.newsapplication

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import com.example.newsapplication.viewmodel.AuthenticationViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`

class AuthenticationViewModelTest {
    private lateinit var auth: FirebaseAuth
    private lateinit var context: Context
    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        auth = mock(FirebaseAuth::class.java)
        context = mock(Context::class.java)
        navController = mock(NavHostController::class.java)
    }

    @Test
    fun checkRegistration_withValidEmailAndPassword() {
        val email = mutableStateOf("test@gmail.com")
        val password = mutableStateOf("password123")
        val viewModel = AuthenticationViewModel()

        val authResult = mock(Task::class.java)
        `when`(auth.createUserWithEmailAndPassword(email.value, password.value))
            .thenReturn(authResult as Task<AuthResult>?)

        viewModel.checkRegistration(context, auth, email, password)

        verify(auth, times(1)).createUserWithEmailAndPassword(email.value, password.value)
        verifyNoMoreInteractions(auth)
    }

    @Test
    fun checkAuthorization_withValidEmailAndPassword() {
        val email = mutableStateOf("test@gmail.com")
        val password = mutableStateOf("password123")
        val viewModel = AuthenticationViewModel()

        val authResult = mock(Task::class.java)
        `when`(auth.signInWithEmailAndPassword(email.value, password.value))
            .thenReturn(authResult as Task<AuthResult>?)

        viewModel.checkAuthorized(context, auth, email, password)

        verify(auth, times(1)).signInWithEmailAndPassword(email.value, password.value)
        verifyNoMoreInteractions(auth)
    }
}