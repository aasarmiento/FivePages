package com.example.fivepages.model

import java.util.Calendar
import java.util.TimeZone

data class WeekDay(
    val letter: String,
    val name: String,
    val pages: Int,
    val isToday: Boolean,
    val isFuture: Boolean,
)

data class ProgressSummary(
    val dayStreak: Int,
    val booksActive: Int,
    val sessionsThisMonth: Int,
    val week: List<WeekDay>,
)

private const val DAY_MS = 86_400_000L
private val WEEK_LETTERS = listOf("M", "T", "W", "T", "F", "S", "S")
private val WEEK_NAMES = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

fun percentDone(pagesRead: Int, totalPages: Int): Int =
    if (totalPages > 0) (pagesRead * 100 / totalPages).coerceIn(0, 100) else 0

fun progressSegments(pagesRead: Int, totalPages: Int, segments: Int = 10): Int =
    if (pagesRead <= 0 || totalPages <= 0) 0
    else ((pagesRead * segments + totalPages - 1) / totalPages).coerceAtMost(segments)

fun dayIndex(millis: Long, tz: TimeZone = TimeZone.getDefault()): Long =
    Math.floorDiv(millis + tz.getOffset(millis), DAY_MS)

fun currentStreak(sessionDays: Set<Long>, today: Long): Int {
    var day = if (today in sessionDays) today else today - 1
    var count = 0
    while (day in sessionDays) {
        count++
        day--
    }
    return count
}

fun weekDays(
    sessions: List<SessionLog>,
    today: Long,
    tz: TimeZone = TimeZone.getDefault(),
): List<WeekDay> {
    val monday = today - Math.floorMod(today + 3L, 7L)
    val pagesByDay = sessions
        .groupBy { dayIndex(it.loggedAtMillis, tz) }
        .mapValues { (_, logs) -> logs.sumOf { it.pages } }
    return WEEK_LETTERS.indices.map { i ->
        val day = monday + i
        WeekDay(
            letter = WEEK_LETTERS[i],
            name = WEEK_NAMES[i],
            pages = pagesByDay[day] ?: 0,
            isToday = day == today,
            isFuture = day > today,
        )
    }
}

fun sessionsThisMonth(
    sessions: List<SessionLog>,
    now: Long,
    tz: TimeZone = TimeZone.getDefault(),
): Int {
    val monthStart = Calendar.getInstance(tz).apply {
        timeInMillis = now
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    return sessions.count { it.loggedAtMillis >= monthStart }
}

fun buildProgressSummary(
    books: List<Book>,
    sessions: List<SessionLog>,
    now: Long,
    tz: TimeZone = TimeZone.getDefault(),
): ProgressSummary {
    val today = dayIndex(now, tz)
    val sessionDays = sessions.map { dayIndex(it.loggedAtMillis, tz) }.toSet()
    return ProgressSummary(
        dayStreak = currentStreak(sessionDays, today),
        booksActive = books.count { it.pagesRead < it.totalPages },
        sessionsThisMonth = sessionsThisMonth(sessions, now, tz),
        week = weekDays(sessions, today, tz),
    )
}