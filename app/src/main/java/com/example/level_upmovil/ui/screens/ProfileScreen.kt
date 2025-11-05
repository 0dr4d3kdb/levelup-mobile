package com.example.level_upmovil.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // Importación necesaria
import androidx.navigation.NavController // Importación necesaria
import coil.compose.AsyncImage
import com.example.level_upmovil.navigation.Screen // Importación necesaria
import com.example.level_upmovil.ui.components.AppScaffold // Importación del Scaffold
import com.example.level_upmovil.ui.theme.OrbitronFamily
import com.example.level_upmovil.viewmodel.MainViewModel // Importación necesaria


@Composable
fun ProfilePicture(
    imageUri: Uri?,
    onCaptureImage: () -> Unit
) {
    val size = 150.dp

    Box(
        modifier = Modifier.size(size)
    ) {
        Surface(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            color = MaterialTheme.colorScheme.surface
        ) {
            if (imageUri == null) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Placeholder de Perfil",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.size(size * 0.7f)
                    )
                }
            } else {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Foto de Perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }
        }

        FloatingActionButton(
            onClick = onCaptureImage,
            modifier = Modifier.align(Alignment.BottomEnd).size(40.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Filled.CameraAlt, contentDescription = "Cambiar Foto", tint = Color.White)
        }
    }
}

// 1. Firma actualizada para aceptar NavController y MainViewModel
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }


    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            profileImageUri = uri
        }
    )

    // 2. Envolvemos la pantalla en el AppScaffold
    AppScaffold(
        navController = navController,
        viewModel = viewModel,
        title = "Perfil" // Título para la TopAppBar
    ) { innerPadding -> // 3. Aplicamos el innerPadding

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Aplicar padding aquí
                .padding(24.dp), // Padding original de la pantalla
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "PERFIL DE USUARIO",
                style = MaterialTheme.typography.headlineLarge.copy(fontFamily = OrbitronFamily),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            ProfilePicture(
                imageUri = profileImageUri,
                onCaptureImage = {
                    imagePickerLauncher.launch("image/*")
                }
            )
            Button(
                // 4. Conectamos la navegación al ViewModel
                onClick = { viewModel.navigateTo(Screen.Home) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Volver a la Pantalla Principal")
            }

            Spacer(modifier = Modifier.height(32.dp))

            ProfileInfoItem(label = "Nombre", value = "Arturo González")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            ProfileInfoItem(label = "Email", value = "arturo@gmail.com")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            ProfileInfoItem(label = "Nivel", value = "12")
        }
    }
}

@Composable
fun ProfileInfoItem(label: String, value: String) {

    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
