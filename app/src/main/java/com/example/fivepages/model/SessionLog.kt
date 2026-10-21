package com.example.fivepages.model

data class SessionLog(
    val id: Int,
    val bookId: Int,
    val pages: Int,
    val loggedAtMillis: Long,
    val durationSeconds: Int = 0,
)