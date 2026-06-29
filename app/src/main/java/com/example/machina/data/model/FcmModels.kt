package com.example.machina.data.model

import com.google.gson.annotations.SerializedName

data class DeviceRegistrationRequest(
    @SerializedName("fcm_token")
    val fcmToken: String,
    @SerializedName("device_name")
    val deviceName: String? = null
)

data class DeviceRegistrationResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("device_id")
    val deviceId: String? = null
)
