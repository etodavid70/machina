package com.example.machina.ui.screens.dashboard.home.active_machinery
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.machina.data.model.dashboard_models.ActiveMachinery


@Composable
fun ActiveMachineryCard(
    vmList: List<ActiveMachinery>,
    onCreateClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        if (vmList.isEmpty()) {
            NoDataCard(onCreateClick)
        } else {
            VmHorizontalList(vmList)
        }
    }
}