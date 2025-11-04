package com.example.level_upmovil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.level_upmovil.ui.screens.HomeScreen
import com.example.level_upmovil.ui.screens.LoginScreen
import com.example.level_upmovil.ui.screens.ProfileScreen
import com.example.level_upmovil.ui.screens.RegisterScreen
import com.example.level_upmovil.ui.theme.LevelupMovilTheme
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Registro: Screen("registro")
    object Login: Screen("login")
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LevelupMovilTheme (
                darkTheme = true , dynamicColor = false
            ){AppNavigation(startDestination = Screen.Login.route)
            }
        }
    }
}

@Composable
fun AppNavigation(startDestination: String) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {


        composable(Screen.Login.route) {
            LoginScreen (
                onNavigateToRegister = { navController.navigate(Screen.Registro.route) },
                onLoginSuccess = {

                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }


        composable(Screen.Registro.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onRegistrationSuccess = {
                    navController.popBackStack()
                }
            )
        }


        composable(Screen.Home.route) {
            HomeScreen (
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
            )
        }


        composable(Screen.Profile.route) {
            ProfileScreen(onNavigateToHome = { navController.navigate(Screen.Home.route) }
            )
        }
    }
}