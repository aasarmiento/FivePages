package com.example.fivepages.navigation

object Routes {
    const val LIBRARY = "library"
    const val ADD_BOOK = "add_book"

    // Book Progress Routes
    const val BOOK_DETAIL = "book_detail/{bookId}"
    const val LOG_PAGES = "log_pages/{bookId}"
    const val READING_SESSION = "reading_session/{bookId}"
    fun bookDetail(bookId: Int) = "book_detail/$bookId"
    fun logPages(bookId: Int) = "log_pages/$bookId"
    fun readingSession(bookId: Int) = "reading_session/$bookId"
    const val BOOK_OPTIONS = "book_options/{bookId}"
    const val EDIT_TITLE = "edit_title/{bookId}"
    fun bookOptions(bookId: Int) = "book_options/$bookId"
    fun editTitle(bookId: Int) = "edit_title/$bookId"

    //Onboarding Screens
    const val ONBOARDING_WELCOME = "onboarding_welcome"
    const val ONBOARDING_HOW_IT_WORKS = "onboarding_how_it_works"
    const val ONBOARDING_NOTIFICATIONS = "onboarding_notifications"

    //Settings Screen
    const val SETTINGS = "settings"

    //Reminder Window Screen
    const val NEW_REMINDER_ID = -1
    const val REMINDER_ID_ARG = "id"
    const val REMINDER_WINDOW = "reminder_window/{$REMINDER_ID_ARG}"
    fun reminderWindow(id: Int) = "reminder_window/$id"

    const val PAGES_ARG = "pages"
    const val SESSION_LOGGED = "session_logged/{$PAGES_ARG}"
    fun sessionLogged(pages: Int) = "session_logged/$pages"

}