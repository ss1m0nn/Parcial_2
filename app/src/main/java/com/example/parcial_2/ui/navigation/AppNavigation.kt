package com.example.parcial_2.ui.navigation

import android.R.attr.type
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.parcial_2.ui.screens.AgregarRecetaScreen
import com.example.parcial_2.ui.screens.DetalleRecetaScreen
import com.example.parcial_2.ui.screens.HomeScreen
import com.example.parcial_2.ui.screens.OpinionScreen
import com.example.parcial_2.viewmodel.EstadisticasViewModel
import com.example.parcial_2.viewmodel.HomeViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AgregarReceta : Screen("agregar_receta")
    object EditarReceta : Screen("editar_receta")
    object DetalleReceta : Screen("detalle_receta")
    object Opinion : Screen("opinion")

}

@Composable
fun AppNavigation(navController: NavHostController) {

    val homeViewModel: HomeViewModel = viewModel()

    val estadisticasViewModel: EstadisticasViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                onAgregarClick = {
                    navController.navigate(Screen.AgregarReceta.route)
                },
                onEditarClick = { receta ->
                    homeViewModel.seleccionarReceta(receta)
                    navController.navigate(Screen.EditarReceta.route)
                },
                onRecetaClick = { receta ->
                    homeViewModel.seleccionarReceta(receta)
                    navController.navigate(Screen.DetalleReceta.route)
                }
            )
        }
        composable(Screen.AgregarReceta.route) {
            AgregarRecetaScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.EditarReceta.route) {
            val receta = homeViewModel.recetaSeleccionada
            AgregarRecetaScreen(
                onBack = { navController.popBackStack() },
                recetaExistente = receta
            )
        }
        composable(Screen.DetalleReceta.route) {
            val receta = homeViewModel.recetaSeleccionada
            if (receta != null) {
                DetalleRecetaScreen(
                    receta = receta,
                    viewModel = estadisticasViewModel,
                    onBack = { navController.popBackStack() },
                    onOpinionClick = { navController.navigate(Screen.Opinion.route)},
                )
            }
        }
        composable(Screen.Opinion.route) {
            val receta = homeViewModel.recetaSeleccionada
            if (receta != null) {
                OpinionScreen(
                    receta = receta,
                    onBack = { navController.popBackStack() },
                    onSubmit = { porciones, puntuacion, nota, veces ->
                        homeViewModel.guardarOpinion(
                            porciones = porciones,
                            puntuacion = puntuacion,
                            nota = nota,
                            veces = veces,
                            onSuccess = {
                                navController.popBackStack(Screen.Home.route, false)
                            }
                        )
                    }
                )
            }
        }

        }
    }

