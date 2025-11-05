package com.example.level_upmovil.navigation

sealed class Screen(val route: String) {

    data object Home : Screen("home_page")

    data object Profile : Screen("profile_page")

    data object Settings : Screen("setting_page")

    data object Catalogo : Screen("catalogo_page")
    data object Login : Screen("login_page")
    data object Registro : Screen("registro_page")

    data object DetalleProducto : Screen("detail_page/{productoId}"){
        fun createRoute(productoId: Int): String{
            return "detail_page/$productoId"
        }
    }

    data object Carrito : Screen("carrito_page")
}