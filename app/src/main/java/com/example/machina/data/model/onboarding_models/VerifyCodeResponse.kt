package com.example.machina.data.model.onboarding_models

import com.google.gson.annotations.SerializedName

data class VerifyCodeResponse(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("userId")
    val userId: String? = null,
    @SerializedName("user_id")
    val userIdSnakeCase: String? = null,
    @SerializedName("user")
    val user: VerifiedUser? = null
) {
    fun resolvedUserId(): String? = userId
        ?: userIdSnakeCase
        ?: user?.id
        ?: id
}

data class VerifiedUser(
    @SerializedName("id")
    val id: String? = null
)
