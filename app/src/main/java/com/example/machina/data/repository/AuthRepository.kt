package com.example.machina.data.repository

import com.example.machina.data.model.*
import com.example.machina.data.model.onboarding_models.EmailRequest
import com.example.machina.data.model.onboarding_models.LoginRequest
import com.example.machina.data.model.onboarding_models.PasswordRequest
import com.example.machina.data.model.onboarding_models.ProfileRequest
import com.example.machina.data.model.onboarding_models.VerifyCodeRequest
import com.example.machina.data.remote.AuthApi
import retrofit2.HttpException
import retrofit2.Response

class AuthRepository (
    private val api: AuthApi
) {

    suspend fun sendEmail(email: String) {
        api.sendEmail(EmailRequest(email)).requireSuccessful()
    }

    suspend fun verifyCode(email: String, code: String) {
        api.verifyCode(VerifyCodeRequest(email, code)).requireSuccessful()
    }

    suspend fun submitProfile(profile: ProfileRequest) {
        api.submitProfile(profile).requireSuccessful()
    }

    suspend fun setPassword(password: String, confirm: String) {
        api.setPassword(PasswordRequest(password, confirm)).requireSuccessful()
    }

    suspend fun login(email: String, password: String) {
        api.login(LoginRequest(email, password)).requireSuccessful()
    }
}

private fun <T> Response<T>.requireSuccessful() {
    if (!isSuccessful) {
        throw HttpException(this)
    }
}
