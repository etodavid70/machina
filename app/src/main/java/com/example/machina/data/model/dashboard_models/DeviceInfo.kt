package com.example.machina.data.model.dashboard_models

data class DeviceInfo(
    val totalRam: String,
    val availableRam: String,
    val totalRamBytes: Long,
    val availableRamBytes: Long,
    val totalStorage: String,
    val freeStorage: String,
    val usedStorage: String,
    val totalStorageBytes: Long,
    val freeStorageBytes: Long,
    val usedStorageBytes: Long,
    val cpuCores: Int,
    val cpuMaxFrequencyMhz: Int?,
    val has64BitCpu: Boolean,
    val hasMinimumRam: Boolean,
    val hasMinimumStorage: Boolean,
    val hasStrongEnoughCpu: Boolean,
    val canCreateVirtualMachine: Boolean,
    val requirementFailures: List<String>,
    val cpuInfoRaw: String
)
