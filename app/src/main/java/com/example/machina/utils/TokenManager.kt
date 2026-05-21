package com.example.machina.utils

import android.content.Context
import androidx.core.content.edit

class TokenManager(private val context: Context) {

    private val prefs =
        context.getSharedPreferences("machina_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit() { putString("access_token", token) }
    }

    fun getToken(): String? {
        return prefs.getString("access_token", null)
    }
}