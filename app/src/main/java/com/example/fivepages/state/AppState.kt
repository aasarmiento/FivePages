package com.example.fivepages.state
import androidx.compose.runtime.mutableStateListOf
import com.example.fivepages.model.Book

class AppState {
    val books = mutableStateListOf<Book>()

    fun addBook(title: String, totalPages: Int, targetDateMillis: Long): Int {
        val id = (books.maxOfOrNull { it.id } ?: 0) + 1
        books.add(Book(id, title, totalPages, targetDateMillis))
        return id
    }
}