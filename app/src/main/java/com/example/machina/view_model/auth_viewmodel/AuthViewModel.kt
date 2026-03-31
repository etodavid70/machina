package com.example.machina.view_model.auth_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.machina.data.model.onboarding_models.ProfileRequest
import com.example.machina.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel (
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val state: StateFlow<AuthUiState> = _state

    private var email = ""

    fun sendEmail(email: String) {
        this.email = email

        viewModelScope.launch {
            _state.value = AuthUiState.Loading

            try {
                repository.sendEmail(email)
                _state.value = AuthUiState.Success(1)
            } catch (e: Exception) {
                _state.value = AuthUiState.Error("Failed")
            }
        }
    }

    fun verifyCode(code: String) {
        viewModelScope.launch {

            _state.value = AuthUiState.Loading

            try {
                repository.verifyCode(email, code)
                _state.value = AuthUiState.Success(2)
            } catch (e: Exception) {
                _state.value = AuthUiState.Error("Invalid code")
            }
        }
    }

    fun submitProfile(profile: ProfileRequest) {

        viewModelScope.launch {

            _state.value = AuthUiState.Loading

            try {
                repository.submitProfile(profile)
                _state.value = AuthUiState.Success(3)
            } catch (e: Exception) {
                _state.value = AuthUiState.Error("Profile failed")
            }
        }
    }

    fun setPassword(password: String, confirm: String) {

        viewModelScope.launch {

            _state.value = AuthUiState.Loading

            try {
                repository.setPassword(password, confirm)
                _state.value = AuthUiState.Success(4)
            } catch (e: Exception) {
                _state.value = AuthUiState.Error("Password failed")
            }
        }
    }


    fun login(email: String, password: String) {

        viewModelScope.launch {

            _state.value = AuthUiState.Loading

            try {
                repository.login(email, password)
                _state.value = AuthUiState.Success(4)
            } catch (e: Exception) {
                _state.value = AuthUiState.Error("Password failed")
            }
        }
    }
}