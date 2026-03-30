package com.example.machina.ui.navigation


import EmailScreen
import PasswordScreen
import ProfileScreen
import VerificationScreen


import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.machina.ui.screens.auth.LoginScreen
import com.example.machina.ui.screens.dashboard.landing_page.LandingPage


@Composable
fun AuthNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "email"
    ) {

        composable("email") { EmailScreen(navController) }

        composable("verify") { VerificationScreen(navController) }

        composable("profile") { ProfileScreen(navController) }

        composable("password") { PasswordScreen(navController) }

        composable("login") { LoginScreen(navController) }

        composable("dashboard") { LandingPage() }
    }
}