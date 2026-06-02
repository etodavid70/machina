package com.example.machina.view_model.dashboard_viewmodel
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.machina.data.repository.DashboardRepository
import androidx.lifecycle.viewModelScope
import com.example.machina.data.model.dashboard_models.ActiveMachinery
import com.example.machina.data.model.dashboard_models.SavedServer
import com.example.machina.data.model.dashboard_models.ServerInstance
import com.example.machina.data.model.onboarding_models.PasswordChangeRequest
import com.example.machina.data.model.onboarding_models.ProfileRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.machina.utils.backendErrorMessage
import retrofit2.HttpException


class DashboardViewModel(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _instances = MutableStateFlow<List<ServerInstance>>(emptyList())
    val instances: StateFlow<List<ServerInstance>> = _instances

    private val _selectedInstance = MutableStateFlow<ServerInstance?>(null)
    val selectedInstance: StateFlow<ServerInstance?> = _selectedInstance

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _profile = MutableStateFlow(ProfileRequest())
    val profile: StateFlow<ProfileRequest> = _profile

    private val _profileLoading = MutableStateFlow(false)
    val profileLoading: StateFlow<Boolean> = _profileLoading

    private val _profileErrorMessage = MutableStateFlow<String?>(null)
    val profileErrorMessage: StateFlow<String?> = _profileErrorMessage

    private val _state = MutableStateFlow< DashboardUiState>(DashboardUiState.Idle)
    val state: StateFlow<DashboardUiState> = _state

    var vmList by mutableStateOf<List<ActiveMachinery>>(emptyList())
        private set



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

    fun saveCloudInstance(saveCloudInstance: SavedServer) {

        viewModelScope.launch {

            _state.value = DashboardUiState.Loading

            try {
                Log.d("save", "saving 3")
                repository.saveCloudInstance(saveCloudInstance)

                _state.value = DashboardUiState.Success("Cloud instance saved successfully.")
            } catch (e: Exception) {
                _state.value = DashboardUiState.Error(e.dashboardErrorMessage("Saved Cloud failed"))
            }
        }
    }


    fun editProfile(profile: ProfileRequest) {

        viewModelScope.launch {

            _state.value = DashboardUiState.Loading

            try {
                repository.editProfile( profile)
                _profile.value = profile
                _state.value = DashboardUiState.Success()
            } catch (e: Exception) {
                val errorMessage = e.dashboardErrorMessage("Profile failed")
                if (e is HttpException) {
                    val response = e.response()
                    Log.e(
                        "profile response",
                        "code=${e.code()}, message=${e.message()}, url=${response?.raw()?.request?.url}, body=$errorMessage",
                        e
                    )
                } else {
                    Log.e("profile response", "body=$errorMessage", e)
                }
                _state.value = DashboardUiState.Error(errorMessage)
            }
        }
    }

    fun fetchProfile() {
        viewModelScope.launch {
            _profileLoading.value = true
            _profileErrorMessage.value = null
            try {
                _profile.value = repository.getProfileInfo()
            } catch (e: Exception) {
                _profileErrorMessage.value = e.dashboardErrorMessage("Failed to load profile")
                if (e is HttpException) {
                    val response = e.response()
                    Log.e(
                        "profile response",
                        "code=${e.code()}, message=${e.message()}, url=${response?.raw()?.request?.url}, body=${_profileErrorMessage.value}",
                        e
                    )
                } else {
                    Log.e("profile response", "body=${_profileErrorMessage.value}", e)
                }
            } finally {
                _profileLoading.value = false
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

    fun selectInstance(instance: ServerInstance) {
        _selectedInstance.value = instance
    }

    fun clearSelectedInstance() {
        _selectedInstance.value = null
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
