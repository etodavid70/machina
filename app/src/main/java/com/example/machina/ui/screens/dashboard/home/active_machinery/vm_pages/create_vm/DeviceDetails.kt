package com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm

import AppButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.machina.R
import com.example.machina.ui.screens.dashboard.home.active_machinery.widgets.DeviceCard
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.theme.AppGrey
import com.example.machina.ui.widgets.AppText
import androidx.navigation.NavController

@Composable
fun DeviceOptions(navController: NavController) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)

    ) {

        AppText("Create Vm")

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // take available space
        ) {

            // LEFT SIDE (TEXTS)
            Column(
                modifier = Modifier
//                    .weight(1f),

                    .width(80.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                AppText("RAM")

                Spacer(modifier = Modifier.height(100.dp))
                AppText("CPU")

                Spacer(modifier = Modifier.height(100.dp))
                AppText("STORAGE")
            }

            // DIVIDER LINE
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            // RIGHT SIDE (CARDS)
            Column(
                modifier = Modifier
                    .weight(2f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                DeviceCard(
                    "Configure RAM",
                    R.drawable.ram,
                    AppGreen
                ) {

                    navController.navigate("ram")
                }

                Spacer(modifier = Modifier.height(50.dp))

                DeviceCard(
                    "Configure CPU", R.drawable.cpu, AppGrey
                ) {
                    navController.navigate("cpu")
                }
                Spacer(modifier = Modifier.height(50.dp))

                DeviceCard(
                    "Configure Storage", R.drawable.storage, AppGrey
                ) {
                    navController.navigate("storage")
                }
            }
        }

        AppButton("PROCEED", {


        })
    }
}

