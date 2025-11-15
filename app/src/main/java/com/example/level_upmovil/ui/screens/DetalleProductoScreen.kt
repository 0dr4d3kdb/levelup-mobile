package com.example.level_upmovil.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.level_upmovil.model.Producto
import com.example.level_upmovil.ui.components.AppScaffold
import com.example.level_upmovil.viewmodel.MainViewModel

// 💥 NUEVA IMPORTACIÓN DE COIL
import coil.compose.AsyncImage

@Composable
fun DetalleProductoScreen(
    productoId: Int,
    navController: NavController,
    viewModel: MainViewModel
){
    // 💥 1. Obtener el producto del ViewModel (que usa la lista cargada de la API)
    // Usamos viewModel.productos.collectAsState().value para tener los productos más recientes del API
    // y luego buscamos por ID.
    val producto = viewModel.getProductoById(productoId)

    val scrollState = rememberScrollState()

    if (producto == null){
        // 💥 Mostrar un indicador de carga o un mensaje de "no encontrado" si la lista está vacía
        Text(
            text = "Buscando producto con ID $productoId...",
            modifier = Modifier.padding(16.dp)
        )
        // Puedes añadir un CircularProgressIndicator aquí si la lista de productos aún está cargando.
        return
    }

    val onAddToCartClick: (Producto) -> Unit = { producto ->
        viewModel.agregarAlCarrito(producto)
    }

    AppScaffold(
        navController = navController,
        viewModel = viewModel,
        title = producto.nombre // Usamos el nombre del producto encontrado
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ){

            // 💥 2. Reemplazamos Image/painterResource por AsyncImage (Coil)
            AsyncImage(
                model = producto.imageResId, // Asumiendo que el campo String con la URL es 'imageUrl'
                contentDescription = producto.nombre,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = producto.nombre,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            // 🛑 Asumo que tu Producto.kt tiene un campo 'descripcion'
            Text(
                text = producto.descripcion
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = producto.precio
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {onAddToCartClick(producto)},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF39FF14)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Agregar al carro")
            }
            Spacer(modifier = Modifier.height(32.dp)) // Espacio al final para el scroll
        }
    }
}