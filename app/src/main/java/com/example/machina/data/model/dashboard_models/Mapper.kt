package com.example.machina.data.model.dashboard_models

fun ServerInstance.toCloudInstance(): CloudInstance {
    return CloudInstance(
        id = id,
        connectionType = connectionType,
        username = username,
        port = port,
        publicIp = publicIp,
        privateIp = privateIp,
        cpuCores = cpuCores,
        ramMb = ramMb,
        storageGb = storageGb,
        osVersion = osVersion,
        serviceProvider = serviceProvider,
        imageUrl = imageUrl,
        status = status,
        createdAt = createdAt,
        user = user,
        mainOs = mainOs,
        password = password,
        secretKey = secretKey
    )
}
