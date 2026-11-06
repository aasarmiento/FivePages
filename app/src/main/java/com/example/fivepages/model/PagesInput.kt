package com.example.fivepages.model

// Turns what the user typed in Log Pages into a page count: digits only, never more than the pages left
fun pagesToLog(typed: String, pagesLeft: Int): Int {
    val number = typed.filter(Char::isDigit).take(5).toIntOrNull() ?: return 0
    return number.coerceIn(0, pagesLeft.coerceAtLeast(0))
}
