package com.example.machina.data.remote
import com.example.machina.data.model.dashboard_models.ServerInstance
import retrofit2.http.GET

interface CloudInstanceApi {

        @GET("virtual-machines/cloud-instances/")
        suspend fun getCloudInstances(): List<ServerInstance>


}