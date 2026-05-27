package com.example.machina.ui.screens.auth

import AppButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.widgets.AppText
import com.example.machina.ui.widgets.AppTextField
import com.example.machina.ui.widgets.AuthErrorSnackbar
import com.example.machina.utils.getEmail
import com.example.machina.utils.saveUserId
import com.example.machina.view_model.auth_viewmodel.AuthStep
import com.example.machina.view_model.auth_viewmodel.AuthUiState
import com.example.machina.view_model.auth_viewmodel.AuthViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgotPasswordVerifyOTP(
    navController: NavController,

    viewModel: AuthViewModel = koinViewModel()
) {

    val context = LocalContext.current
    val savedEmail = remember { getEmail(context) }
    val state by viewModel.state.collectAsState()
    val isLoading = state is AuthUiState.Loading
    val snackbarHostState = remember { SnackbarHostState() }

    var timeLeft by remember { mutableStateOf(60) }

    var otp by remember { mutableStateOf("") }
    var codeError by remember { mutableStateOf<String?>(null) }

    AuthErrorSnackbar(
        state = state,
        snackbarHostState = snackbarHostState,
        onMessageShown = viewModel::resetState
    )

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            kotlinx.coroutines.delay(1000)
            timeLeft--
        }
    }

    LaunchedEffect(Unit) {
        viewModel.state.collectLatest { state ->
            if (state is AuthUiState.Success && state.step == AuthStep.EmailVerified) {
                state.userId?.let { saveUserId(context, it) }
                viewModel.resetState()
                navController.navigate("password-reset")
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


            Image(
                painter = painterResource(id =R.drawable.email ),
                contentDescription = "Background Image",
//
            )

            AppText("Validate OTP",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))
            AppText(
                "Please enter 6 digit OTP sent to your email",
                fontSize = 10.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))
            AppText(
                "OTP Expires in  ${timeLeft} Seconds",
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                color = Color.Red
            )
            Spacer(Modifier.height(10.dp))

            AppTextField(
                value = otp,
                onValueChange = {
                    otp = it.filter(Char::isDigit).take(6)
                    codeError = null
                    viewModel.resetState()
                },
                placeholder= "Enter 6 digit OTP",
                borderColor = Color.LightGray,
                focusedBorderColor = AppGreen,
                errorText = codeError
            )
            Spacer(modifier = Modifier.height(50.dp))

            AppButton(
                onClick = {
                    if (otp.length != 6) {
                        codeError = "Enter the 6 digit OTP sent to your email"
                        return@AppButton
                    }
                    val email = savedEmail.orEmpty()
                    if (email.isBlank()) {
                        codeError = "Email session expired. Please request a new code."
                        return@AppButton
                    }
                    viewModel.verifyOtp(email, otp)
//                navController.navigate("profile")
                },
                text = "Verify Code",
                isLoading = isLoading
            )
        }
    }
}
