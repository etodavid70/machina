package com.example.machina.view_model.dashboard_viewmodel
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.machina.data.repository.DashboardRepository
import androidx.lifecycle.viewModelScope
import com.example.machina.data.local.LocalInstanceStore
import com.example.machina.data.model.dashboard_models.ActiveMachinery
import com.example.machina.data.model.dashboard_models.SavedServer
import com.example.machina.data.model.dashboard_models.ServerInstance
import com.example.machina.data.model.onboarding_models.PasswordChangeRequest
import com.example.machina.data.model.onboarding_models.ProfileRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.machina.utils.backendErrorMessage
import com.example.machina.view_model.auth_viewmodel.UserSession
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException


class DashboardViewModel(
    private val repository: DashboardRepository,
    private val userSession: UserSession,
    private val localInstanceStore: LocalInstanceStore
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


    //for delete instance

    private val _deleteState =
        MutableStateFlow<DashboardUiState>(DashboardUiState.Idle)
    val deleteState = _deleteState.asStateFlow()

    //...
    var vmList by mutableStateOf<List<ActiveMachinery>>(emptyList())
        private set



//fun deleteInstance(id: Int) {
//    viewModelScope.launch {
//
//        _deleteState.value = DashboardUiState.Loading
//
//        try {
//           repository.deleteInstance(id.toString())
//            // Both the home card and the instances screen observe this flow. Update it
//            // once the server confirms deletion so Compose recomposes immediately.
//            _instances.value = _instances.value.filterNot { it.id == id }
//            _deleteState.value = DashboardUiState.Success("Instance deleted successfully")
//        } catch (e: Exception) {
//            _deleteState.value = DashboardUiState.Error(e.dashboardErrorMessage("Delete Instance failed"))
//        }
//    }
//}


    fun deleteInstance(id: Int) {
        viewModelScope.launch {
            _deleteState.value = DashboardUiState.Loading

            try {
                if (userSession.isSubscribed) {
                    repository.deleteInstance(id.toString())
                } else {
                    val deleted = localInstanceStore.deleteInstance(
                        ownerEmail = localOwnerEmail(),
                        id = id
                    )

                    check(deleted) { "Saved instance no longer exists." }
                }

                _instances.value = _instances.value.filterNot { it.id == id }

                _deleteState.value =
                    DashboardUiState.Success("Instance deleted successfully")
            } catch (e: Exception) {
                _deleteState.value =
                    DashboardUiState.Error(e.dashboardErrorMessage("Delete Instance failed"))
            }
        }
    }

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
                if (userSession.isSubscribed) {
                    repository.saveCloudInstance(saveCloudInstance)
                } else {
                    localInstanceStore.saveInstance(
                        ownerEmail = localOwnerEmail(),
                        savedServer = saveCloudInstance
                    )

                    _instances.value =
                        localInstanceStore.getInstances(localOwnerEmail())
                }

                _state.value =
                    DashboardUiState.Success("Cloud instance saved successfully.")
            } catch (e: Exception) {
                _state.value =
                    DashboardUiState.Error(e.dashboardErrorMessage("Saved Cloud failed"))
            }
        }
    }
//    fun saveCloudInstance(saveCloudInstance: SavedServer) {
//
//        viewModelScope.launch {
//
//            _state.value = DashboardUiState.Loading
//
//            try {
//                Log.d("save", "saving 3")
//                repository.saveCloudInstance(saveCloudInstance)
//
//                _state.value = DashboardUiState.Success("Cloud instance saved successfully.")
//            } catch (e: Exception) {
//                _state.value = DashboardUiState.Error(e.dashboardErrorMessage("Saved Cloud failed"))
//            }
//        }
//    }


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
                _instances.value =
                    if (userSession.isSubscribed) {
                        repository.getCloudInstances()
                    } else {
                        localInstanceStore.getInstances(localOwnerEmail())
                    }
            } catch (e: Exception) {
                _errorMessage.value =
                    e.backendErrorMessage("Failed to load cloud instances")
            } finally {
                _loading.value = false
            }
        }
    }

//    private fun CoroutineScope.localOwnerEmail(): String {}

//    fun fetchInstances() {
//        viewModelScope.launch {
//            _loading.value = true
//            _errorMessage.value = null
//            try {
//                _instances.value = repository.getCloudInstances()
//            } catch (e: Exception) {
//                _errorMessage.value = e.backendErrorMessage("Failed to load cloud instances")
//                e.printStackTrace()
//            } finally {
//                _loading.value = false
//            }
//        }
//    }

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
        _deleteState.value = DashboardUiState.Idle
    }

    private fun localOwnerEmail(): String {
        return userSession.user.value?.email
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?: throw IllegalStateException("User session is unavailable.")
    }

    private fun Exception.dashboardErrorMessage(fallback: String): String {
        return backendErrorMessage(fallback)
    }
}
