package com.example.machina.data.repository
import com.example.machina.data.model.dashboard_models.CloudInstance
import com.example.machina.data.model.dashboard_models.toCloudInstance
import com.example.machina.data.remote.CloudInstanceApi

class CloudInstanceRepository(private val api: CloudInstanceApi) {

    suspend fun getCloudInstances(): List<CloudInstance> {
        return api.getCloudInstances().map { it.toCloudInstance() }
    }
}



