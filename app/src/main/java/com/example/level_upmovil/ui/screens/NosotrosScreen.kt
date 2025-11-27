package com.example.level_upmovil.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_upmovil.ui.components.AppScaffold
import com.example.level_upmovil.viewmodel.MainViewModel
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
    val duocUC = LatLng(-33.6942529,-71.2159749)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(duocUC, 15f)
    }
    val scrollState = rememberScrollState()

    AppScaffold(
        navController = navController,
        viewModel = viewModel,
        title = "Sobre nosotros"
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "La mejor tienda de artículos gamer")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Nuestra misión es llevar la experiencia de juego al siguiente nivel.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Somos una empresa dedicada a la venta de equipos y accesorios de alta gama. ".repeat(4)) // EJEMPLO DE TEXTO LARGO
            }
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = duocUC),
                    title = "level-up gamer",
                    snippet = "Tienda física level-up gamer"
                )
            }

        }

    }

}