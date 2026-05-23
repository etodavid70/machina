package com.example.machina.view_model.dashboard_viewmodel
import androidx.lifecycle.ViewModel
import com.example.machina.data.repository.DashboardRepository
import androidx.lifecycle.viewModelScope
import com.example.machina.data.model.dashboard_models.CloudInstance
import com.example.machina.data.model.onboarding_models.PasswordChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.machina.utils.backendErrorMessage


class DashboardViewModel(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _instances = MutableStateFlow<List<CloudInstance>>(emptyList())
    val instances: StateFlow<List<CloudInstance>> = _instances

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _state = MutableStateFlow< DashboardUiState>(DashboardUiState.Idle)
    val state: StateFlow<DashboardUiState> = _state



    fun changePassword(passwordData: PasswordChangeRequest ) {

        viewModelScope.launch {

            _state.value = DashboardUiState.Loading

            try {
                repository.changePassword(passwordData)
                _state.value = DashboardUiState.Success("Password changed successfully.")
            } catch (e: Exception) {
                _state.value = DashboardUiState.Error(e.dashboardErrorMessage("Change Password failed"))
            }
        }
    }



    fun fetchInstances() {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                _instances.value = repository.getCloudInstances()
            } catch (e: Exception) {
                _errorMessage.value = e.backendErrorMessage("Failed to load cloud instances")
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun resetState() {
        _state.value = DashboardUiState.Idle
    }

    private fun Exception.dashboardErrorMessage(fallback: String): String {
        return backendErrorMessage(fallback)
    }
}
