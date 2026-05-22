package com.example.parcial_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.parcial_2.ui.navigation.AppNavigation
import com.example.parcial_2.ui.theme.Parcial_2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Parcial_2Theme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}