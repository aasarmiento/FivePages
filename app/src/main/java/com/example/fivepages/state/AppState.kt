package com.example.fivepages.state

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.fivepages.model.Book
import com.example.fivepages.model.ReminderWindow

class AppState : ViewModel() {
    val books = mutableStateListOf<Book>()

    //Reminder Windows (starts with the two examples from Figma)
    val reminderWindows = mutableStateListOf(
        ReminderWindow(1, "Morning coffee", 8, 0),
        ReminderWindow(2, "Post-lunch", 13, 0),
    )

    fun setReminderEnabled(id: Int, enabled: Boolean) {
        val index = reminderWindows.indexOfFirst { it.id == id }
        if (index >= 0) reminderWindows[index] = reminderWindows[index].copy(enabled = enabled)
    }

    //Save Reminder (adds a new one if the id is not in the list)
    fun saveReminder(id: Int, label: String, hour: Int, minute: Int, enabled: Boolean) {
        val index = reminderWindows.indexOfFirst { it.id == id }
        if (index >= 0) {
            reminderWindows[index] = reminderWindows[index].copy(
                label = label, hour = hour, minute = minute, enabled = enabled
            )
        } else {
            val newId = (reminderWindows.maxOfOrNull { it.id } ?: 0) + 1
            reminderWindows.add(ReminderWindow(newId, label, hour, minute, enabled))
        }
    }

    //Delete Reminder
    fun deleteReminder(id: Int) {
        reminderWindows.removeAll { it.id == id }
    }

    fun addBook(title: String, totalPages: Int, targetDateMillis: Long): Int {
        val id = (books.maxOfOrNull { it.id } ?: 0) + 1
        books.add(Book(id, title, totalPages, targetDateMillis))
        return id
    }

    fun logPages(bookId: Int, pages: Int) {
        val i = books.indexOfFirst { it.id == bookId }
        if (i < 0 || pages <= 0) return
        val b = books[i]
        books[i] = b.copy(pagesRead = (b.pagesRead + pages).coerceAtMost(b.totalPages))
    }

    fun titleExists(title: String, exceptId: Int? = null): Boolean {
        val wanted = normalizeTitle(title)
        return books.any { it.id != exceptId && normalizeTitle(it.title) == wanted }
    }

    private fun normalizeTitle(title: String): String =
        title.trim().replace(Regex("\\s+"), " ").lowercase()

    fun renameBook(bookId: Int, newTitle: String) {
        val i = books.indexOfFirst { it.id == bookId }
        if (i < 0 || newTitle.isBlank() || titleExists(newTitle, bookId)) return
        books[i] = books[i].copy(title = newTitle.trim())
    }

    fun deleteBook(bookId: Int) {
        books.removeAll { it.id == bookId }
    }
}