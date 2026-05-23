package com.example.machina.ui.screens.dashboard.home.home_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_cards.ActiveMachineryCard
import com.example.machina.ui.screens.dashboard.home.cloud_instances.cloud_cards.CloudInstancesCard
import com.example.machina.ui.widgets.AppText
import com.example.machina.view_model.dashboard_viewmodel.DashboardViewModel
import com.example.machina.view_model.dashboard_viewmodel.HomeViewModel


@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    cloudInstanceViewModel: DashboardViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.refresh()
        cloudInstanceViewModel.fetchInstances()
    }


    val vmList = viewModel.vmList
    val cloudList by cloudInstanceViewModel.instances.collectAsState()

//    Log.d("vmlist", vmList.toString())
//    Log.d("cloudlist", cloudList.toString())

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        AppText("Welcome Eto")

        //vm
        AppText(
            "Active machinery",
            fontWeight = FontWeight.Light,
            fontSize = 12.sp
        )
        ActiveMachineryCard(
            vmList = vmList,
            onCreateClick = {
                if (vmList.isEmpty()) {
                    navController.navigate("create_vm")
                }
                else{
                    navController.navigate("view_vm")
                }
            }
        )

        // cloud instance
        AppText(
            "Cloud instances",
            fontWeight = FontWeight.Light,
            fontSize = 12.sp
        )
        CloudInstancesCard(
            cloudList = cloudList,
            onCreateClick = {
                if (cloudList.isEmpty()) {
                    navController.navigate("connect_cloud")
                }
                else{
                    navController.navigate("view_cloud_instances")
                }
            }
        )
    }

}
