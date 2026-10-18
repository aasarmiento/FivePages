package com.example.fivepages.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fivepages.LibraryEmptyScreen
import com.example.fivepages.state.AppState
import com.example.fivepages.ui.screens.addbook.AddBookScreen
import com.example.fivepages.ui.screens.onboarding.HowItWorksScreen
import com.example.fivepages.ui.screens.onboarding.NotificationsScreen
import com.example.fivepages.ui.screens.onboarding.RemindersOnDialog
import com.example.fivepages.ui.screens.onboarding.WelcomeScreen
import com.example.fivepages.ui.screens.onboarding.rememberNotificationPermission
import com.example.fivepages.ui.screens.settings.SettingsScreen
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.fivepages.ui.screens.settings.ReminderWindowScreen

// Safe na Back: pop lang kung may previous screen, para hindi ma-pop ang start screen

private fun NavController.goBack() {
    if (previousBackStackEntry != null) popBackStack()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(appState: AppState) {
    val navController = rememberNavController()

    // Onboarding done: go to Library and remove onboarding from the back stack
    val finishOnboarding: () -> Unit = {
        navController.navigate(Routes.LIBRARY) {
            popUpTo(Routes.ONBOARDING_WELCOME) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = Routes.ONBOARDING_WELCOME) {

        // Onboarding
        composable(Routes.ONBOARDING_WELCOME) {
            WelcomeScreen(
                onGetStarted = {
                    navController.navigate(Routes.ONBOARDING_HOW_IT_WORKS) {
                        launchSingleTop = true
                    }
                },
                onSkip = finishOnboarding
            )
        }
        composable(Routes.ONBOARDING_HOW_IT_WORKS) {
            HowItWorksScreen(
                onNext = {
                    navController.navigate(Routes.ONBOARDING_NOTIFICATIONS) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.ONBOARDING_NOTIFICATIONS) {
            var showDialog by rememberSaveable { mutableStateOf(false) }
            val requestPermission = rememberNotificationPermission { granted ->
                if (granted) showDialog = true else finishOnboarding()
            }
            NotificationsScreen(
                onEnableReminders = requestPermission,
                onNotNow = finishOnboarding
            )
            if (showDialog) {
                RemindersOnDialog(onDismiss = {
                    showDialog = false
                    finishOnboarding()
                })
            }
        }

        composable(Routes.LIBRARY) {
            LibraryEmptyScreen(
                books = appState.books,
                onAddBook = {
                    navController.navigate(Routes.ADD_BOOK) { launchSingleTop = true }
                },
                onTabSelected = { tab ->
                    if (tab == "Settings") {
                        navController.navigate(Routes.SETTINGS) { launchSingleTop = true }
                    }
                }
            )
        }

        composable(Routes.ADD_BOOK) {
            AddBookScreen(
                onBack = { navController.goBack() },
                onSave = { title, totalPages, targetDateMillis ->
                    appState.addBook(title, totalPages, targetDateMillis)
                    navController.goBack()
                }
            )
        }

        //Settings
        composable(Routes.SETTINGS) {
            SettingsScreen(
                reminderWindows = appState.reminderWindows,
                onToggleReminder = { id, enabled -> appState.setReminderEnabled(id, enabled) },
                onAddReminderWindow = {
                    navController.navigate(Routes.reminderWindow(Routes.NEW_REMINDER_ID)) { launchSingleTop = true }
                },
                onEditReminder = { id ->
                    navController.navigate(Routes.reminderWindow(id)) { launchSingleTop = true }
                },
                onTabSelected = { tab ->
                    if (tab == "Library") navController.goBack()
                }
            )
        }

        //Reminder Window (add or edit)
        composable(
            route = Routes.REMINDER_WINDOW,
            arguments = listOf(navArgument(Routes.REMINDER_ID_ARG) { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt(Routes.REMINDER_ID_ARG) ?: Routes.NEW_REMINDER_ID
            //remember keeps the original reminder even after it gets deleted
            val reminder = remember { appState.reminderWindows.firstOrNull { it.id == id } }
            ReminderWindowScreen(
                reminder = reminder,
                onBack = { navController.goBack() },
                onSave = { label, hour, minute, enabled -> appState.saveReminder(id, label, hour, minute, enabled) },
                onDelete = { appState.deleteReminder(id) }
            )
        }
    }
}

