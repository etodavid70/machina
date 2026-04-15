package com.example.machina.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.machina.ui.navigation.Screen.DeviceOps

import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm.CreateVirtualMachine
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm.DeviceOptions
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm.FailedDetails
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm.SelectCPUCores
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm.SelectRam
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm.SelectStorage
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm.ViewOsTypes
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.view_vm.ViewActiveMachinery
import com.example.machina.ui.screens.dashboard.home.cloud_instances.cloud_pages.ConnectToACloudInstance
import com.example.machina.ui.screens.dashboard.home.cloud_instances.cloud_pages.ViewCloudInstance
import com.example.machina.ui.screens.dashboard.home.home_screen.HomeScreen
import com.example.machina.ui.screens.dashboard.profile.ProfileScreen
import com.example.machina.ui.screens.dashboard.settings.SettingsScreen
import com.example.machina.view_model.dashboard_viewmodel.CreateVmViewModel
import com.example.machina.view_model.dashboard_viewmodel.HomeViewModel

//define your screens class
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Settings : Screen("settings")
    object Profile : Screen("profile")

    object ConnectCloud : Screen("connect_cloud")
    object ViewCloud : Screen("view_cloud_instances")

    object CreateVM : Screen("create_vm")
    object ViewVM : Screen("view_vm")
    object Failed : Screen("failed_details")
    object ViewOsType : Screen("view_os_type")
    object DeviceOps: Screen("device_options")
    object Ram: Screen("ram")
    object Cpu: Screen("cpu")
    object Storage: Screen("storage")

}

// set up a navigation graph using the screen class and the pages already created
@Composable
fun NavigationGraph(navController: NavHostController) {

    val viewModel: HomeViewModel = viewModel()

    val createVmViewModel: CreateVmViewModel = viewModel()



    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        //bottom nav screens
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(Screen.Settings.route) { SettingsScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }

        //other screens
        composable(Screen.ConnectCloud.route) { ConnectToACloudInstance(navController) }
        composable(Screen.ViewCloud.route) {

            ViewCloudInstance(
                navController,
                viewModel.cloudList
            )
        }

        composable(Screen.ViewVM.route) {
            ViewActiveMachinery(
                navController,
                viewModel.vmList
            )
        }


        composable(Screen.CreateVM.route) {
            CreateVirtualMachine(
                navController,
                createVmViewModel.mainOsVmList

            )
        }

        composable(Screen.Failed.route) { FailedDetails() }

        composable(Screen.ViewOsType.route) {
            ViewOsTypes(
                navController,
                createVmViewModel.vmList
            )
        }

        composable(DeviceOps.route){ DeviceOptions(navController) }

        composable(Screen.Ram.route){ SelectRam() }
        composable(Screen.Cpu.route){ SelectCPUCores() }
        composable(Screen.Storage.route){ SelectStorage() }
    }
}

// Bottom navigation items using the screen class
val items = listOf(
    Screen.Home,
    Screen.Settings,
    Screen.Profile
)






