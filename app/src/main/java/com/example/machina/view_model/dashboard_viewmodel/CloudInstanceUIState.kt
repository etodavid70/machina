package com.example.machina.view_model.dashboard_viewmodel
import com.example.machina.data.model.dashboard_models.CloudInstances


sealed class CloudInstanceUIState {
    object Loading : CloudInstanceUIState()
    data class Success(val data: List<CloudInstances>) : CloudInstanceUIState()
    data class Error(val message: String) : CloudInstanceUIState()
}