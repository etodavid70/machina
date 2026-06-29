import android.provider.CalendarContract.Colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person3
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.machina.ui.navigation.Screen
import com.example.machina.ui.navigation.items
import com.example.machina.ui.theme.AppDarkGreen
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.theme.GreyColor
import com.example.machina.ui.theme.GreyColor2
import com.example.machina.ui.widgets.AppText

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar (
        containerColor = Color.White, // or any color you want
        tonalElevation = 0.dp
    ){

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
                            Screen.Home -> Icons.Outlined.Home
                            Screen.Settings -> Icons.Outlined.Settings
                            Screen.Profile -> Icons.Outlined.Person3

                            else -> Icons.Rounded.Info // Default icon (you can change this)
                        },
                        contentDescription = screen.route
                    )
                },
                label = {  Text(screen.route.capitalize()) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppDarkGreen,
                    selectedTextColor = AppDarkGreen,
                    unselectedIconColor = GreyColor2,
                    unselectedTextColor = GreyColor2,
                    indicatorColor = Color.Transparent, // background of selected item

                )
            )
        }
    }
}