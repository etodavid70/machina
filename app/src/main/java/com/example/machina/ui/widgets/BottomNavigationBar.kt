import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.machina.ui.navigation.Screen
import com.example.machina.ui.navigation.items
import com.example.machina.ui.theme.AppGreen

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = when (screen) {
                            Screen.Home -> Icons.Rounded.Home
                            Screen.Settings -> Icons.Rounded.Settings
                            Screen.Profile -> Icons.Rounded.AccountCircle

                            else -> Icons.Rounded.Info // Default icon (you can change this)
                        },
                        contentDescription = screen.route
                    )
                },
                label = { Text(text = screen.route.capitalize()) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppGreen,
                    selectedTextColor = AppGreen,
                    unselectedIconColor = AppGreen,
                    unselectedTextColor = AppGreen,
                    indicatorColor = Color.White // background of selected item
                )
            )
        }
    }
}