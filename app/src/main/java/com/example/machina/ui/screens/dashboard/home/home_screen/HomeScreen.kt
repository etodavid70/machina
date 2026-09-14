package com.example.machina.ui.screens.dashboard.home.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.ui.navigation.Screen
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_cards.ActiveMachineryCard
import com.example.machina.ui.screens.dashboard.home.widgets.CloudInstancesCard
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.theme.GreyColor2
import com.example.machina.ui.widgets.AppText
import com.example.machina.view_model.dashboard_viewmodel.DashboardViewModel


@Composable
fun HomeScreen(
    viewModel: DashboardViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.fetchProfile()
        viewModel.fetchInstances()
    }

    val profile by viewModel.profile.collectAsState()
    val vmList = viewModel.vmList
    val cloudList by viewModel.instances.collectAsState()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppGreen.copy(alpha = 0.10f), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            AppText(
                text = "Welcome back${
                    profile.firstName
                        .takeIf { it.isNotBlank() }
                        ?.let { ", ${it.replaceFirstChar { char -> char.uppercase() }}" }
                        ?: ""
                }",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF17352A)
            )
            Spacer(Modifier.height(6.dp))
            AppText(
                text = "Your infrastructure, all in one place.",
                fontSize = 14.sp,
                color = GreyColor2
            )
        }

        AppText(
            "REMOTE SERVERS",
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = GreyColor2
        )
        CloudInstancesCard(
            cloudList = cloudList,
            onCreateClick = {
                if (cloudList.isEmpty()) {
                    navController.navigate(Screen.ConnectCloud.route)
                }
                else{
                    navController.navigate(Screen.ViewCloud.route)
                }
            }
        )

        AppText(
            "ACTIVE MACHINERY",
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = GreyColor2
        )
        ActiveMachineryCard(
            vmList = vmList,
            onCreateClick = {
                if (vmList.isEmpty()) {
                    navController.navigate(Screen.CreateVM.route)
                }
                else{
                    navController.navigate(Screen.ViewVM.route)
                }
            }
        )


    }

}
