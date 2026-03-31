package com.example.machina.view_model.dashboard_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.machina.data.model.dashboard_models.ActiveMachinery
import com.example.machina.data.model.dashboard_models.CloudInstances
import com.example.machina.data.model.dashboard_models.VirtualMachineDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _vmState = MutableStateFlow<VmUiState>(VmUiState.Loading)
    val vmState: StateFlow<VmUiState> = _vmState

    private val _cloudInstanceUIState = MutableStateFlow<CloudInstanceUIState>(CloudInstanceUIState.Loading)
    val cloudInstanceUIState: StateFlow<CloudInstanceUIState> = _cloudInstanceUIState

    init {
        fetchVMs()
    }

    private fun fetchVMs() {
        viewModelScope.launch {
            try {
                // TODO: Replace with API call later
//                val data = repository.getActiveMachinery()
                val data = listOf(
                    ActiveMachinery(
                        id = 1,
                        name = "Fedora",
                        status = "Running",
                        imageUrl = "https://share.google/4pNh1teeP2vzfKMQm",
                        vmDetails = VirtualMachineDetails(
                            cpu = "4 cores",
                            ram = "8GB",
                            storage = "100GB",
                            os = "Ubuntu",
                            ipAddress = "192.168.1.1",
                            createdAt = "2026-03-30"
                        )
                    )


                )

                _vmState.value = VmUiState.Success(data)

            } catch (e: Exception) {
                _vmState.value = VmUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }



    private fun fetchCloudInstances() {
        viewModelScope.launch {
            try {
                // TODO: Replace with API call later
//                val data = repository.getActiveMachinery()
                val data = listOf(
                    CloudInstances(
                        id = 1,
                        name = "VM 1",
                        status = "Running",
                        vmDetails = VirtualMachineDetails(
                            cpu = "4 cores",
                            ram = "8GB",
                            storage = "100GB",
                            os = "Ubuntu",
                            ipAddress = "192.168.1.1",
                            createdAt = "2026-03-30"
                        )
                    )
                )

                _cloudInstanceUIState.value = CloudInstanceUIState.Success(data)

            } catch (e: Exception) {
                _cloudInstanceUIState.value = CloudInstanceUIState.Error(e.message ?: "Something went wrong")
            }
        }
    }
}