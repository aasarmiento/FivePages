package com.example.fivepages.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fivepages.LibraryEmptyScreen
import com.example.fivepages.state.AppState
import com.example.fivepages.ui.screens.addbook.AddBookScreen
import com.example.fivepages.ui.screens.addbook.BookDetailsScreen
import com.example.fivepages.ui.screens.addbook.LogPagesScreen
import com.example.fivepages.ui.screens.onboarding.HowItWorksScreen
import com.example.fivepages.ui.screens.onboarding.NotificationsScreen
import com.example.fivepages.ui.screens.onboarding.RemindersOnDialog
import com.example.fivepages.ui.screens.onboarding.WelcomeScreen
import com.example.fivepages.ui.screens.onboarding.rememberNotificationPermission
import com.example.fivepages.ui.screens.settings.SettingsScreen
import androidx.compose.runtime.remember
import com.example.fivepages.ui.screens.addbook.ReadingSessionScreen
import com.example.fivepages.ui.screens.addbook.SessionLoggedScreen
import com.example.fivepages.ui.screens.bookoptions.BookOptionsScreen
import com.example.fivepages.ui.screens.bookoptions.EditGoalScreen
import com.example.fivepages.ui.screens.bookoptions.EditTitleScreen
import com.example.fivepages.ui.screens.progress.ProgressScreen
import com.example.fivepages.ui.screens.settings.ReminderWindowScreen

// Safe na Back: pop lang kung may previous screen, para hindi ma-pop ang start screen

private fun NavController.goBack() {
    if (previousBackStackEntry != null) popBackStack()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(appState: AppState) {
    val navController = rememberNavController()
    val startDestination = remember {
        if (appState.onboarded) Routes.LIBRARY else Routes.ONBOARDING_WELCOME
    }

    // Onboarding done: go to Library and remove onboarding from the back stack
    val finishOnboarding: () -> Unit = {
        appState.markOnboarded()
        navController.navigate(Routes.LIBRARY) {
            popUpTo(Routes.ONBOARDING_WELCOME) { inclusive = true }
        }
    }

    val goToTab: (String) -> Unit = { tab ->
        if (tab == "Library") {
            navController.popBackStack(Routes.LIBRARY, inclusive = false)
        } else {
            val route = if (tab == "Progress") Routes.PROGRESS else Routes.SETTINGS
            navController.navigate(route) {
                popUpTo(Routes.LIBRARY) { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {

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
        //LIBRARY ROUTES
        composable(Routes.LIBRARY) {
            LibraryEmptyScreen(
                books = appState.books,
                onAddBook = {
                    navController.navigate(Routes.ADD_BOOK) { launchSingleTop = true }
                },
                onTabSelected = goToTab,
                streakFor = { appState.streakFor(it) },
                onReadBook = { bookId: Int ->
                    navController.navigate(Routes.bookDetail(bookId)) { launchSingleTop = true }
                }
            )
        }

        composable(Routes.ADD_BOOK) {
            AddBookScreen(
                onBack = { navController.goBack() },
                onSave = { title, totalPages, targetDateMillis ->
                    appState.addBook(title, totalPages, targetDateMillis)
                },
                isTitleTaken = { appState.titleExists(it) }
            )
        }

        //Progress
        composable(Routes.PROGRESS) {
            ProgressScreen(
                summary = appState.progressSummary(),
                onTabSelected = goToTab
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
                onTabSelected = goToTab
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

        //BOOK DETAIL
        composable(
            Routes.BOOK_DETAIL,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { entry ->
            val bookId = entry.arguments?.getInt("bookId") ?: return@composable
            val book = appState.books.firstOrNull { it.id == bookId } ?: return@composable
            BookDetailsScreen(
                book = book,
                onBack = { navController.goBack() },

                onStartSession = {
                    navController.navigate(Routes.readingSession(bookId)) { launchSingleTop = true }
                },
                onLogPages = {
                    navController.navigate(Routes.logPages(bookId)) { launchSingleTop = true }
                },
                onOptions = {
                    navController.navigate(Routes.bookOptions(bookId)) { launchSingleTop = true }
                },
                dayStreak = appState.streakFor(bookId)
            )
        }

        //TIMER
        composable(
            Routes.READING_SESSION,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { entry ->
            val bookId = entry.arguments?.getInt("bookId") ?: return@composable

            ReadingSessionScreen(
                onComplete = {
                    navController.navigate(Routes.logPages(bookId)) {
                        popUpTo(Routes.READING_SESSION) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onDiscard = { navController.goBack() },
            )
        }

        //WORKFLOW
        composable(
            Routes.LOG_PAGES,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { entry ->
            val bookId = entry.arguments?.getInt("bookId") ?: return@composable
            val book = appState.books.firstOrNull { it.id == bookId } ?: return@composable
            LogPagesScreen(
                book = book,
                onBack = { navController.goBack() },
                onSave = { pages: Int ->
                    appState.logPages(bookId, pages)
                    navController.navigate(Routes.sessionLogged(pages)) {
                        popUpTo(Routes.LOG_PAGES) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            Routes.SESSION_LOGGED,
            arguments = listOf(navArgument(Routes.PAGES_ARG) { type = NavType.IntType })
        ) { entry ->
            val pages = entry.arguments?.getInt(Routes.PAGES_ARG) ?: 0
            var undone by rememberSaveable { mutableStateOf(false) }
            SessionLoggedScreen(
                pagesAdded = pages,
                onReturnToLibrary = { navController.popBackStack(Routes.LIBRARY, inclusive = false) },
                streakDays = appState.progressSummary().dayStreak.takeIf { it > 0 },
                onUndo = {
                    if (!undone) {
                        undone = true
                        appState.undoLastSession()?.let { bookId ->
                            navController.navigate(Routes.logPages(bookId)) {
                                popUpTo(Routes.SESSION_LOGGED) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                }
            )
        }

        composable(
            Routes.BOOK_OPTIONS,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { entry ->
            val bookId = entry.arguments?.getInt("bookId") ?: return@composable
            val firstSeen = remember { appState.books.firstOrNull { it.id == bookId } } ?: return@composable
            val title = appState.books.firstOrNull { it.id == bookId }?.title ?: firstSeen.title
            BookOptionsScreen(
                bookTitle = title,
                onBack = { navController.goBack() },
                onEditTitle = {
                    navController.navigate(Routes.editTitle(bookId)) { launchSingleTop = true }
                },
                onEditGoal = {
                    navController.navigate(Routes.editGoal(bookId)) { launchSingleTop = true }
                },
                onDeleteConfirmed = { appState.deleteBook(bookId) },
                onDeletedDone = { navController.popBackStack(Routes.LIBRARY, inclusive = false) }
            )
        }

        composable(
            Routes.EDIT_TITLE,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { entry ->
            val bookId = entry.arguments?.getInt("bookId") ?: return@composable
            val book = appState.books.firstOrNull { it.id == bookId } ?: return@composable
            EditTitleScreen(
                currentTitle = book.title,
                onBack = { navController.goBack() },
                onSave = { newTitle -> appState.renameBook(bookId, newTitle) },
                isTitleTaken = { appState.titleExists(it, exceptId = bookId) }
            )
        }

        composable(
            Routes.EDIT_GOAL,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { entry ->
            val bookId = entry.arguments?.getInt("bookId") ?: return@composable
            val book = appState.books.firstOrNull { it.id == bookId } ?: return@composable
            EditGoalScreen(
                totalPages = book.totalPages,
                pagesRead = book.pagesRead,
                targetDateMillis = book.targetDateMillis,
                onBack = { navController.goBack() },
                onSave = { newTotal, newDate -> appState.updateGoal(bookId, newTotal, newDate) }
            )
        }
    }
}