package com.example.machina.data.remote

import com.example.machina.data.model.onboarding_models.PasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticatedAuthApi {

    @POST("auth/password_reset/")
    suspend fun resetPassword(
        @Body request: PasswordRequest
    ): Response<Unit>
}
