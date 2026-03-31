package com.example.machina.data.model.dashboard_models

data class CloudInstances (
    val id: Int,
    val name: String,
    val status: String,
    val vmDetails: VirtualMachineDetails
)