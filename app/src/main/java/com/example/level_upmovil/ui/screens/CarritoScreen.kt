package com.example.level_upmovil.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.runtime.getValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_upmovil.model.CartItem
import com.example.level_upmovil.navigation.Screen
import com.example.level_upmovil.ui.components.AppScaffold
import com.example.level_upmovil.viewmodel.MainViewModel

// 💥 NUEVA IMPORTACIÓN DE COIL
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
){
    val itemsCarrito by viewModel.carrito.collectAsState()

    val onProductoClick: (Int) -> Unit = {productoId ->
        navController.navigate(Screen.DetalleProducto.createRoute(productoId))
    }

    AppScaffold(
        navController = navController,
        viewModel = viewModel,
        title = "Carro de compras"
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            if (itemsCarrito.isEmpty()){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = "¡El carro de compras está vacío!")
                }
            } else { // 💥 Agregamos el else para evitar que el Box se superponga
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(itemsCarrito) {cartItem ->
                        CarritoItems(
                            cartItem = cartItem,
                            onItemClick = onProductoClick,
                            viewModel = viewModel
                        )
                    }
                }

                // Opcional: Mostrar un resumen de compra o un botón de checkout
                // Button(onClick = { /* ... */ }) { Text("Checkout") }
            }
        }
    }
}

@Composable
fun CarritoItems(
    cartItem: CartItem,
    onItemClick: (productoId: Int) -> Unit,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
){

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 💥 REEMPLAZAMOS Image/painterResource por AsyncImage
            AsyncImage(
                model = cartItem.producto.imageResId, // 💥 Usamos la URL (String)
                contentDescription = cartItem.producto.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { onItemClick(cartItem.producto.id) }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Producto: ",
                    fontWeight = FontWeight.Bold
                )
                Text(text = cartItem.producto.nombre)

                // 💥 Mostrar el precio total por item (opcional, pero buena práctica)
                val precioUnitario = cartItem.producto.precio // Esto ya es un Int
                val itemTotalPrice = precioUnitario * cartItem.cantidad

                Text(
                    text = "Precio/u: $${viewModel.formatPrice(precioUnitario)}"
                )
                Text(
                    text = "Total: $${viewModel.formatPrice(itemTotalPrice)}"
                )

                Text(
                    text = "Cant: ${cartItem.cantidad}",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Columna de Controles
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row (
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    IconButton(
                        onClick = { viewModel.removerDelCarrito(cartItem.producto) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Restar cantidad")
                    }

                    IconButton(
                        onClick = {viewModel.agregarAlCarrito(cartItem.producto) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Aumentar cantidad")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                IconButton(
                    onClick = {viewModel.eliminarProductoDelCarrito(cartItem.producto) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar del carro", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}