package com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.data.model.createvm_models.OperatingSystems
import com.example.machina.ui.navigation.Screen
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.widgets.AppText
import com.example.machina.ui.widgets.OsAsyncImage
import com.example.machina.view_model.dashboard_viewmodel.CreateVmViewModel

@Composable
fun ViewOsTypes(
    navController: NavController,
    vmList: List<OperatingSystems>,
    createVmViewModel: CreateVmViewModel,
    loading: Boolean,
    errorMessage: String?,
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        AppText(
            text = "Create a Virtual Machine",
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
                        color = AppGreen
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
                    AppText(text = "No distributions available")
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(vmList) { instance ->
                        VMItem(
                            instance = instance,
                            onButtonClick = {
                                createVmViewModel.selectDistro(instance)
                                navController.navigate(Screen.DeviceOps.route)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VMItem(
    instance: OperatingSystems,
    onButtonClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .clickable(onClick = onButtonClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OsAsyncImage(
            imageUrl = instance.imageUrl,
            osName = instance.name,
        )
        Spacer(modifier = Modifier.width(5.dp))

        Column(horizontalAlignment = Alignment.Start) {
            AppText(text = instance.name, fontWeight = FontWeight.Medium, fontSize = 20.sp)
        }
    }
}
