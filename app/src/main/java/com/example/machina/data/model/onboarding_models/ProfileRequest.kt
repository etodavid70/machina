package com.example.machina.data.model.onboarding_models

import com.google.gson.annotations.SerializedName

data class ProfileRequest(
    @SerializedName("first_name")
    val firstName: String = "",
    @SerializedName("last_name")
    val lastName: String = "",
    @SerializedName("date_of_birth")
    val dob: String? = null,
    @SerializedName("gender")
    val gender: String = ""
)
