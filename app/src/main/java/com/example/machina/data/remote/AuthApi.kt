package com.example.machina.data.remote



import com.example.machina.data.model.*
import com.example.machina.data.model.onboarding_models.EmailRequest
import com.example.machina.data.model.onboarding_models.LoginRequest
import com.example.machina.data.model.onboarding_models.PasswordRequest
import com.example.machina.data.model.onboarding_models.ProfileRequest
import com.example.machina.data.model.onboarding_models.VerifyCodeRequest
import com.example.machina.data.model.onboarding_models.VerifyCodeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/start-email-verification/")
    suspend fun sendEmail(
        @Body request: EmailRequest
    ): Response<Unit>

    @POST("auth/verify-email/")
    suspend fun verifyCode(
        @Body request: VerifyCodeRequest
    ): Response<VerifyCodeResponse>

    @POST("auth/user-details/{userId}")
    suspend fun submitProfile(
        @Path("userId") userId: String,
        @Body request: ProfileRequest
    ): Response<Unit>

    @POST("auth/set-password/{userId}")
    suspend fun setPassword(
        @Path("userId") userId: String,
        @Body request: PasswordRequest
    ): Response<Unit>

    @POST("auth/login/")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<Unit>
}
