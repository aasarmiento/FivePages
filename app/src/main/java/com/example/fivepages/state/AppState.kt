package com.example.fivepages.state

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fivepages.data.AppDao
import com.example.fivepages.data.FivePagesDatabase
import com.example.fivepages.data.toEntity
import com.example.fivepages.data.toModel
import com.example.fivepages.model.Book
import com.example.fivepages.model.ProgressSummary
import com.example.fivepages.model.ReminderWindow
import com.example.fivepages.model.SessionLog
import com.example.fivepages.model.buildProgressSummary
import com.example.fivepages.model.currentStreak
import com.example.fivepages.model.dayIndex
import com.example.fivepages.model.freezesLeft
import com.example.fivepages.model.missedYesterdayStreak
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppState(application: Application) : AndroidViewModel(application) {
    private val dao = FivePagesDatabase.get(application).dao()
    private val prefs = application.getSharedPreferences("fivepages", Context.MODE_PRIVATE)
    private val writes = Channel<suspend AppDao.() -> Unit>(Channel.UNLIMITED)

    val books = mutableStateListOf<Book>()
    val sessions = mutableStateListOf<SessionLog>()

    var pendingDurationSeconds = 0

    //Reminder Windows (starts with the two examples from Figma)
    val reminderWindows = mutableStateListOf<ReminderWindow>()

    var isLoaded by mutableStateOf(false)
        private set

    private var frozenDays by mutableStateOf(loadFrozenDays())
    private var freshStartDay by mutableStateOf(prefs.getLong(KEY_FRESH_START_DAY, -1L))

    val onboarded: Boolean
        get() = prefs.getBoolean(KEY_ONBOARDED, false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val savedBooks = dao.getBooks().map { it.toModel() }
            val savedSessions = dao.getSessions().map { it.toModel() }
            var savedReminders = dao.getReminders().map { it.toModel() }

            if (savedReminders.isEmpty() && !prefs.getBoolean(KEY_SEEDED, false)) {
                savedReminders = listOf(
                    ReminderWindow(1, "Morning coffee", 8, 0),
                    ReminderWindow(2, "Post-lunch", 13, 0),
                )
                savedReminders.forEach { dao.upsertReminder(it.toEntity()) }
                prefs.edit().putBoolean(KEY_SEEDED, true).apply()
            }

            withContext(Dispatchers.Main) {
                books.addAll(savedBooks)
                sessions.addAll(savedSessions)
                reminderWindows.addAll(savedReminders)
                isLoaded = true
            }

            for (job in writes) {
                try {
                    dao.job()
                } catch (e: Exception) {
                    Log.e("AppState", "Could not save to the database", e)
                }
            }
        }
    }

    private fun persist(job: suspend AppDao.() -> Unit) {
        writes.trySend(job)
    }

    fun markOnboarded() {
        prefs.edit().putBoolean(KEY_ONBOARDED, true).apply()
    }

    fun setReminderEnabled(id: Int, enabled: Boolean) {
        val index = reminderWindows.indexOfFirst { it.id == id }
        if (index >= 0) {
            val updated = reminderWindows[index].copy(enabled = enabled)
            reminderWindows[index] = updated
            persist { upsertReminder(updated.toEntity()) }
        }
    }

    //Save Reminder (adds a new one if the id is not in the list)
    fun saveReminder(id: Int, label: String, hour: Int, minute: Int, enabled: Boolean) {
        val index = reminderWindows.indexOfFirst { it.id == id }
        val saved: ReminderWindow
        if (index >= 0) {
            saved = reminderWindows[index].copy(
                label = label, hour = hour, minute = minute, enabled = enabled
            )
            reminderWindows[index] = saved
        } else {
            val newId = (reminderWindows.maxOfOrNull { it.id } ?: 0) + 1
            saved = ReminderWindow(newId, label, hour, minute, enabled)
            reminderWindows.add(saved)
        }
        persist { upsertReminder(saved.toEntity()) }
    }

    //Delete Reminder
    fun deleteReminder(id: Int) {
        reminderWindows.removeAll { it.id == id }
        persist { deleteReminderById(id) }
    }

    fun addBook(title: String, totalPages: Int, targetDateMillis: Long): Int {
        val id = (books.maxOfOrNull { it.id } ?: 0) + 1
        val book = Book(id, title, totalPages, targetDateMillis)
        books.add(book)
        persist { upsertBook(book.toEntity()) }
        return id
    }

    fun logPages(bookId: Int, pages: Int) {
        val i = books.indexOfFirst { it.id == bookId }
        if (i < 0 || pages <= 0) return
        val b = books[i]
        val updatedPages = (b.pagesRead + pages).coerceAtMost(b.totalPages)
        val added = updatedPages - b.pagesRead
        if (added <= 0) return
        val updatedBook = b.copy(pagesRead = updatedPages)
        books[i] = updatedBook
        val id = (sessions.maxOfOrNull { it.id } ?: 0) + 1
        val session = SessionLog(id, bookId, added, System.currentTimeMillis(), pendingDurationSeconds)
        pendingDurationSeconds = 0
        sessions.add(session)
        persist {
            upsertBook(updatedBook.toEntity())
            upsertSession(session.toEntity())
        }
    }

    fun undoLastSession(): Int? {
        val last = sessions.maxByOrNull { it.id } ?: return null
        sessions.remove(last)
        val i = books.indexOfFirst { it.id == last.bookId }
        val restored: Book? = if (i >= 0) {
            val b = books[i]
            b.copy(pagesRead = (b.pagesRead - last.pages).coerceAtLeast(0)).also { books[i] = it }
        } else null
        persist {
            deleteSessionById(last.id)
            if (restored != null) upsertBook(restored.toEntity())
        }
        return last.bookId
    }

    fun progressSummary(now: Long = System.currentTimeMillis()): ProgressSummary =
        buildProgressSummary(books, sessions, now, frozenDays = frozenDays)

    fun titleExists(title: String, exceptId: Int? = null): Boolean {
        val wanted = normalizeTitle(title)
        return books.any { it.id != exceptId && normalizeTitle(it.title) == wanted }
    }

    private fun normalizeTitle(title: String): String =
        title.trim().replace(Regex("\\s+"), " ").lowercase()

    fun renameBook(bookId: Int, newTitle: String) {
        val i = books.indexOfFirst { it.id == bookId }
        if (i < 0 || newTitle.isBlank() || titleExists(newTitle, bookId)) return
        val updated = books[i].copy(title = newTitle.trim())
        books[i] = updated
        persist { upsertBook(updated.toEntity()) }
    }

    fun updateGoal(bookId: Int, totalPages: Int, targetDateMillis: Long) {
        val i = books.indexOfFirst { it.id == bookId }
        if (i < 0) return
        val b = books[i]
        if (totalPages <= 0 || totalPages < b.pagesRead) return
        val updated = b.copy(totalPages = totalPages, targetDateMillis = targetDateMillis)
        books[i] = updated
        persist { upsertBook(updated.toEntity()) }
    }

    fun streakFor(bookId: Int, now: Long = System.currentTimeMillis()): Int {
        val days = sessions.filter { it.bookId == bookId }.map { dayIndex(it.loggedAtMillis) }.toSet()
        return currentStreak(days, dayIndex(now))
    }

    fun deleteBook(bookId: Int) {
        books.removeAll { it.id == bookId }
        sessions.removeAll { it.bookId == bookId }
        persist { deleteBookById(bookId) }
    }

    private fun sessionDays(): Set<Long> = sessions.map { dayIndex(it.loggedAtMillis) }.toSet()

    private fun loadFrozenDays(): Set<Long> =
        prefs.getStringSet(KEY_FROZEN_DAYS, emptySet())
            .orEmpty()
            .mapNotNull { it.toLongOrNull() }
            .toSet()

    fun missedStreak(now: Long = System.currentTimeMillis()): Int {
        val today = dayIndex(now)
        if (freshStartDay == today) return 0
        return missedYesterdayStreak(sessionDays(), frozenDays, today)
    }

    fun freezesLeftThisMonth(now: Long = System.currentTimeMillis()): Int =
        freezesLeft(frozenDays, dayIndex(now))

    fun useStreakFreeze(now: Long = System.currentTimeMillis()): Boolean {
        if (missedStreak(now) == 0 || freezesLeftThisMonth(now) == 0) return false
        frozenDays = frozenDays + (dayIndex(now) - 1)
        prefs.edit()
            .putStringSet(KEY_FROZEN_DAYS, frozenDays.map { it.toString() }.toSet())
            .apply()
        return true
    }

    fun startFreshToday(now: Long = System.currentTimeMillis()) {
        freshStartDay = dayIndex(now)
        prefs.edit().putLong(KEY_FRESH_START_DAY, freshStartDay).apply()
    }

    private companion object {
        const val KEY_ONBOARDED = "onboarded"
        const val KEY_FROZEN_DAYS = "frozen_days"
        const val KEY_FRESH_START_DAY = "fresh_start_day"
        const val KEY_SEEDED = "reminders_seeded"
    }
}