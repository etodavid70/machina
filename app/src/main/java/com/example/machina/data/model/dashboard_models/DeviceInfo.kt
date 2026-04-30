package com.example.machina.data.model.dashboard_models

data class DeviceInfo(
    val totalRam: String,
    val availableRam: String,
    val totalStorage: String,
    val freeStorage: String,
    val usedStorage: String,
    val cpuCores: Int,
    val cpuInfoRaw: String
)
