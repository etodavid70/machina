package com.example.machina.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.example.machina.view_model.AuthViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun PasswordScreen(
    navController: NavController,
    viewModel: AuthViewModel = koinViewModel()
) {

    var email by remember { mutableStateOf("") }

    Column {

        TextField(
            value = email,
            onValueChange = { email = it }
        )

        Button(
            onClick = { viewModel.sendEmail(email) }
        ) {
            Text("password screen")
        }
    }
}