package com.example.machina.utils


import android.content.Context
import androidx.core.content.edit

private const val PREFS_NAME = "onboarding_prefs"
private const val KEY_ONBOARDING_SEEN = "onboarding_seen"

fun saveOnboardingSeen(context: Context) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit { putBoolean(KEY_ONBOARDING_SEEN, true) }
}

fun hasSeenOnboarding(context: Context): Boolean {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getBoolean(KEY_ONBOARDING_SEEN, false)
}
