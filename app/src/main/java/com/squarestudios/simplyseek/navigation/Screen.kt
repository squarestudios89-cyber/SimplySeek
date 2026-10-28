package com.squarestudios.simplyseek.navigation

/**
 * Sealed class defining navigation routes and argument helpers.
 */
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object CategorySelect : Screen("category_select")
    data object GameBoard : Screen("game_board/{category}/{difficulty}") {
        const val ARG_CATEGORY = "category"
        const val ARG_DIFFICULTY = "difficulty"

        fun createRoute(category: String, difficulty: String): String {
            return "game_board/$category/$difficulty"
        }
    }
    data object Stats : Screen("stats")
}
