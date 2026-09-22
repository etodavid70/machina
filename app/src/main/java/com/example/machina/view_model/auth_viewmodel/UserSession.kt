package com.example.machina.view_model.auth_viewmodel
import com.example.machina.data.model.onboarding_models.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserSession {

    private val _user = MutableStateFlow<UserData?>(null)
    val user: StateFlow<UserData?> = _user.asStateFlow()

    fun setUser(userData: UserData) {
        _user.value = userData
    }

    fun clearUser() {
        _user.value = null
    }

    val isSubscribed: Boolean
        get() = _user.value?.isSubscribed == true
}