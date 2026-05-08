package com.example.machina.data.model.dashboard_models

import com.google.gson.annotations.SerializedName

data class ServerInstance(

    @SerializedName("id")
    val id: Int,

    @SerializedName("connection_type")
    val connectionType: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("port")
    val port: Int,

    @SerializedName("password")
    val password: String?,

    @SerializedName("secret_key")
    val secretKey: String?,

    @SerializedName("cpu_cores")
    val cpuCores: Int,

    @SerializedName("ram_mb")
    val ramMb: Int,

    @SerializedName("storage_gb")
    val storageGb: Int,

    @SerializedName("os_version")
    val osVersion: String,

    @SerializedName("public_ip")
    val publicIp: String,

    @SerializedName("private_ip")
    val privateIp: String,

    @SerializedName("service_provider")
    val serviceProvider: String,

    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("user")
    val user: Int,

    @SerializedName("main_os")
    val mainOs: Int
)
