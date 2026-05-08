package com.example.machina.data.remote
import com.example.machina.data.model.dashboard_models.ServerInstance
import retrofit2.http.GET

interface DashBoardApi {

        @GET("virtual-machines/cloud-instances/")
        suspend fun getInstances(): List<ServerInstance>


}