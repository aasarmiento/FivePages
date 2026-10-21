package com.example.fivepages.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatDuration(seconds: Int): String {
    if (seconds <= 0) return "—"
    val totalMinutes = ((seconds + 30) / 60).coerceAtLeast(1)
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return when {
        hours == 0 -> "${minutes}m"
        minutes == 0 -> "${hours}h"
        else -> "${hours}h ${minutes}m"
    }
}

fun pagesLabel(pages: Int): String = if (pages == 1) "1 page" else "$pages pages"

fun formatSessionDate(millis: Long, tz: TimeZone = TimeZone.getDefault()): String {
    val format = SimpleDateFormat("MM/dd/yy", Locale.US)
    format.timeZone = tz
    return format.format(Date(millis))
}

fun sessionsForBook(sessions: List<SessionLog>, bookId: Int): List<SessionLog> =
    sessions
        .filter { it.bookId == bookId }
        .sortedWith(compareByDescending<SessionLog> { it.loggedAtMillis }.thenByDescending { it.id })