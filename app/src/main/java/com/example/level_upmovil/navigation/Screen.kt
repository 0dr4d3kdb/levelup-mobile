package com.example.level_upmovil.navigation

sealed class Screen(val route: String) {

    data object Home : Screen("home_page")

    data object Profile : Screen("profile_page")

    data object Settings : Screen("setting_page")

    data object Catalogo : Screen("catalogo_page")

    data class Details(val itemId: String) : Screen("detail_page/{itemId}") {

        fun buildRoute(): String {
            return route.replace("{itemId}", itemId)
        }
    }
}