package com.example.machina.view_model.auth_viewmodel

sealed class AuthUiState {

    object Idle : AuthUiState()

    object Loading : AuthUiState()

    data class Success(
        val step: AuthStep
    ) : AuthUiState()

    data class Error(
        val message: String
    ) : AuthUiState()
}

enum class AuthStep {
    EmailSent,
    EmailVerified,
    ProfileSubmitted,
    PasswordSet,
    LoggedIn
}
