package com.example.level_upmovil.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen() {
    Surface (color = MaterialTheme.colorScheme.background) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "PERFIL DE USUARIO",
                // Tipografía Orbitron
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 32.dp)
            )

        }
    }
}
@Composable
fun ProfileInfoItem(label: String, value: String) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Usa bodyMedium o bodyLarge para el texto, asegurando Roboto
        Text(text = label, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}