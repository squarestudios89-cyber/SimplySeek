package com.squarestudios.simplyseek.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.squarestudios.simplyseek.ui.screens.CategorySelectScreen
import com.squarestudios.simplyseek.ui.screens.GameBoardScreen
import com.squarestudios.simplyseek.ui.screens.HomeScreen
import com.squarestudios.simplyseek.ui.screens.StatsScreen

/**
 * Top-level Jetpack Compose NavHost managing app routes and screen transitions.
 */
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        // 1. Home Screen
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToCategorySelect = {
                    navController.navigate(Screen.CategorySelect.route)
                },
                onNavigateToStats = {
                    navController.navigate(Screen.Stats.route)
                },
            )
        }

        // 2. Category Select Screen
        composable(route = Screen.CategorySelect.route) {
            CategorySelectScreen(
                onStartGame = { category, difficulty ->
                    val route = Screen.GameBoard.createRoute(category, difficulty)
                    navController.navigate(route)
                },
            )
        }

        // 3. Game Board Screen with arguments
        composable(
            route = Screen.GameBoard.route,
            arguments = listOf(
                navArgument(Screen.GameBoard.ARG_CATEGORY) { type = NavType.StringType },
                navArgument(Screen.GameBoard.ARG_DIFFICULTY) { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString(Screen.GameBoard.ARG_CATEGORY) ?: "Technology"
            val difficulty = backStackEntry.arguments?.getString(Screen.GameBoard.ARG_DIFFICULTY) ?: "Medium"

            GameBoardScreen(
                category = category,
                difficulty = difficulty,
            )
        }

        // 4. Stats Screen
        composable(route = Screen.Stats.route) {
            StatsScreen()
        }
    }
}
