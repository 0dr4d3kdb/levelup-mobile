package com.example.level_upmovil.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_upmovil.ui.components.AppScaffold
import com.example.level_upmovil.viewmodel.MainViewModel
// Importaciones del mapa:
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun NosotrosScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel ()
) {
    // 1. Definir la Ubicación
    val duocUC = LatLng(-33.6942529, -71.2159749)

    // 2. Definir el Estado Inicial de la Cámara
    val cameraPositionState = rememberCameraPositionState {
        // Inicializar la cámara sobre la ubicación con un zoom de 15f (cercano)
        position = CameraPosition.fromLatLngZoom(duocUC, 15f)
    }

    // Usando un contenedor (AppScaffold) para la pantalla completa
    AppScaffold(
        navController = navController,
        viewModel = viewModel,
        title = "Sobre nosotros"
    ) { innerPadding ->
        // Contenedor principal para el contenido (Texto y Mapa)
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Contenido de la sección "Sobre nosotros" (parte superior)
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "La mejor tienda de artículos gamer")
                Text(text = "Nuestra tienda física se encuentra en:")
                // Si quieres que esta sección sea larga y desplace el mapa hacia abajo,
                // añade más contenido o usa el modificador .verticalScroll como vimos antes.
            }

            // 3. El Composable del Mapa
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    // Usamos una altura fija (ej. 300dp) para que ocupe un "recuadro"
                    .height(300.dp),
                cameraPositionState = cameraPositionState
            ) {
                // 4. Añadir un Marcador
                Marker(
                    state = MarkerState(position = duocUC),
                    title = "Level-Up Gamer",
                    snippet = "Tienda física Level-Up Gamer"
                )
            }

        }

    }

}