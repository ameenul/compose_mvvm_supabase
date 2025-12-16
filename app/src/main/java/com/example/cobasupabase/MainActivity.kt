package com.example.cobasupabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.cobasupabase.ui.nav.AppNavigation // Corrected import
import androidx.navigation.compose.rememberNavController // Import rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController() // Create a NavController
            AppNavigation(navController = navController) // Call AppNavigation with navController
        }
    }
}






