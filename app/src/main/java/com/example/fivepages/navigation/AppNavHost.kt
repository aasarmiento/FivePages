package com.example.fivepages.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fivepages.LibraryEmptyScreen
import com.example.fivepages.state.AppState
import com.example.fivepages.ui.screens.addbook.AddBookScreen

// Safe na Back: pop lang kung may previous screen, para hindi ma-pop ang start screen

private fun NavController.goBack() {
    if (previousBackStackEntry != null) popBackStack()
}

@Composable
fun AppNavHost(appState: AppState) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LIBRARY) {
        composable(Routes.LIBRARY) {
            LibraryEmptyScreen(
                onAddBook = {
                    navController.navigate(Routes.ADD_BOOK) { launchSingleTop = true }
                }
            )
        }
        composable(Routes.ADD_BOOK) {
            AddBookScreen(onBack = { navController.goBack() })
        }
    }
}