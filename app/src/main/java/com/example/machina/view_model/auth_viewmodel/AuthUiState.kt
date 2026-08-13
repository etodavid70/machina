package com.example.machina.view_model.auth_viewmodel

sealed class AuthUiState {
//Eto: when the state is idle
    object Idle : AuthUiState()

    //Eto: when the state is loading
    object Loading : AuthUiState()

    //for successful, for auth this will has different states -AuthStep
    data class Success(
        val step: AuthStep,
        val userId: String? = null,
    ) : AuthUiState()

    //when there is error, takes only a string
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
