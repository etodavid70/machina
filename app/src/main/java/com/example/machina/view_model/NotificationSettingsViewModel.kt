package com.example.machina.view_model

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.machina.data.repository.DeviceRepository
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class NotificationSettingsUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val fcmToken: String? = null,
    val deviceName: String? = null
)

class NotificationSettingsViewModel(
    private val deviceRepository: DeviceRepository,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationSettingsUiState())
    val uiState: StateFlow<NotificationSettingsUiState> = _uiState.asStateFlow()

    init {
        loadFcmToken()
    }

    private fun loadFcmToken() {
        viewModelScope.launch {
            try {
                val token = Firebase.messaging.token.await()
                _uiState.value = _uiState.value.copy(fcmToken = token)
                Log.d("FCM", "Token loaded: $token")
            } catch (e: Exception) {
                Log.w("FCM", "Failed to get FCM token", e)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to retrieve FCM token: ${e.message}"
                )
            }
        }
    }

    fun registerFcmDevice(deviceName: String? = null) {
        val fcmToken = _uiState.value.fcmToken
        
        if (fcmToken == null) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "FCM token not available. Please try again."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            try {
                val finalDeviceName = deviceName ?: generateDeviceName()
                val result = deviceRepository.registerFcmDevice(fcmToken, finalDeviceName)
                
                result.onSuccess { response ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        deviceName = finalDeviceName,
                        errorMessage = null
                    )
                    // Save FCM registration state
                    saveFcmRegistrationState(true)
                    Log.d("FCM", "Device registered successfully")
                }
                
                result.onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = false,
                        errorMessage = exception.message ?: "Failed to register device"
                    )
                    Log.e("FCM", "Failed to register device", exception)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = false,
                    errorMessage = e.message ?: "An error occurred"
                )
                Log.e("FCM", "Registration error", e)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }

    private fun generateDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return "$manufacturer $model"
    }

    private fun saveFcmRegistrationState(isRegistered: Boolean) {
        val sharedPref = context.getSharedPreferences("fcm_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("is_registered", isRegistered).apply()
    }

    fun isFcmRegistered(): Boolean {
        val sharedPref = context.getSharedPreferences("fcm_prefs", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("is_registered", false)
    }
}
