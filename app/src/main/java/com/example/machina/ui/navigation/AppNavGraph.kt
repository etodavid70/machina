package com.example.machina.ui.navigation
import androidx.compose.material.icons.rounded.Home
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.machina.ui.screens.dashboard.home.cloud_instances.cloud_pages.ConnectToACloudInstance
import com.example.machina.ui.screens.dashboard.home.home_screen.HomeScreen
import com.example.machina.ui.screens.dashboard.profile.ProfileScreen
import com.example.machina.ui.screens.dashboard.settings.SettingsScreen

//define your screens class
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Settings : Screen("settings")
    object Profile : Screen("profile")

    object ConnectCloud: Screen("connect_cloud")

}

// set up a navigation graph using the screen class and the pages already created
@Composable
fun NavigationGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        //bottom nav screens
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable (Screen.Settings.route) { SettingsScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }

        //other screens
        composable(Screen.ConnectCloud.route){ ConnectToACloudInstance(navController) }
    }
}

// Bottom navigation items using the screen class
val items = listOf(
    Screen.Home,
    Screen.Settings,
    Screen.Profile
)






