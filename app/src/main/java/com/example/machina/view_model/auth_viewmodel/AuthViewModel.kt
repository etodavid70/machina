package com.example.machina.view_model.auth_viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.machina.data.model.onboarding_models.PasswordRequest
import com.example.machina.data.model.onboarding_models.ProfileRequest
import com.example.machina.data.repository.AuthRepository
import retrofit2.HttpException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel (
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val state: StateFlow<AuthUiState> = _state



    fun sendEmail(email: String) {


        viewModelScope.launch {
            _state.value = AuthUiState.Loading

            try {
                repository.sendEmail(email)
                _state.value = AuthUiState.Success(AuthStep.EmailSent)
                Log.d("email sent", "successful")
            } catch (e: Exception) {
                _state.value = AuthUiState.Error(e.authErrorMessage("Failed"))
                Log.d("email failed", e.toString())
            }
        }
    }

    fun verifyCode(email: String, code: String) {
        viewModelScope.launch {

            _state.value = AuthUiState.Loading

            try {
                val userId = repository.verifyCode(email, code)
                Log.d("verify", email.toString())
                _state.value = AuthUiState.Success(AuthStep.EmailVerified, userId)
            } catch (e: Exception) {
                Log.d("verify error", e.toString())
                _state.value = AuthUiState.Error(e.authErrorMessage("Invalid code"))
            }
        }
    }

    fun submitProfile(userId: String, profile: ProfileRequest) {

        viewModelScope.launch {

            _state.value = AuthUiState.Loading
            Log.d("profile request", "userId=$userId, body=$profile")

            try {
                repository.submitProfile(userId, profile)
                _state.value = AuthUiState.Success(AuthStep.ProfileSubmitted)
            } catch (e: Exception) {
                val errorMessage = e.authErrorMessage("Profile failed")
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
                _state.value = AuthUiState.Error(errorMessage)
            }
        }
    }

    fun setPassword(userId: String, passwordData: PasswordRequest ) {

        viewModelScope.launch {

            _state.value = AuthUiState.Loading

            try {
                repository.setPassword(userId, passwordData)
                _state.value = AuthUiState.Success(AuthStep.PasswordSet)
            } catch (e: Exception) {
                _state.value = AuthUiState.Error(e.authErrorMessage("Password failed"))
            }
        }
    }


    fun login(email: String, password: String) {

        viewModelScope.launch {

            _state.value = AuthUiState.Loading
            Log.d("login", "logging in")
            try {
                repository.login(email, password)
                _state.value = AuthUiState.Success(AuthStep.LoggedIn)
                Log.d("login now", "success")
            } catch (e: Exception) {
                _state.value = AuthUiState.Error(e.authErrorMessage("Password failed"))
                Log.d("login failed", e.toString())
            }
        }
    }

    fun resetState() {
        _state.value = AuthUiState.Idle
    }

    private fun Exception.authErrorMessage(fallback: String): String {
        return if (this is HttpException) {
            response()?.errorBody()?.string()?.takeIf { it.isNotBlank() } ?: fallback
        } else {
            message?.takeIf { it.isNotBlank() } ?: fallback
        }
    }
}
