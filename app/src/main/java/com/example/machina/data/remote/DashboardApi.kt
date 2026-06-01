package com.example.machina.data.remote
import com.example.machina.data.model.dashboard_models.SavedServer
import com.example.machina.data.model.dashboard_models.ServerInstance
import com.example.machina.data.model.onboarding_models.PasswordChangeRequest
import com.example.machina.data.model.onboarding_models.PasswordRequest
import com.example.machina.data.model.onboarding_models.ProfileRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DashboardApi {

        @GET("virtual-machines/saved-servers/")
        suspend fun getCloudInstances(): List<ServerInstance>

        @POST ("virtual-machines/saved-servers/")
        suspend fun saveCloudInstances(
                @Body request: SavedServer
        ): Response<Unit>

        @POST("auth/password_change/")
        suspend fun changePassword(
                @Body request: PasswordChangeRequest
        ): Response<Unit>

        @POST("auth/profile/")
        suspend fun editProfile(
                @Body request: ProfileRequest
        ): Response<Unit>

        @GET("auth/profile/")
        suspend fun getProfileInfo(
        ): Response<ProfileRequest>

}