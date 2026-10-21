package com.example.fivepages.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface AppDao {

    @Query("SELECT * FROM books")
    suspend fun getBooks(): List<BookEntity>

    @Upsert
    suspend fun upsertBook(book: BookEntity)

    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun deleteBookById(bookId: Int)

    @Query("SELECT * FROM sessions")
    suspend fun getSessions(): List<SessionEntity>

    @Upsert
    suspend fun upsertSession(session: SessionEntity)

    @Query("DELETE FROM sessions WHERE id = :sessionId")
    suspend fun deleteSessionById(sessionId: Int)

    @Query("SELECT * FROM reminders")
    suspend fun getReminders(): List<ReminderEntity>

    @Upsert
    suspend fun upsertReminder(reminder: ReminderEntity)

    @Query("DELETE FROM reminders WHERE id = :reminderId")
    suspend fun deleteReminderById(reminderId: Int)
}