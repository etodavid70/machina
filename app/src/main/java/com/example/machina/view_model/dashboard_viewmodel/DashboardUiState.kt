package com.example.machina.view_model.dashboard_viewmodel

import com.example.machina.data.model.dashboard_models.ActiveMachinery
import com.example.machina.view_model.auth_viewmodel.AuthUiState

sealed class DashboardUiState {
    object Idle : DashboardUiState()
    object Loading : DashboardUiState()
    data class Success(
        val message: String? = null,
    ) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}