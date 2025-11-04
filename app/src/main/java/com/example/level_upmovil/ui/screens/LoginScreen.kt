

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
import com.example.level_upmovil.ui.theme.OrbitronFamily

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoginEnabled = email.isNotEmpty() && password.isNotEmpty()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Login") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                    if (email == "arturo@gmail.com" && password == "123456") {
                        onLoginSuccess()
                    } else if (isLoginEnabled) {
                        println("Credenciales incorrectas")
                    }
                },
                enabled = isLoginEnabled,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("ENTRAR")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}