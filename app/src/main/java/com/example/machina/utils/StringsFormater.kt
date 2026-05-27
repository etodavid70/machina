package com.example.machina.utils

import java.text.SimpleDateFormat
import java.util.*

fun formatDate(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("MMMM d", Locale.getDefault())

    val parsedDate = try {
        inputFormat.parse(date)
    } catch (e: Exception) {
        null
    } ?: return date
    val calendar = Calendar.getInstance()
    calendar.time = parsedDate

    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val suffix = when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }

    return "${outputFormat.format(parsedDate)}$suffix, ${
        SimpleDateFormat("yyyy", Locale.getDefault()).format(parsedDate)
    }"
}


fun toTitleCase(text: String): String {
    return text
        .trim()
        .split(Regex("\\s+"))
        .filter { it.isNotEmpty() }
        .joinToString(" ") { word ->
            word.lowercase()
                .replaceFirstChar { it.uppercase() }
        }
}
