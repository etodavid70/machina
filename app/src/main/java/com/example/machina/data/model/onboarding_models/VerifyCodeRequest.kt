package com.example.machina.data.model.onboarding_models

data class VerifyCodeRequest(
    val email: String,
    val code: String
)