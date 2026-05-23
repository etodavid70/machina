package com.example.machina.ui.screens.dashboard.settings

import AppButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.R
import com.example.machina.data.model.onboarding_models.PasswordChangeRequest
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.widgets.AppPasswordField
import com.example.machina.ui.widgets.AppText
import com.example.machina.ui.widgets.BackButton
import com.example.machina.ui.widgets.DashboardErrorSnackbar
import com.example.machina.utils.getToken
import com.example.machina.view_model.dashboard_viewmodel.DashboardUiState
import com.example.machina.view_model.dashboard_viewmodel.DashboardViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PasswordChange(
    navController: NavController,
    viewModel: DashboardViewModel = koinViewModel()
) {

    val context = LocalContext.current

    val state by viewModel.state.collectAsState()
    val isLoading = state is DashboardUiState.Loading
    val snackbarHostState = remember { SnackbarHostState() }

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var oldPasswordError by remember { mutableStateOf<String?>(null) }
    var newPasswordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var formError by remember { mutableStateOf<String?>(null) }

    DashboardErrorSnackbar(
        state = state,
        snackbarHostState = snackbarHostState,
        onMessageShown = viewModel::resetState
    )

    LaunchedEffect(state) {
        val successState = state as? DashboardUiState.Success ?: return@LaunchedEffect
        oldPassword = ""
        newPassword = ""
        confirmPassword = ""
        snackbarHostState.showSnackbar(successState.message ?: "Password changed successfully.")
        viewModel.resetState()
        navController.popBackStack()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 10.dp, vertical = 40.dp)
            .verticalScroll(rememberScrollState()),
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
                painter = painterResource(id =R.drawable.password ),
                contentDescription = "Background Image",
            )

            AppText("Change Password",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))
            AppPasswordField(
                value = oldPassword,
                onValueChange = {
                    oldPassword = it
                    oldPasswordError = null
                    confirmPasswordError = null
                    formError = null
                    viewModel.resetState()
                },
                placeholder = "Old Password",
                borderColor = AppGreen,
                errorText = oldPasswordError
            )

            Spacer(modifier = Modifier.height(16.dp))


            AppPasswordField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    newPasswordError = null
                    confirmPasswordError = null
                    formError = null
                    viewModel.resetState()
                },
                placeholder = "New Password",
                borderColor = AppGreen,
                errorText = newPasswordError
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppPasswordField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = null
                    formError = null
                    viewModel.resetState()
                },
                placeholder = "Confirm Password",
                borderColor = AppGreen,
                errorText = confirmPasswordError
            )
            Spacer(modifier = Modifier.height(50.dp))
            formError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            AppButton(
                onClick = {
                    oldPasswordError = when {
                        oldPassword.isBlank() -> "Password is required."
                        oldPassword.length < 8 -> "Password must be at least 8 characters."
                        else -> null
                    }

                    newPasswordError = when {
                        newPassword.isBlank() -> "Password is required."
                        newPassword.length < 8 -> "Password must be at least 8 characters."
                        newPassword == oldPassword -> "New password must be different from old password."
                        else -> null
                    }
                    confirmPasswordError = when {
                        confirmPassword.isBlank() -> "Confirm your password."
                        confirmPassword != newPassword -> "Passwords do not match."
                        else -> null
                    }

                    if (oldPasswordError != null || newPasswordError != null || confirmPasswordError != null) {
                        return@AppButton
                    }

                    val passwordData = PasswordChangeRequest(
                        old_password = oldPassword,
                        new_password= newPassword,
                        confirm_password = confirmPassword
                    )
                    val token = getToken(context).orEmpty()
                    if (token.isBlank()) {
                        formError = "Your session has expired. Please log in again."
                        return@AppButton
                    }

                    formError = null
                    viewModel.changePassword(passwordData)


                },
                text = "Submit",
                isLoading = isLoading
            )
        }
    }
}
