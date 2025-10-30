package com.example.level_upmovil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.level_upmovil.ui.screens.HomeScreen
import com.example.level_upmovil.ui.screens.ProfileScreen
import com.example.level_upmovil.ui.theme.LevelupMovilTheme
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile")
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LevelupMovilTheme (
                darkTheme = true , dynamicColor = false
            ){AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            // Navegación a la pantalla Home
            HomeScreen (
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
            )
        }
        composable(Screen.Profile.route) {
            // Navegación a la pantalla Profile
            ProfileScreen()
        }
    }
}