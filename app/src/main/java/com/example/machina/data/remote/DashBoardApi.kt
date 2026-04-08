package com.example.machina.data.remote
import com.example.machina.data.model.onboarding_models.EmailRequest
import com.example.machina.data.model.onboarding_models.LoginRequest
import com.example.machina.data.model.onboarding_models.PasswordRequest
import com.example.machina.data.model.onboarding_models.ProfileRequest
import com.example.machina.data.model.onboarding_models.VerifyCodeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DashBoardApi {

    @POST("dashboard/home/get-instances")
    suspend fun getCloudInstance(
        @Body request: EmailRequest
    ): Response<Unit>

    @POST("dashboard/home/active-machinery")
    suspend fun activeMachinery(
        @Body request: VerifyCodeRequest
    ): Response<Unit>

}