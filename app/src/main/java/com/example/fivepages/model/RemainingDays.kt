package com.example.fivepages.model

private const val DAY_MS = 86_400_000L

fun remainingDays(targetDateMillis: Long, now: Long = System.currentTimeMillis()): Int {
    val days = ((targetDateMillis - now) / DAY_MS).toInt() + 1
    return if (days > 0) days else 1
}