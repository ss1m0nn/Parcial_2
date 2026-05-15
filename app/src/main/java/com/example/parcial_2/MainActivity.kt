package com.example.parcial_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.parcial_2.model.remote.RetrofitClient
import com.example.parcial_2.model.repository.RecipeRepository
import com.example.parcial_2.ui.navigation.AppNavigation
import com.example.parcial_2.ui.theme.Parcial_2Theme
import com.example.parcial_2.viewmodel.RecipeViewModel
import com.example.parcial_2.viewmodel.RecipeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // In a real app, you might use DI like Hilt
        val repository = RecipeRepository(RetrofitClient.recipeApi)
        val viewModelFactory = RecipeViewModelFactory(repository)

        setContent {
            Parcial_2Theme {
                val navController = rememberNavController()
                val viewModel: RecipeViewModel = viewModel(factory = viewModelFactory)
                
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // AppNavigation handles the screens
                    AppNavigation(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
