package com.example.machina.data.model.onboarding_models

data class PasswordRequest(
    val password: String,
    val confirm_password: String
)


data class PasswordChangeRequest(
    val old_password: String,
    val new_password: String,
    val confirm_password: String
)