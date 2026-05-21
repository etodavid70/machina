package com.example.machina.data.repository

import com.example.machina.data.model.onboarding_models.EmailRequest
import com.example.machina.data.model.onboarding_models.LoginRequest
import com.example.machina.data.model.onboarding_models.PasswordRequest
import com.example.machina.data.model.onboarding_models.ProfileRequest
import com.example.machina.data.model.onboarding_models.VerifyCodeRequest
import com.example.machina.data.model.onboarding_models.VerifyOtpRequest
import com.example.machina.data.remote.AuthenticatedAuthApi
import com.example.machina.data.remote.AuthApi
import retrofit2.HttpException
import retrofit2.Response

class AuthRepository (
    private val api: AuthApi,
    private val authenticatedApi: AuthenticatedAuthApi
) {



    suspend fun sendEmail(email: String) {
        api.sendEmail(EmailRequest(email)).requireSuccessful()
    }

    suspend fun sendOtp(email: String) {
        api.sendOtp(EmailRequest(email)).requireSuccessful()
    }


    suspend fun verifyCode(email: String, code: String): String? {
        return api.verifyCode(VerifyCodeRequest(email, code))
            .requireSuccessful()
            ?.resolvedUserId()
    }

    suspend fun submitProfile(userId: String, profile: ProfileRequest) {
        api.submitProfile(userId, profile).requireSuccessful()
    }

    suspend fun setPassword(userId: String, passwordData:  PasswordRequest) {
        api.setPassword(userId, passwordData).requireSuccessful()
    }

    suspend fun resetPassword( passwordData:  PasswordRequest) {
        authenticatedApi.resetPassword( passwordData).requireSuccessful()
    }

//    suspend fun login(email: String, password: String) {
//        api.login(LoginRequest(email, password)).requireSuccessful()
//    }

    suspend fun login( email: String, password: String): String {
        val response = api.login(LoginRequest(email, password)) .requireSuccessful()
        return response?.access ?: ""

    }

    suspend fun verifyOtp(email: String, otp: String): String? {
        val response = api.verifyOtp(VerifyOtpRequest(email, otp))
            .requireSuccessful()
        return response?.access ?: ""
    }
}

private fun <T> Response<T>.requireSuccessful(): T? {
    if (!isSuccessful) {
        throw HttpException(this)
    }
    return body()
}
