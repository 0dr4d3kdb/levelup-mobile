package com.example.level_upmovil.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.level_upmovil.navigation.Screen
import com.example.level_upmovil.ui.components.AppScaffold
import com.example.level_upmovil.ui.theme.OrbitronFamily
import com.example.level_upmovil.viewmodel.MainViewModel
import java.io.File

/* ---------------------------------- */
/* FUNCIÓN AUXILIAR URI                */
/* ---------------------------------- */
fun createImageUri(context: Context): Uri {
    val imageFile = File.createTempFile(
        "profile_",
        ".jpg",
        context.cacheDir
    )

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}

/* ---------------------------------- */
/* FOTO DE PERFIL                     */
/* ---------------------------------- */
@Composable
fun ProfilePicture(
    imageUri: Uri?,
    onGalleryClick: () -> Unit
) {
    val size = 150.dp

    Box(modifier = Modifier.size(size)) {

        Surface(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            color = MaterialTheme.colorScheme.surface
        ) {
            if (imageUri == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.size(size * 0.7f)
                    )
                }
            } else {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }
        }

        FloatingActionButton(
            onClick = onGalleryClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(40.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Filled.CameraAlt,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

/* ---------------------------------- */
/* PANTALLA DE PERFIL                 */
/* ---------------------------------- */
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    /* -------- Galería -------- */
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        profileImageUri = uri
    }

    /* -------- Cámara -------- */
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            profileImageUri = tempImageUri
        }
    }

    /* -------- Permiso Cámara -------- */
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createImageUri(context)
            tempImageUri = uri
            takePictureLauncher.launch(uri)
        }
    }

    fun openCamera() {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                val uri = createImageUri(context)
                tempImageUri = uri
                takePictureLauncher.launch(uri)
            }

            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    AppScaffold(
        navController = navController,
        viewModel = viewModel,
        title = "Perfil"
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "PERFIL DE USUARIO",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = OrbitronFamily
                ),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            ProfilePicture(
                imageUri = profileImageUri,
                onGalleryClick = { galleryLauncher.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProfileInfoItem("Nombre", "Arturo González")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            ProfileInfoItem("Email", "arturo@gmail.com")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            ProfileInfoItem("Nivel", "12")

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { openCamera() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(Icons.Filled.CameraAlt, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Abrir cámara")
            }

            Button(
                onClick = { viewModel.navigateTo(Screen.Home) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text("Volver a la Pantalla Principal")
            }
        }
    }
}

/* ---------------------------------- */
/* ITEM DE INFO                       */
/* ---------------------------------- */
@Composable
fun ProfileInfoItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.Bold)
        Text(value)
    }
}
