package com.example.machina.data.model.onboarding_models
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val message: String,
    val data: UserData,
    val access: String
)

data class UserData(
    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    val email: String,

    @SerializedName("date_of_birth")
    val dateOfBirth: String,

    @SerializedName("is_active")
    val isActive: Boolean,

    @SerializedName("is_subscribed")
    val isSubscribed: Boolean
)

//data class LoginResponse(
//    val message: String,
//    val data: UserData,
//    val access: String
//)
//
//data class UserData(
//    val first_name: String,
//    val last_name: String,
//    val email: String,
//    val date_of_birth: String,
//    val is_active: Boolean,
//    val is_subscribed: Boolean
//)

