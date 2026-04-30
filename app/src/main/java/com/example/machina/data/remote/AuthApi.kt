package com.example.machina.data.remote



import com.example.machina.data.model.*
import com.example.machina.data.model.onboarding_models.EmailRequest
import com.example.machina.data.model.onboarding_models.LoginRequest
import com.example.machina.data.model.onboarding_models.PasswordRequest
import com.example.machina.data.model.onboarding_models.ProfileRequest
import com.example.machina.data.model.onboarding_models.VerifyCodeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/start-email-verification/")
    suspend fun sendEmail(
        @Body request: EmailRequest
    ): Response<Unit>

    @POST("auth/verify-email/")
    suspend fun verifyCode(
        @Body request: VerifyCodeRequest
    ): Response<Unit>

    @POST("auth/profile")
    suspend fun submitProfile(
        @Body request: ProfileRequest
    ): Response<Unit>

    @POST("auth/password")
    suspend fun setPassword(
        @Body request: PasswordRequest
    ): Response<Unit>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<Unit>
}