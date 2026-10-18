package com.example.fivepages.navigation

object Routes {
    const val LIBRARY = "library"
    const val ADD_BOOK = "add_book"

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

    }