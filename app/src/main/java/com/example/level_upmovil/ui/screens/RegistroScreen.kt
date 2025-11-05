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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_upmovil.navigation.Screen
import com.example.level_upmovil.ui.components.AppScaffold
import com.example.level_upmovil.ui.theme.OrbitronFamily
import com.example.level_upmovil.viewmodel.MainViewModel


private val emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RegistroScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Estados de error
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    fun validateInputs(): Boolean {
        nameError = name.length < 3

        emailError = !email.matches(emailRegex)

        passwordError = password.length < 6

        return !nameError && !emailError && !passwordError
    }

    AppScaffold(
        navController = navController,
        viewModel = viewModel,
        title = "Registro"
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "CREAR CUENTA",
                style = MaterialTheme.typography.headlineMedium.copy(fontFamily = OrbitronFamily),
                modifier = Modifier.padding(bottom = 48.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it; nameError = false },
                label = { Text("Nombre") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Nombre") },
                isError = nameError,
                supportingText = if (nameError) { { Text("Mínimo 3 letras.") } } else null,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; emailError = false },
                label = { Text("Correo Electrónico") },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Correo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailError,
                supportingText = if (emailError) { { Text("Formato de correo inválido.") } } else null,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it; passwordError = false },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = passwordError,
                supportingText = if (passwordError) { { Text("Mínimo 6 caracteres.") } } else null,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (validateInputs()) {
                        println("Usuario Registrado: $name, $email")
                        viewModel.navigateTo(
                            screen = Screen.Login
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("REGISTRARSE")
            }

            Spacer(modifier = Modifier.height(16.dp))


            TextButton(onClick = {viewModel.navigateTo(Screen.Login)}) {
                Text("¿Ya tienes cuenta? Inicia Sesión")
            }
        }
    }
}