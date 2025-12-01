// com.example.level_upmovil.ui.screens/RegisterScreen.kt

package com.example.level_upmovil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_upmovil.navigation.Screen
import com.example.level_upmovil.ui.components.AppScaffold
import com.example.level_upmovil.ui.theme.OrbitronFamily
import com.example.level_upmovil.viewmodel.MainViewModel
import com.example.level_upmovil.viewmodel.RegistrationStatus
import androidx.compose.foundation.clickable
import android.util.Log // 💥 Necesario para el Log temporal
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

private val emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RegistroScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    // Estados locales para los campos de entrada
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Estados de error
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    // Observar el estado de la operación POST desde el ViewModel
    val registrationStatus by viewModel.registrationStatus.collectAsState()
    val isApiLoading = registrationStatus == RegistrationStatus.Loading

    // Snackbar para mostrar mensajes de error/éxito
    val snackbarHostState = remember { SnackbarHostState() }

    // Manejo de la navegación y errores de la API

    val scrollState = rememberScrollState()

    LaunchedEffect(registrationStatus) {
        when (registrationStatus) {
            is RegistrationStatus.Success -> {
                snackbarHostState.showSnackbar(
                    "✅ Registro exitoso. Ahora puedes iniciar sesión.",
                    duration = SnackbarDuration.Long
                )
                viewModel.resetRegistrationStatus()
                navController.navigate(Screen.Login.route) {
                    // Limpiar la pila de registro para que no pueda volver atrás
                    popUpTo(Screen.Registro.route) { inclusive = true }
                }
            }
            is RegistrationStatus.Error -> {
                val errorMessage = (registrationStatus as RegistrationStatus.Error).message
                snackbarHostState.showSnackbar(errorMessage, duration = SnackbarDuration.Long)
                viewModel.resetRegistrationStatus() // Resetear para que el usuario pueda reintentar
                Log.e("REGISTRO_UI", "Error de API recibido y mostrado: $errorMessage") // Log de error recibido
            }
            else -> Unit
        }
    }

    val onRegisterClicked: () -> Unit = {
        // 1. VALIDACIÓN LOCAL
        nameError = name.isBlank()
        emailError = email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        passwordError = password.isBlank() || password.length < 6

        // 2. LLAMADA AL VIEWMODEL SOLO SI NO HAY ERRORES
        if (!nameError && !emailError && !passwordError) {
            Log.d("REGISTRO_UI", "Validación local exitosa. Llamando a registerNewUser.") // 💥 LOG CRÍTICO
            viewModel.registerNewUser(name, email, password)
        } else {
            Log.w("REGISTRO_UI", "Validación local fallida. No se llama al ViewModel.")
        }
    }

    // --- ESTRUCTURA DE LA PANTALLA ---
    AppScaffold(
        navController = navController,
        viewModel = viewModel,
        title = "Registro de Usuario",
        snackbarHostState = snackbarHostState
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(32.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "¡Únete a Level Up!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // 1. Campo Nombre
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = false // Limpiar error al escribir
                },
                label = { Text("Nombre Completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Nombre") },
                isError = nameError,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            if (nameError) {
                Text("El nombre no puede estar vacío.", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            // 2. Campo Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = false
                },
                label = { Text("Correo Electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Correo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailError,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            if (emailError) {
                Text("Ingresa un correo válido.", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            // 3. Campo Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = false
                },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = passwordError,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )
            if (passwordError) {
                Text("La contraseña es requerida y debe tener al menos 6 caracteres.", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            // 4. Botón de Registro
            Button(
                onClick = onRegisterClicked,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                // Deshabilitar si hay un error local o si la API está cargando
                enabled = !nameError && !emailError && !passwordError && !isApiLoading
            ) {
                if (isApiLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Registrarse")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 5. Enlace a Login
            Row {
                Text("¿Ya tienes una cuenta? ", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Iniciar Sesión",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Login.route)
                    }
                )
            }
        }
    }
}