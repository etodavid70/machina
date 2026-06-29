package com.example.machina.data.remote
import com.example.machina.data.model.DeviceRegistrationRequest
import com.example.machina.data.model.DeviceRegistrationResponse
import com.example.machina.data.model.createvm_models.MainOs
import com.example.machina.data.model.createvm_models.OperatingSystems
import com.example.machina.data.model.dashboard_models.SavedServer
import com.example.machina.data.model.dashboard_models.ServerInstance
import com.example.machina.data.model.onboarding_models.PasswordChangeRequest
import com.example.machina.data.model.onboarding_models.PasswordRequest
import com.example.machina.data.model.onboarding_models.ProfileRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DashboardApi {

        @GET("virtual-machines/main-os/")
        suspend fun getMainOs(): List<MainOs>

        @GET("virtual-machines/main-os/")
        suspend fun getOperatingSystems(
                @Query("os") os: String
        ): List<OperatingSystems>

        @GET("virtual-machines/saved-servers/")
        suspend fun getCloudInstances(): List<ServerInstance>

        @DELETE ("virtual-machines/saved-servers/{id}/")
        suspend fun deleteInstance(
                @Path("id") id: String,
        ): Response<Unit>

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