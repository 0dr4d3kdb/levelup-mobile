package com.example.level_upmovil.ui.screens

import com.example.level_upmovil.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.level_upmovil.model.Producto
import com.example.level_upmovil.model.listaProductos
import com.example.level_upmovil.ui.components.AppScaffold
import com.example.level_upmovil.viewmodel.MainViewModel

@Composable
fun DetalleProductoScreen(
    productoId: Int,
    navController: NavController,
    viewModel: MainViewModel,
    onAddToCartClick: (Producto) -> Unit
){

    val producto = listaProductos.find { it.id == productoId }
    val scrollState = rememberScrollState()

    if (producto == null){
        Text(
            text = "Error, producto con ID $productoId no encontrado",
            modifier = Modifier.padding(16.dp)
        )
        return
    }

    AppScaffold(
        navController = navController,
        viewModel = viewModel,
        title = "Detalle del producto"
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ){
            Image(
                painter = painterResource(id = producto.imageResId),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = producto.nombre,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = producto.descripcion
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = producto.precio
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {onAddToCartClick(producto)}
            ) {
                Text(text = "Agregar al carro")
            }
        }
    }

}