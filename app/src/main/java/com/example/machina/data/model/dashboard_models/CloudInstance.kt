package com.example.machina.data.model.dashboard_models

data class CloudInstance(
    val id: Int,
    val connectionType: String,
    val username: String,
    val port: Int,
    val publicIp: String,
    val privateIp: String,
    val cpuCores: Int,
    val ramMb: Int,
    val storageGb: Int,
    val osVersion: String,
    val serviceProvider: String,
    val imageUrl: String,
    val status: String,
    val createdAt: String,
    val user: Int,
    val mainOs: Int,
    val password: String? = null,
    val secretKey: String? = null
) {
    val name: String
        get() = "$username@$publicIp"

    val cpu: String
        get() = "$cpuCores cores"

    val ram: String
        get() = if (ramMb % 1024 == 0) "${ramMb / 1024}GB" else "${ramMb}MB"

    val storage: String
        get() = "${storageGb}GB"
}
