package com.example.machina.ui.navigation


import EmailScreen
import PasswordScreen
import ProfileScreen
import VerificationScreen


import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.machina.utils.getUserId
import com.example.machina.ui.screens.auth.LoginScreen
import com.example.machina.ui.screens.dashboard.landing_page.LandingPage
import com.example.machina.utils.hasCompletedSignup


@Composable
fun AuthNavGraph() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val hasExistingSignup = getUserId(context) != null
    val startDestination = if (hasCompletedSignup(context) || hasExistingSignup) "login" else "email"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable("email") { EmailScreen(navController) }

        composable("verify") { VerificationScreen(navController) }

        composable("profile") { ProfileScreen(navController) }

        composable("password") { PasswordScreen(navController) }

        composable("login") { LoginScreen(navController) }

        composable("dashboard") { LandingPage() }
    }
}
