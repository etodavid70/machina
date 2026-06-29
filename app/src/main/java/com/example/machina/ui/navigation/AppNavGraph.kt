package com.example.machina.ui.navigation

import EditProfileScreen
import TerminalScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.machina.ui.navigation.Screen.DeviceOps

import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm.CreateVirtualMachine
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm.DeviceOptions
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm.DownloadArtifacts
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm.FailedDetails
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm.SelectCPUCores
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm.SelectRam
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm.SelectStorage
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm.ViewOsTypes
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.view_vm.ViewActiveMachinery
import com.example.machina.ui.screens.dashboard.home.cloud_instances.cloud_pages.ConnectToACloudInstance

import com.example.machina.ui.screens.dashboard.home.cloud_instances.cloud_pages.ViewCloudInstance
import com.example.machina.ui.screens.dashboard.home.home_screen.HomeScreen
import com.example.machina.ui.screens.dashboard.profile.ViewProfileScreen
import com.example.machina.ui.screens.dashboard.settings.ContactScreen
import com.example.machina.ui.screens.dashboard.settings.NotificationSettingsScreen
import com.example.machina.ui.screens.dashboard.settings.PasswordChange
import com.example.machina.ui.screens.dashboard.settings.PrivacyPolicyScreen
import com.example.machina.ui.screens.dashboard.settings.SettingsScreen
import com.example.machina.ui.screens.dashboard.settings.TermsAndConditionsScreen
import com.example.machina.ui.screens.dashboard.settings.UnavailableFeatureScreen
import com.example.machina.view_model.NotificationSettingsViewModel
import com.example.machina.view_model.dashboard_viewmodel.DashboardViewModel
import com.example.machina.view_model.dashboard_viewmodel.CreateVmViewModel
import com.example.machina.view_model.dashboard_viewmodel.DeviceInfoViewModel
import com.example.machina.view_model.dashboard_viewmodel.SshConnectionViewModel
import org.koin.androidx.compose.koinViewModel

//define your screens class
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Settings : Screen("settings")
    object PasswordChange: Screen("password_change")
    object NotificationSettings : Screen("notification_settings")
    object RateApp : Screen("rate_app")
    object ShareApp : Screen("share_app")
    object PrivacyPolicy : Screen("privacy_policy")
    object TermsAndConditions : Screen("terms_and_conditions")
    object Contact : Screen("contact")
    object EditProfile : Screen("edit_profile")
    object Profile : Screen("profile")

    object ConnectCloud : Screen("connect_cloud")
    object ConnectSavedCloud : Screen("connect_saved_cloud")
    object Terminal : Screen("terminal")
    object ViewCloud : Screen("view_cloud_instances")

    object CreateVM : Screen("create_vm")
    object ViewVM : Screen("view_vm")
    object Failed : Screen("failed_details")
    object ViewOsType : Screen("view_os_type")
    object DeviceOps: Screen("device_options")
    object Ram: Screen("ram")
    object Cpu: Screen("cpu")
    object Storage: Screen("storage")
    object Downloading: Screen("download_artifact")
}

// set up a navigation graph using the screen class and the pages already created
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    onLogout: () -> Unit = {}
) {

    val createVmViewModel: CreateVmViewModel = koinViewModel()

    val mainOsList by createVmViewModel.mainOs.collectAsState()
    val operatingSystems by createVmViewModel.operatingSystems.collectAsState()
    val mainOsLoading by createVmViewModel.mainOsLoading.collectAsState()
    val distrosLoading by createVmViewModel.distrosLoading.collectAsState()
    val createVmError by createVmViewModel.errorMessage.collectAsState()

    val deviceInfoViewModel: DeviceInfoViewModel = viewModel()

    val dashboardViewModel: DashboardViewModel = koinViewModel()

    val sshConnectionViewModel: SshConnectionViewModel = koinViewModel()

    val notificationSettingsViewModel: NotificationSettingsViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        //bottom nav screens
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                navController = navController,
                onChangePasswordClick = { navController.navigate(Screen.PasswordChange.route) },
                onRateAppClick = { navController.navigate(Screen.RateApp.route) },
                onShareAppClick = { navController.navigate(Screen.ShareApp.route) },
                onPrivacyPolicyClick = { navController.navigate(Screen.PrivacyPolicy.route) },
                onTermsAndConditionsClick = { navController.navigate(Screen.TermsAndConditions.route) },
                onContactClick = { navController.navigate(Screen.Contact.route) },
                onNotificationSettingsClick = { navController.navigate(Screen.NotificationSettings.route) },
            )
        }
        composable(Screen.PasswordChange.route) { PasswordChange(navController) }
        composable(Screen.NotificationSettings.route) {
            NotificationSettingsScreen(
                viewModel = notificationSettingsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.RateApp.route) {
            UnavailableFeatureScreen(
                title = "Rate App",
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.ShareApp.route) {
            UnavailableFeatureScreen(
                title = "Share App",
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.PrivacyPolicy.route) {
            PrivacyPolicyScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.TermsAndConditions.route) {
            TermsAndConditionsScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.Contact.route) {
            ContactScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.Profile.route) {
            ViewProfileScreen(navController = navController,
                onLogout = onLogout)
        }
        composable(Screen.EditProfile.route) {
            EditProfileScreen(navController = navController,)
        }



//        other screens
        composable(Screen.ConnectCloud.route) {
            ConnectToACloudInstance(
                navController = navController,
                viewModel = sshConnectionViewModel,
                dashboardViewModel = dashboardViewModel
            )
        }
        composable(Screen.ConnectSavedCloud.route) {
            val selectedInstance by dashboardViewModel.selectedInstance.collectAsState()
            ConnectToACloudInstance(
                navController = navController,
                viewModel = sshConnectionViewModel,
                dashboardViewModel = dashboardViewModel,
                savedServer = selectedInstance,
                savedServerMode = true
            )
        }
        composable(Screen.Terminal.route) {
            TerminalScreen(navController, sshConnectionViewModel)
        }
        composable(Screen.ViewCloud.route) {

            ViewCloudInstance(
                navController,
                dashboardViewModel
            )
        }

        composable(Screen.ViewVM.route) {
            ViewActiveMachinery(
                navController = navController,
                vmList = dashboardViewModel.vmList,
            )
        }

        composable(Screen.CreateVM.route) {
            CreateVirtualMachine(
                navController = navController,
                vmList = mainOsList,
                createVmViewModel = createVmViewModel,
                loading = mainOsLoading,
                errorMessage = createVmError,
            )
        }

        composable(Screen.Failed.route) {
            FailedDetails(viewModel = deviceInfoViewModel)
        }

        composable(Screen.ViewOsType.route) {
            ViewOsTypes(
                navController = navController,
                vmList = operatingSystems,
                createVmViewModel = createVmViewModel,
                loading = distrosLoading,
                errorMessage = createVmError,
            )
        }

        composable(DeviceOps.route){ DeviceOptions(navController) }

        composable(Screen.Ram.route){ SelectRam() }
        composable(Screen.Cpu.route){ SelectCPUCores() }
        composable(Screen.Storage.route){ SelectStorage() }
        composable(Screen.Downloading.route){ DownloadArtifacts() }
    }
}

// Bottom navigation items using the screen class
val items = listOf(
    Screen.Home,
    Screen.Settings,
    Screen.Profile
)
