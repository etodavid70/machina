package com.example.machina.view_model.dashboard_viewmodel

import com.example.machina.data.model.dashboard_models.ActiveMachinery

sealed class VmUiState {
    object Loading : VmUiState()
    data class Success(val data: List<ActiveMachinery>) : VmUiState()
    data class Error(val message: String) : VmUiState()
}