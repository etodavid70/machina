package com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm

import AppButton
import com.example.machina.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.ui.navigation.Screen
import com.example.machina.ui.widgets.AppProgressBar
import com.example.machina.ui.widgets.AppText
import kotlinx.coroutines.delay


@Composable
fun DownloadArtifacts(navController: NavController) {
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        for (step in 1..100) {
            progress = step / 100f
            delay(35)
        }
        navController.navigate(Screen.DisplayOs.route) {
            popUpTo(Screen.Downloading.route) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        AppText(
            text = "Downloading Required Artifacts",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Description
        AppText(
            text = "Please wait...",
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        // Image
        Image(
            painter = painterResource(id = R.drawable.machina),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        AppProgressBar(
            progress = progress
        )


        Spacer(modifier = Modifier.height(30.dp))

        AppText(
            text = "You can minimize or close the app while downloading",
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
//        AppButton("Launch Terminal",
//            {
//
//                navController.navigate("display_os")
//
//            })


    }
}
