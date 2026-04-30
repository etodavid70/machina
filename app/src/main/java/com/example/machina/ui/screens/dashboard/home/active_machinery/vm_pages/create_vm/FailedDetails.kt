package com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.machina.ui.widgets.AppText
import com.example.machina.view_model.dashboard_viewmodel.DeviceInfoViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun FailedDetails(viewModel: DeviceInfoViewModel) {

    val context = LocalContext.current
    val deviceInfo by viewModel.deviceInfo.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDeviceInfo(context)
    }

    deviceInfo?.let { info ->

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text("MEMORY")
            Text("Total RAM: ${info.totalRam}")
            Text("Available RAM: ${info.availableRam}")

            Spacer(modifier = Modifier.height(12.dp))

            Text("STORAGE")
            Text("Total Storage: ${info.totalStorage}")
            Text("Used Storage: ${info.usedStorage}")
            Text("Free Storage: ${info.freeStorage}")

            Spacer(modifier = Modifier.height(12.dp))

            Text("CPU")
            Text("Cores: ${info.cpuCores}")

            Spacer(modifier = Modifier.height(12.dp))

            Text("RAW CPU INFO")
            Text(info.cpuInfoRaw)
        }

    } ?: Text("Loading device info...")
}






