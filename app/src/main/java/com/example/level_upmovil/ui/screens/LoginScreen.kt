package com.example.level_upmovil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_upmovil.navigation.NavigationEvent // <-- NUEVO: Para manejar eventos de navegación
import com.example.level_upmovil.navigation.Screen
import com.example.level_upmovil.ui.components.AppScaffold
import com.example.level_upmovil.ui.theme.OrbitronFamily
import com.example.level_upmovil.viewmodel.LoginStatus // <-- NUEVO: Para observar el estado de Login
import com.example.level_upmovil.viewmodel.MainViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    // Estados locales para los campos de entrada
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Observar estados del ViewModel
    val loginStatus by viewModel.loginStatus.collectAsState()
    val isApiLoading = loginStatus == LoginStatus.Loading
    val snackbarHostState = remember { SnackbarHostState() }

    // El botón se habilita si hay texto y no estamos cargando
    val isLoginEnabled = email.isNotEmpty() && password.isNotEmpty() && !isApiLoading

    // 💥 1. LANCHED EFFECT PARA MANEJAR EVENTOS DE NAVEGACIÓN (SharedFlow)
    // Escucha eventos de navegación emitidos desde el ViewModel (ej: navegación a Home tras éxito)
    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is NavigationEvent.NavigateTo -> {
                    navController.navigate(event.route.route) {
                        event.popUpRoute?.let { popUpTo(it.route) { inclusive = event.inclusive } }
                        launchSingleTop = event.singleTop
                    }
                }
                is NavigationEvent.PopBackStack -> navController.popBackStack()
                is NavigationEvent.NavigateUp -> navController.navigateUp()
            }
        }
    }

    // 💥 2. LANCHED EFFECT PARA MANEJAR EL ESTADO DE LOGIN (StateFlow)
    // Escucha el resultado de la llamada a la API
    LaunchedEffect(loginStatus) {
        when (loginStatus) {
            is LoginStatus.Success -> {
                // La navegación a Screen.Home ya es manejada por el VM. Aquí solo mostramos el mensaje.
                snackbarHostState.showSnackbar(
                    "✅ Inicio de sesión exitoso.",
                    duration = SnackbarDuration.Short
                )
                viewModel.resetLoginStatus()
            }
            is LoginStatus.Error -> {
                val errorMessage = (loginStatus as LoginStatus.Error).message
                snackbarHostState.showSnackbar(errorMessage, duration = SnackbarDuration.Long)
                viewModel.resetLoginStatus() // Resetear para que el usuario pueda reintentar
            }
            else -> Unit
        }
    }

    AppScaffold(
        navController = navController,
        viewModel = viewModel,
        title = "Login"
    ) { paddingValues ->
        // Usamos Box para superponer el SnackbarHost sobre el contenido de la columna
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "INICIAR SESIÓN",
                    style = MaterialTheme.typography.headlineMedium.copy(fontFamily = OrbitronFamily),
                    modifier = Modifier.padding(bottom = 48.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Correo") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        // 💥 LLAMADA A LA LÓGICA DEL VIEWMODEL EN LUGAR DE CÓDIGO CABLEADO
                        viewModel.loginUser(email, password)
                    },
                    enabled = isLoginEnabled,
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    if (isApiLoading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                    } else {
                        Text("ENTRAR")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick ={viewModel.navigateTo(Screen.Registro)}) {
                    Text("¿No tienes cuenta? Regístrate")
                }
            }

            // Snackbar Host para mostrar mensajes de error/éxito
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
            )
        }
    }
}