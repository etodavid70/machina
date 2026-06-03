package com.example.machina.utils

import com.example.machina.data.remote.ApiConfig

/**
 * Backend often returns PythonAnywhere *file browser* URLs (login HTML), not direct image bytes.
 * Rewrite those to paths on the API host so Coil can load them (with auth when required).
 */
object OsImageUrl {

    private const val BACKEND_FILES_MARKER = "machina-backend/"

    fun resolve(rawUrl: String): String {
        if (rawUrl.isBlank()) return rawUrl

        val apiHost = ApiConfig.BASE_URL.removePrefix("https://").removeSuffix("/")

        if (rawUrl.contains(apiHost) && !rawUrl.contains("/user/")) {
            return rawUrl
        }

        val relativePath = when {
            rawUrl.contains(BACKEND_FILES_MARKER) ->
                rawUrl.substringAfter(BACKEND_FILES_MARKER, missingDelimiterValue = "")
            else -> rawUrl.substringAfterLast('/', missingDelimiterValue = rawUrl)
        }.trim().trimStart('/')

        if (relativePath.isBlank()) return rawUrl

        return "${ApiConfig.BASE_URL}media/$relativePath"
    }
}
