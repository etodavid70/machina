package com.example.machina.view_model.dashboard_viewmodel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.machina.data.model.dashboard_models.ActiveMachinery
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.machina.data.model.dashboard_models.VirtualMachineDetails


class HomeViewModel : ViewModel() {

    var vmList by mutableStateOf<List<ActiveMachinery>>(emptyList())
        private set

    init {
        loadVMData() // automatically loads data
    }

    fun refresh() {
        loadVMData()
    }




    fun loadVMData() {
        // simulate API response
     vmList = listOf(
            ActiveMachinery(
                id = 1,
                name = "Ubuntu VM",
                status = "Running",
                imageUrl = "https://imageurl",
                vmDetails = VirtualMachineDetails(
                    cpu = "4 cores",
                    ram = "8GB",
                    storage = "100GB",
                    os = "Ubuntu 22.04",
                    ipAddress = "192.168.1.10",
                    createdAt = "2026-04-07"
                )
            ),
            ActiveMachinery(
                id = 2,
                name = "Windows VM",
                status = "Stopped",
                imageUrl = "https://imageurl2",
                vmDetails = VirtualMachineDetails(
                    cpu = "2 cores",
                    ram = "4GB",
                    storage = "50GB",
                    os = "Windows 10",
                    ipAddress = "192.168.1.11",
                    createdAt = "2026-04-06"
                )
            )
        )


    }
}
