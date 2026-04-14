package com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.view_vm
import AppButton
import AppWhiteButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.ui.widgets.AppText
import com.example.machina.ui.widgets.BackButton
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.machina.view_model.dashboard_viewmodel.HomeViewModel
import coil.compose.AsyncImage
import com.example.machina.R
import com.example.machina.data.model.dashboard_models.ActiveMachinery
import com.example.machina.ui.widgets.AppPopupModal

@Composable
fun ViewActiveMachinery(
    navController: NavController,
    vmList: List<ActiveMachinery>,
) {

    var showDialog by remember { mutableStateOf(false) }

    val viewModel: HomeViewModel = viewModel()
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {


        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            BackButton(
                navController = navController,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(vmList) { instance ->
                VirtualMachineItem(
                    instance,
                    "Connect"
                ) {
                    showDialog=true
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                AppWhiteButton(
                    text = "Create Virtual Machine",
                    onClick = {
                        navController.navigate("create_vm")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        AppPopupModal(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            imageRes = R.drawable.biometrics2,
            title = "Fingerprint Authentication",
            description = "Please use finger print to Login",
            buttonText = "Close",
            onButtonClick = {
                showDialog = false
                // handle action
            }
        )
    }
}


@Composable
fun VirtualMachineItem(
    instance: ActiveMachinery,
    buttonText: String,
    onButtonClick: () -> Unit,
) {


    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = instance.imageUrl,
            contentDescription = "Network Image",
            error = painterResource(R.drawable.error)
        )
        Spacer(modifier = Modifier.width(5.dp))

        Column(
            horizontalAlignment = Alignment.Start
        )
        {
//
            AppText(text = instance.name, fontWeight = FontWeight.Bold, fontSize = 25.sp)
            AppText(text = "ID: ${instance.id}", fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))

            AppText(
                text = "${instance.vmDetails.ram}| ${instance.vmDetails.storage}| ${instance.vmDetails.cpu}",
                fontWeight = FontWeight.Light
            )

            Spacer(modifier = Modifier.height(20.dp))
            AppButton(

                text = buttonText,
                onClick = onButtonClick
            )


        }
    }


}