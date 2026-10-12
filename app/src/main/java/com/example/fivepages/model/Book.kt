package com.example.fivepages.model

data class Book(
    val id: Int,
    val title: String,
    val totalPages: Int,
    val targetDateMillis: Long,
    val pagesRead: Int = 0,
)

// 250 pages over 57 days -> 5 pages/day
fun dailyGoal(totalPages: Int, daysLeft: Int): Int {
    if (totalPages <= 0 || daysLeft <= 0) return 0
    return (totalPages + daysLeft - 1) / daysLeft
}