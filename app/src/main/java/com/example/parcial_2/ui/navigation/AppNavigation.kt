package com.example.parcial_2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.parcial_2.ui.screens.HomeScreen
import com.example.parcial_2.ui.screens.RecipeDetailScreen
import com.example.parcial_2.viewmodel.RecipeViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{recipeId}") {
        fun createRoute(recipeId: String) = "detail/$recipeId"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: RecipeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.Detail.createRoute(recipeId))
                }
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
            RecipeDetailScreen(
                recipeId = recipeId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
