package com.example.machina.utils

import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException

fun Throwable.backendErrorMessage(fallback: String): String {
    return if (this is HttpException) {
        response()
            ?.errorBody()
            ?.string()
            ?.let { parseBackendErrorMessage(it) }
            ?.takeIf { it.isNotBlank() }
            ?: fallback
    } else {
        message?.takeIf { it.isNotBlank() } ?: fallback
    }
}

private fun parseBackendErrorMessage(errorBody: String): String {
    val trimmedBody = errorBody.trim()
    if (trimmedBody.isBlank()) return ""

    return runCatching {
        val json = JSONObject(trimmedBody)
        val possibleMessageKeys = listOf("message", "detail", "error", "errors")

        possibleMessageKeys.firstNotNullOfOrNull { key ->
            if (json.has(key) && !json.isNull(key)) {
                json.get(key).toReadableErrorMessage()
            } else {
                null
            }
        } ?: trimmedBody
    }.getOrElse {
        trimmedBody
    }
}

private fun Any.toReadableErrorMessage(): String {
    return when (this) {
        is JSONArray -> {
            (0 until length())
                .mapNotNull { index -> get(index).toReadableErrorMessage().takeIf { it.isNotBlank() } }
                .joinToString("\n")
        }
        is JSONObject -> {
            keys().asSequence()
                .mapNotNull { key ->
                    get(key).toReadableErrorMessage().takeIf { it.isNotBlank() }?.let { "$key: $it" }
                }
                .joinToString("\n")
        }
        else -> toString()
    }
}
