package com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm

import AppButton
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.machina.R
import com.example.machina.data.model.createvm_models.CreateMachinery
import com.example.machina.data.model.createvm_models.MainOs
import com.example.machina.data.model.dashboard_models.ActiveMachinery
import com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.view_vm.VirtualMachineItem
import com.example.machina.ui.widgets.AppPopupModal
import com.example.machina.ui.widgets.AppText

@Composable
fun CreateVirtualMachine(
    navController: NavController,
    vmList: List<MainOs>,
) {


    var showDialog by remember { mutableStateOf(true) }
    var successful by remember { mutableStateOf(false) }
    var failedDialog by remember { mutableStateOf(false) }


    Column() {
//        AppText(text = "Create a Virtual Machine", fontWeight = FontWeight.Bold, fontSize = 25.sp)

        //ensure this is shown after successful is true


        if (showDialog==false){
            AppText(text = "Select an Operating system", fontWeight = FontWeight.Normal, fontSize = 18.sp)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(vmList) { instance ->
                    VirtualMachineItem(
                        instance,
                        "View Options"

                    ) {
                        navController.navigate("view_os_type")
                    }
                }
                item {
                }

            }


        }


        AppPopupModal(
            showDialog = showDialog,
            onDismiss = {

                //shows the failed dialogue
                failedDialog = true
            },
            imageRes = R.drawable.machina,
            title = "Checking your device’s Specification",
            description = "Please wait...",
            buttonText = "Cancel",
            onButtonClick = {

                //removes this dialogue
                showDialog = false
                // handle action
            }
        )

        AppPopupModal(
            showDialog = failedDialog,
            onDismiss = {

                //removes the failed dialogue
                failedDialog = false
            },
            imageRes = R.drawable.setup_failed,
            title = "Checking your device’s Specification",
            description = "Your Device does not meet the required minimum Specification",
            buttonText = "View Details",
            onButtonClick = {
//view details page
                failedDialog = false
                navController.navigate("failed_details")
            }
        )

    }
}


@Composable
fun VirtualMachineItem(
    instance: MainOs,
    buttonText: String,
    onButtonClick: () -> Unit,
) {


    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
//            .clickable(
//                onClick = onButtonClick
//            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = instance.osImageUrl,
            contentDescription = "Network Image",
            error = painterResource(R.drawable.error)
        )
        Spacer(modifier = Modifier.width(5.dp))

        Column(
            horizontalAlignment = Alignment.Start
        )
        {
            AppText(text = instance.osName, fontWeight = FontWeight.Bold, fontSize = 25.sp)
            Spacer(modifier = Modifier.height(20.dp))
            AppButton(
                text = buttonText,
                onClick = onButtonClick
            )


        }
    }


}