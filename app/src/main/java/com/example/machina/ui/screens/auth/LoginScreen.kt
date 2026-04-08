package com.example.machina.ui.screens.auth

import AppButton
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
import androidx.compose.runtime.Composable
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
import com.example.machina.ui.widgets.AppPopupModal
import com.example.machina.view_model.auth_viewmodel.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = koinViewModel()
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var biometricStatus by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            value = email,
            onValueChange = { email = it },
            placeholder = "Email",
//            borderColor = AppGreen
        )
        Spacer(modifier = Modifier.height(16.dp))

        AppPasswordField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            borderColor = AppGreen
        )

        Spacer(modifier = Modifier.height(50.dp))

        Row(

        ) {

            AppButton(
                onClick = {
                    viewModel.login(email, password)
                    navController.navigate("dashboard")

                },
                text = "Login",
                modifier = Modifier
                    .width(250.dp)
            )

            Spacer(modifier = Modifier.width(50.dp))
            Image(
                painter = painterResource(id = R.drawable.biometrics),
                contentDescription = "Background Image",
                modifier = Modifier.clickable {
                 showDialog=true

                }
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        AppText(
            "Forgot Password?",
            fontSize = 15.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            color = AppGreen
        )

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