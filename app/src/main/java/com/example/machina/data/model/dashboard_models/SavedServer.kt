package com.example.machina.data.model.dashboard_models

import com.google.gson.annotations.SerializedName

data class SavedServer(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("name")
    val name: String,

    @SerializedName("connection_type")
    val connectionType: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("port")
    val port: Int,

    @SerializedName("public_ip")
    val publicIp: String,

    @SerializedName("private_ip")
    val privateIp: String?,

    @SerializedName("service_provider")
    val serviceProvider: String,

    @SerializedName("last_connected_at")
    val lastConnectedAt: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)
