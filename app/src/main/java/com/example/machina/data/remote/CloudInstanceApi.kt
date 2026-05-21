package com.example.machina.data.remote
import com.example.machina.data.model.dashboard_models.ServerInstance
import com.example.machina.data.model.onboarding_models.PasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CloudInstanceApi {

        @GET("virtual-machines/cloud-instances/")
        suspend fun getCloudInstances(): List<ServerInstance>



}