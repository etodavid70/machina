package com.example.machina.ui.screens.dashboard.home.home_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.machina.ui.screens.dashboard.home.active_machinery.ActiveMachineryCard
import com.example.machina.ui.screens.dashboard.home.cloud_instances.dashboardcards.CloudInstancesCard
import com.example.machina.ui.widgets.AppText
import com.example.machina.view_model.dashboard_viewmodel.HomeViewModel



@Composable
fun HomeScreen(
//    viewModel: HomeViewModel = viewModel(),
    navController: NavController
) {



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
            onCreateClick = {
                // handle create click
            }
        )

        // cloud instance
        AppText(
            "Cloud instances",
            fontWeight = FontWeight.Light,
            fontSize = 12.sp
        )
        CloudInstancesCard(
            onCreateClick = {
                navController.navigate("connect_cloud")
            }
        )
    }

}