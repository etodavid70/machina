package com.example.machina.data.repository
import android.util.Log
import com.example.machina.data.model.createvm_models.MainOs
import com.example.machina.data.model.createvm_models.OperatingSystems
import com.example.machina.data.model.dashboard_models.SavedServer
import com.example.machina.data.model.dashboard_models.ServerInstance
import com.example.machina.data.model.onboarding_models.PasswordChangeRequest
import com.example.machina.data.model.onboarding_models.ProfileRequest
import com.example.machina.data.remote.DashboardApi
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response


class DashboardRepository(private val api: DashboardApi) {

    suspend fun deleteInstance(id: String) {

        api.deleteInstance(id).requireSuccessful()
    }

    suspend fun getMainOs(): List<MainOs> {
        return api.getMainOs()
    }

    suspend fun getOperatingSystems(os: String): List<OperatingSystems> {
        return api.getOperatingSystems(os)
    }


    suspend fun getCloudInstances(): List<ServerInstance> {
        return api.getCloudInstances()
    }

    suspend fun saveCloudInstance(savedInstance: SavedServer) {
        try {
            val response = api.saveCloudInstances(savedInstance)
            response.requireSuccessful()
        } catch (e: Exception) {
            Log.e("save", "Error saving instance", e)
        }
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
//        throw HttpException(this)
        extractError(errorBody()?.string())
    }
    return body()
}


private fun extractError(errorBody: String?): String {
    if (errorBody == null) return "Unknown error"

    return try {
        val json = JSONObject(errorBody)

        json.keys().asSequence()
            .firstOrNull()
            ?.let { key ->
                json.getJSONArray(key).getString(0)
            }
            ?: "Unknown error"

    } catch (e: Exception) {
        errorBody
    }
}

