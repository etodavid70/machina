package com.example.machina.data.repository

import com.example.machina.data.model.*
import com.example.machina.data.model.onboarding_models.EmailRequest
import com.example.machina.data.model.onboarding_models.LoginRequest
import com.example.machina.data.model.onboarding_models.PasswordRequest
import com.example.machina.data.model.onboarding_models.ProfileRequest
import com.example.machina.data.model.onboarding_models.VerifyCodeRequest
import com.example.machina.data.remote.AuthApi

class AuthRepository (
    private val api: AuthApi
) {

    suspend fun sendEmail(email: String) {
        api.sendEmail(EmailRequest(email))
    }

    suspend fun verifyCode(email: String, code: String) {
        api.verifyCode(VerifyCodeRequest(email, code))
    }

    suspend fun submitProfile(profile: ProfileRequest) {
        api.submitProfile(profile)
    }

    suspend fun setPassword(password: String, confirm: String) {
        api.setPassword(PasswordRequest(password, confirm))
    }

    suspend fun login(email: String, password: String) {
        api.login(LoginRequest(email, password))
    }
}