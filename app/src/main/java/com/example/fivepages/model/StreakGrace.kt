package com.example.fivepages.model

import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone

const val FREEZES_PER_MONTH = 1

private const val DAY_MS = 86_400_000L

fun monthKey(day: Long): Int {
    val calendar = GregorianCalendar(TimeZone.getTimeZone("UTC"))
    calendar.timeInMillis = day * DAY_MS
    return calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH)
}

fun streakEndingAt(day: Long, sessionDays: Set<Long>, frozenDays: Set<Long>): Int {
    var current = day
    var count = 0
    while (current in sessionDays || current in frozenDays) {
        if (current in sessionDays) count++
        current--
    }
    return count
}

fun streakWithFreezes(sessionDays: Set<Long>, frozenDays: Set<Long>, today: Long): Int {
    val start = if (today in sessionDays) today else today - 1
    return streakEndingAt(start, sessionDays, frozenDays)
}

fun missedYesterdayStreak(sessionDays: Set<Long>, frozenDays: Set<Long>, today: Long): Int {
    val yesterday = today - 1
    if (today in sessionDays || yesterday in sessionDays || yesterday in frozenDays) return 0
    return streakEndingAt(today - 2, sessionDays, frozenDays)
}

fun freezesLeft(frozenDays: Set<Long>, today: Long): Int {
    val used = frozenDays.count { monthKey(it) == monthKey(today) }
    return (FREEZES_PER_MONTH - used).coerceAtLeast(0)
}