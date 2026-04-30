package com.example.machina.utils

import java.io.File

fun getCpuInfo(): String {
    return try {
        File("/proc/cpuinfo").readText()
    } catch (e: Exception) {
        "Unable to read CPU info: ${e.message}"
    }
}