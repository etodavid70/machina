package com.example.machina.data.remote



import com.example.machina.data.model.onboarding_models.EmailRequest
import com.example.machina.data.model.onboarding_models.LoginRequest
import com.example.machina.data.model.onboarding_models.LoginResponse
import com.example.machina.data.model.onboarding_models.OTPVerifiedResponse
import com.example.machina.data.model.onboarding_models.PasswordRequest
import com.example.machina.data.model.onboarding_models.ProfileRequest
import com.example.machina.data.model.onboarding_models.VerifyCodeRequest
import com.example.machina.data.model.onboarding_models.VerifyCodeResponse
import com.example.machina.data.model.onboarding_models.VerifyOtpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.POST

interface AuthApi {



    @POST("auth/start-email-verification/")
    suspend fun sendEmail(
        @Body request: EmailRequest
    ): Response<Unit>

    @POST("auth/request-otp/")
    suspend fun sendOtp(
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
    ): Response<LoginResponse>

    @POST("auth/validate-otp/")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest
    ): Response<OTPVerifiedResponse>
}
