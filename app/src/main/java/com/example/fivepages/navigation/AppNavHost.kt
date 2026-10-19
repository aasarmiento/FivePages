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
        //LIBRARY ROUTES
        composable(Routes.LIBRARY) {
            LibraryEmptyScreen(
                books = appState.books,
                onAddBook = {
                    navController.navigate(Routes.ADD_BOOK) { launchSingleTop = true }
                },
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
                    navController.goBack()
                }
            )
        }

        composable(
            Routes.BOOK_DETAIL,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { entry ->
            val bookId = entry.arguments?.getInt("bookId") ?: return@composable
            val book = appState.books.firstOrNull { it.id == bookId } ?: return@composable
            BookDetailsScreen(
                book = book,
                onBack = { navController.goBack() },
                onLogPages = {
                    navController.navigate(Routes.logPages(bookId)) { launchSingleTop = true }
                }
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
                onSave = { pages: Int ->
                    appState.logPages(bookId, pages)
                    navController.goBack()
                }
            )
        }
    }
}


