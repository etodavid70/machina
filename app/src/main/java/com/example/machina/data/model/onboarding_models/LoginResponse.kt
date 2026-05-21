package com.example.machina.data.model.onboarding_models

data class LoginResponse(
    val message: String,
    val data: UserData,
    val access: String
)

data class UserData(
    val first_name: String,
    val last_name: String,
    val email: String,
    val date_of_birth: String,
    val is_active: Boolean
)
