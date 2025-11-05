package com.example.level_upmovil.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.level_upmovil.navigation.Screen
import com.example.level_upmovil.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navController: NavController,
    viewModel: MainViewModel,
    title: String,
    content: @Composable (PaddingValues) -> Unit
){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect (key1 = Unit) {
        viewModel.uiEvents.collectLatest { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short // Mostrarlo por un corto tiempo
            )
        }
    }

    ModalNavigationDrawer(
        drawerState =drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet {
                Text(text = "Menú", modifier = Modifier.padding(16.dp))

                fun navigateAndClose(screen: Screen){
                    scope.launch { drawerState.close() }
                    viewModel.navigateTo(screen)
                }

                NavigationDrawerItem(
                    label = {Text("Inicio")},
                    selected = title == "Inicio",
                    onClick = {navigateAndClose(Screen.Home)}
                )
                NavigationDrawerItem(
                    label = {Text("Catálogo")},
                    selected = title == "Catálogo",
                    onClick = {navigateAndClose(Screen.Catalogo)}
                )
                NavigationDrawerItem(
                    label = {Text("Perfil")},
                    selected = title == "Perfil",
                    onClick = {navigateAndClose(Screen.Profile)}
                )
            }
        }
    ) {
        Scaffold (
            topBar = {
                TopAppBar(
                    title = {Text(title)},
                    navigationIcon = {
                        IconButton(onClick = {scope.launch { drawerState.open() }}) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            viewModel.navigateTo(Screen.Carrito)
                        }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito de compras" )
                        }
                    }

                )

            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ){ innerPadding ->
            content(innerPadding)
        }
    }
}