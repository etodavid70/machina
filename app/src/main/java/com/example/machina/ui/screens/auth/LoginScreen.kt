package com.example.machina.ui.screens.auth

import AppButton
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.R
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.widgets.AppPasswordField
import com.example.machina.ui.widgets.AppText
import com.example.machina.ui.widgets.AppTextFieldRounded
import com.example.machina.ui.widgets.BackButton
import com.example.machina.ui.widgets.AuthErrorSnackbar
import com.example.machina.ui.widgets.AppPopupModal
import com.example.machina.utils.saveSignupCompleted
import com.example.machina.view_model.auth_viewmodel.AuthStep
import com.example.machina.view_model.auth_viewmodel.AuthUiState
import com.example.machina.view_model.auth_viewmodel.AuthViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.machina.view_model.dashboard_viewmodel.DashboardUiState

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = koinViewModel()
) {
    val context = LocalContext.current

    var rawEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    var showDialog by remember { mutableStateOf(false) }
    var biometricStatus by remember { mutableStateOf(false) }

    val state by viewModel.state.collectAsState()
    val isLoading = state is AuthUiState.Loading
    val snackbarHostState = remember { SnackbarHostState() }

    AuthErrorSnackbar(
        state = state,
        snackbarHostState = snackbarHostState,
        onMessageShown = viewModel::resetState
    )

    LaunchedEffect(state) {
        val currentState = state
        if (currentState is AuthUiState.Success && currentState.step == AuthStep.LoggedIn) {


            saveSignupCompleted(context)

            Toast.makeText(
                context,
                "Logged in successfully",
                Toast.LENGTH_SHORT).show()
            viewModel.resetState()
            navController.navigate("dashboard") {
                popUpTo("login") { inclusive = true } // optional (removes login from backstack)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 10.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {


            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                BackButton(
                    navController = navController,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }

            Spacer(Modifier.height(16.dp))


            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = "Background Image",
            )

            Spacer(Modifier.height(16.dp))

            AppText(
                "Login to Continue",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            AppTextFieldRounded(
                value = rawEmail,
                onValueChange = {
                    rawEmail = it
                    emailError = null
                    viewModel.resetState()
                },
                placeholder = "Email",
                errorText = emailError
//            borderColor = AppGreen
            )
            Spacer(modifier = Modifier.height(16.dp))

            AppPasswordField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                    viewModel.resetState()
                },
                placeholder = "Password",
                borderColor = AppGreen,
                errorText = passwordError
            )

            Spacer(modifier = Modifier.height(50.dp))

            Row(

            ) {

                AppButton(
                    onClick = {
                        val email = rawEmail.trim()
                        emailError = when {
                            email.isBlank() -> "Email is required."
                            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Enter a valid email address."
                            else -> null
                        }
                        passwordError = if (password.isBlank()) {
                            "Password is required."
                        } else {
                            null
                        }

                        if (emailError != null || passwordError != null) {
                            return@AppButton
                        }

                        viewModel.login(email, password)
//

                    },
                    text = "Login",
                    isLoading = isLoading,
                    modifier = Modifier
                        .width(250.dp)
                )

                Spacer(modifier = Modifier.width(25.dp))
                Image(
                    painter = painterResource(id = R.drawable.biometrics),
                    contentDescription = "biometric image",
                    modifier = Modifier.clickable {
                        showDialog = true

                    }
                )

            }

            Spacer(modifier = Modifier.height(16.dp))

            AppText(
                "Forgot Password?",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = AppGreen,
                        modifier = Modifier
                        .clickable {
                    navController.navigate("request-otp")
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = "Don't have an account? ",
                    fontSize = 14.sp
                )
                AppText(
                    text = "Sign up",
                    color = AppGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("email")
                        }
                )
            }

            AppPopupModal(
                showDialog = showDialog,
                onDismiss = { showDialog = false },
                imageRes = R.drawable.biometrics2,
                title = "Fingerprint Authentication",
                description = "Please use finger print to Login",
                buttonText = "Close",
                onButtonClick = {

                    showDialog = false
                    // handle action
                }
            )

        }
    }
}
