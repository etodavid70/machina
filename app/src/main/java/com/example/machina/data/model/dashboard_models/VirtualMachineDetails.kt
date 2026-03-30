package com.example.machina.data.model.dashboard_models

data class VirtualMachineDetails(
    val cpu: String,
    val ram: String,
    val storage: String,
    val os: String,
    val ipAddress: String,
    val createdAt: String
)