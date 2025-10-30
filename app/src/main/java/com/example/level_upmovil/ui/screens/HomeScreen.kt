package com.example.level_upmovil.ui.screens

import android.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onNavigateToProfile: () -> Unit ){
    Surface ( color = MaterialTheme.colorScheme.background ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Bienvenido a la App",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Esta es la pantalla de inicio.",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button (onClick = onNavigateToProfile) {
                // El texto del botón usa labelLarge por defecto, que es Roboto
                Text("Ver Perfil")
            }
        }
    }
}