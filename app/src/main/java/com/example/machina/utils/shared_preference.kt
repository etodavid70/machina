package com.example.machina.utils


import android.content.Context
import androidx.core.content.edit

private const val PREFS_NAME = "onboarding_prefs"
private const val KEY_ONBOARDING_SEEN = "onboarding_seen"
private const val KEY_EMAIL = "saved_email"
private const val KEY_USER_ID = "saved_user_id"

fun saveOnboardingSeen(context: Context) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit { putBoolean(KEY_ONBOARDING_SEEN, true) }
}

fun hasSeenOnboarding(context: Context): Boolean {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getBoolean(KEY_ONBOARDING_SEEN, false)
}


fun saveEmail(context: Context, email: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit { putString(KEY_EMAIL, email) }
}

fun getEmail(context: Context): String? {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getString(KEY_EMAIL, null)
}

fun saveUserId(context: Context, userId: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit { putString(KEY_USER_ID, userId) }
}

fun getUserId(context: Context): String? {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getString(KEY_USER_ID, null)
}
