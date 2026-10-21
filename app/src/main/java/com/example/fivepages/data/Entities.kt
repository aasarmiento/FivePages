package com.example.fivepages.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.fivepages.model.Book
import com.example.fivepages.model.ReminderWindow
import com.example.fivepages.model.SessionLog

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val totalPages: Int,
    val targetDateMillis: Long,
    val pagesRead: Int,
)

@Entity(
    tableName = "sessions",
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("bookId")]
)
data class SessionEntity(
    @PrimaryKey val id: Int,
    val bookId: Int,
    val pages: Int,
    val loggedAtMillis: Long,
    val durationSeconds: Int,
)

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey val id: Int,
    val label: String,
    val hour: Int,
    val minute: Int,
    val enabled: Boolean,
)

fun Book.toEntity() = BookEntity(id, title, totalPages, targetDateMillis, pagesRead)

fun BookEntity.toModel() = Book(id, title, totalPages, targetDateMillis, pagesRead)

fun SessionLog.toEntity() = SessionEntity(id, bookId, pages, loggedAtMillis, durationSeconds)

fun SessionEntity.toModel() = SessionLog(id, bookId, pages, loggedAtMillis, durationSeconds)

fun ReminderWindow.toEntity() = ReminderEntity(id, label, hour, minute, enabled)

fun ReminderEntity.toModel() = ReminderWindow(id, label, hour, minute, enabled)