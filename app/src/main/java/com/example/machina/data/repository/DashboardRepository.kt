package com.example.machina.data.repository
import com.example.machina.data.model.dashboard_models.CloudInstance
import com.example.machina.data.model.dashboard_models.toCloudInstance
import com.example.machina.data.model.onboarding_models.PasswordChangeRequest
import com.example.machina.data.model.onboarding_models.ProfileRequest
import com.example.machina.data.remote.DashboardApi
import retrofit2.HttpException
import retrofit2.Response

class DashboardRepository(private val api: DashboardApi) {

    suspend fun getCloudInstances(): List<CloudInstance> {
        return api.getCloudInstances().map { it.toCloudInstance() }
    }

    suspend fun changePassword(passwordData: PasswordChangeRequest) {
        api.changePassword( passwordData).requireSuccessful()
    }

    suspend fun editProfile(profileDetails: ProfileRequest){
        api.editProfile( profileDetails).requireSuccessful()

    }

    suspend fun getProfileInfo(): ProfileRequest {
        return api.getProfileInfo().requireSuccessful() ?: ProfileRequest()
    }
}

private fun <T> Response<T>.requireSuccessful(): T? {
    if (!isSuccessful) {
        throw HttpException(this)
    }
    return body()
}


