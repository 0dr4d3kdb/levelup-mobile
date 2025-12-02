package com.example.level_upmovil.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.level_upmovil.model.Producto
import com.example.level_upmovil.ui.components.AppScaffold
import com.example.level_upmovil.viewmodel.MainViewModel
import coil.compose.AsyncImage

@Composable
fun DetalleProductoScreen(
    productoId: Int,
    navController: NavController,
    viewModel: MainViewModel
){


    val producto by viewModel.productoDetalle.collectAsState()


    LaunchedEffect(productoId) {
        viewModel.fetchProductoDetalle(productoId)
    }

    val scrollState = rememberScrollState()


    if (producto == null){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator() // 💡 Indicador visual mientras carga
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Cargando detalles del producto ID $productoId...",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        return
    }


    val currentProducto = producto!!

    val onAddToCartClick: (Producto) -> Unit = { p ->
        viewModel.agregarAlCarrito(p)
    }

    AppScaffold(
        navController = navController,
        viewModel = viewModel,
        title = currentProducto.nombre
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ){

            AsyncImage(
                model = currentProducto.imageResId,
                contentDescription = currentProducto.nombre,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = currentProducto.nombre,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // La descripción usa el valor seguro del objeto cargado
            Text(
                text = currentProducto.descripcion ?: "Información detallada no disponible."
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "$${viewModel.formatPrice(currentProducto.precio)}"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {onAddToCartClick(currentProducto)},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF39FF14)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Agregar al carro")
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}