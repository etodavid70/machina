package com.example.machina.data.model.dashboard_models

fun ServerInstance.toCloudInstance(): CloudInstance {
    return CloudInstance(
        id = id,
        connectionType = connectionType,
        username = username,
        port = port,
        publicIp = publicIp,
        privateIp = privateIp.orEmpty(),
        cpuCores = 0,
        ramMb = 0,
        storageGb = 0,
        osVersion = "",
        serviceProvider = serviceProvider,
        imageUrl = "",
        status = if (lastConnectedAt == null) "saved" else "connected",
        createdAt = createdAt,
        user = 0,
        mainOs = 0
    )
}
