package com.example.machina.data.repository

import com.example.machina.data.model.DeviceRegistrationRequest
import com.example.machina.data.model.DeviceRegistrationResponse
import com.example.machina.data.remote.DeviceApi
import retrofit2.Response

class DeviceRepository(private val deviceApi: DeviceApi) {

    suspend fun registerFcmDevice(
        fcmToken: String,
        deviceName: String? = null
    ): Result<DeviceRegistrationResponse> {
        return try {
            val request = DeviceRegistrationRequest(
                fcmToken = fcmToken,
                deviceName = deviceName
            )
            val response = deviceApi.registerDevice(request)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to register device: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
