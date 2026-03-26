package com.example.machina.view_model



sealed class AuthUiState {

    object Idle : AuthUiState()

    object Loading : AuthUiState()

    data class Success(
        val step: Int
    ) : AuthUiState()

    data class Error(
        val message: String
    ) : AuthUiState()
}