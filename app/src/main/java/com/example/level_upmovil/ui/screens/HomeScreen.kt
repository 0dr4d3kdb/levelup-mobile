package com.example.level_upmovil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.level_upmovil.ui.model.Producto
import com.example.level_upmovil.ui.model.productos
import com.example.level_upmovil.ui.theme.OrbitronFamily


@Composable
fun PromotionalBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),

        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        AsyncImage(

            model = "https://img.freepik.com/psd-gratis/plantilla-banner-web-viernes-negro-super-venta_120329-3858.jpg",
            contentDescription = "Banner de Promoción",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp), // Altura adecuada para un banner
            contentScale = ContentScale.Crop
        )
    }
}
@Composable
fun HomeScreen(onNavigateToProfile: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.background) {


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            item {
                WelcomeBanner(onNavigateToProfile)
            }
            item {
                PromotionalBanner()
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Nuestros Productos",
                    style = MaterialTheme.typography.headlineMedium.copy(fontFamily = OrbitronFamily),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
            item {
                ProductListRow(products = productos)
            }
        }
    }
}


@Composable
fun WelcomeBanner(onNavigateToProfile: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "BIENVENIDO A LEVEL UP",
                style = MaterialTheme.typography.headlineLarge.copy(fontFamily = OrbitronFamily),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNavigateToProfile) {
                Text("Ver Perfil")
            }
        }
    }
}


@Composable
fun ProductListRow(products: List<Producto>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(products) { producto ->
            ProductCard(producto) // Llama a la tarjeta individual
        }
    }
}


@Composable
fun ProductCard(producto: Producto) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(260.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = producto.image,
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = producto.descripcion,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "$ ${producto.precio}",
                style = MaterialTheme.typography.titleLarge.copy(fontFamily = OrbitronFamily),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}