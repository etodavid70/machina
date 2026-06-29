package com.example.machina.data.remote

import com.example.machina.data.model.DeviceRegistrationRequest
import com.example.machina.data.model.DeviceRegistrationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DeviceApi {

    @POST("auth/device/register/")
    suspend fun registerDevice(
        @Body request: DeviceRegistrationRequest
    ): Response<DeviceRegistrationResponse>
}
