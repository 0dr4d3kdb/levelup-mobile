package com.example.level_upmovil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.level_upmovil.ui.screens.HomeScreen
<<<<<<< Updated upstream
=======
import com.example.level_upmovil.ui.screens.LoginScreen
import com.example.level_upmovil.ui.screens.NosotrosScreen
import com.example.level_upmovil.ui.screens.RegistroScreen
import com.example.level_upmovil.ui.screens.ProfileScreen
>>>>>>> Stashed changes
import com.example.level_upmovil.ui.theme.LevelupMovilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            LevelupMovilTheme {
                Surface(
                    color = MaterialTheme.colorScheme.surface
                ) {
<<<<<<< Updated upstream
                    HomeScreen()
=======
                    composable(route = Screen.Home.route) {
                        HomeScreen(navController = navController, viewModel = viewModel)
                    }
                    composable (route = Screen.Login.route){
                        LoginScreen(navController = navController, viewModel = viewModel)
                    }
                    composable (route = Screen.Registro.route){
                        RegistroScreen(navController = navController, viewModel = viewModel)
                    }
                    composable(route = Screen.Profile.route) {
                        ProfileScreen(navController = navController, viewModel = viewModel)
                    }
                    composable (route = Screen.Catalogo.route){
                        CatalogoScreen(navController = navController, viewModel = viewModel)
                    }
                    composable (route = Screen.DetalleProducto.route,
                        arguments = listOf(
                            navArgument("productoId"){
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ){backStackEntry ->
                        val productoId = backStackEntry.arguments?.getInt("productoId") ?: -1

                        val onAddAction: (Producto) -> Unit = {producto ->
                            println("Acción de agregar al carro pendiente")
                        }

                        DetalleProductoScreen(
                            productoId = productoId,
                            navController = navController,
                            viewModel = viewModel
                            //onAddToCartClick =onAddAction
                        )
                    }

                    composable(route = Screen.Carrito.route) {
                        CarritoScreen(navController = navController, viewModel = viewModel)
                    }

                    composable(route = Screen.Nosotros.route) {
                        NosotrosScreen(navController = navController, viewModel = viewModel)
                    }
>>>>>>> Stashed changes
                }
            }
        }
    }
}


/*@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LevelupMovilTheme {
        Greeting("Android")
    }
}*/