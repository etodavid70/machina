package com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm

import AppButton
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.R
import com.example.machina.data.model.createvm_models.MainOs
import com.example.machina.ui.navigation.Screen
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.widgets.AppPopupModal
import com.example.machina.ui.widgets.AppText
import com.example.machina.ui.widgets.OsAsyncImage
import com.example.machina.view_model.dashboard_viewmodel.CreateVmViewModel
import com.example.machina.view_model.dashboard_viewmodel.DeviceInfoViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateVirtualMachine(
    navController: NavController,
    vmList: List<MainOs>,
    createVmViewModel: CreateVmViewModel,
    loading: Boolean,
    errorMessage: String?,
) {
    val context = LocalContext.current

    var showCheckingDialog by remember { mutableStateOf(true) }
    var checkComplete by remember { mutableStateOf(false) }
    var canCreateVirtualMachine by remember { mutableStateOf(false) }
    var failedDialog by remember { mutableStateOf(false) }

    val deviceInfoViewModel: DeviceInfoViewModel = koinViewModel()

    LaunchedEffect(Unit) {
        val deviceInfo = deviceInfoViewModel.loadDeviceInfo(context)
        canCreateVirtualMachine = deviceInfo.canCreateVirtualMachine
        checkComplete = true
        showCheckingDialog = false
        failedDialog = !deviceInfo.canCreateVirtualMachine
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 16.dp)

    ) {
        if (checkComplete && canCreateVirtualMachine) {
            AppText(
                text = "Select an Operating system",
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            )

            when {
                loading -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color= AppGreen
                        )
                    }
                }
                errorMessage != null -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AppText(text = errorMessage)
                    }
                }
                vmList.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        AppText(text = "No operating systems available")
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        items(vmList) { instance ->
                            VirtualMachineItem(
                                instance = instance,
                                buttonText = "View Options",
                                onButtonClick = {
                                    createVmViewModel.fetchOperatingSystems(instance.name)
                                    navController.navigate(Screen.ViewOsType.route)
                                }
                            )
                        }
                    }
                }
            }
        }

        AppPopupModal(
            showDialog = showCheckingDialog,
            onDismiss = {
                showCheckingDialog = false
                navController.popBackStack()
            },
            imageRes = R.drawable.machina,
            title = "Checking your device's Specification",
            description = "Please wait...",
            buttonText = "Cancel",
            onButtonClick = {
                showCheckingDialog = false
                navController.popBackStack()
            }
        )

        AppPopupModal(
            showDialog = failedDialog,
            onDismiss = {
                failedDialog = false
            },
            imageRes = R.drawable.setup_failed,
            title = "Device check failed",
            description = "Your Device does not meet the required minimum Specification",
            buttonText = "View Details",
            onButtonClick = {
                failedDialog = false
                navController.navigate(Screen.Failed.route)
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
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OsAsyncImage(
            imageUrl = instance.imageUrl,
            osName = instance.name,
        )
        Spacer(modifier = Modifier.width(5.dp))

        Column(horizontalAlignment = Alignment.Start) {
            AppText(text = instance.name, fontWeight = FontWeight.Bold, fontSize = 25.sp)
            Spacer(modifier = Modifier.height(20.dp))
            AppButton(
                text = buttonText,
                onClick = onButtonClick
            )
        }
    }
}
