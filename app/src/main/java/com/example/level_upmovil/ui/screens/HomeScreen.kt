package com.example.level_upmovil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.level_upmovil.model.Producto // Usaremos el modelo Producto (del API)
import com.example.level_upmovil.navigation.Screen
import com.example.level_upmovil.ui.components.AppScaffold
import com.example.level_upmovil.ui.theme.OrbitronFamily
import com.example.level_upmovil.viewmodel.MainViewModel
import androidx.compose.foundation.clickable


// =========================================================
// FUNCIONES EXISTENTES (SIN CAMBIOS)
// =========================================================

@Composable
fun PromotionalBanner() {
    // ... (Tu código existente)
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
                .height(150.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun WelcomeBanner(onNavigateToProfile: () -> Unit) {
    // ... (Tu código existente)
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

// =========================================================
// HOME SCREEN (MODIFICADA)
// =========================================================

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    // 💥 1. Observar el StateFlow de los productos de la Home Screen (limit 4)
    val featuredProducts by viewModel.homeProductsById.collectAsState()

    // 💥 2. Activar la carga de productos al iniciar la pantalla
    LaunchedEffect(Unit) {
        viewModel.fetchHomeProductsByIds()
    }

    AppScaffold(
        navController = navController,
        viewModel = viewModel,
        title = "Inicio"
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            item {
                WelcomeBanner(onNavigateToProfile = {
                    viewModel.navigateTo(Screen.Profile)
                })
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

            // 💥 3. Mostrar la lista, o un indicador de carga si la lista está vacía
            item {
                if (featuredProducts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator() // Muestra carga
                    }
                } else {
                    // ✅ Pasar la lista observada (Producto, no ProductoHome)
                    ProductListRow(
                        products = featuredProducts,
                        navController = navController, // Necesitamos pasar navController para la navegación
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}


// =========================================================
// COMPOSABLES REQUERIDOS (MODIFICADOS)
// =========================================================

// 💥 MODIFICADA: Ahora acepta NavController y el tipo Producto (del API)
@Composable
fun ProductListRow(
    products: List<Producto>, // Usamos Producto (del API)
    navController: NavController,
    viewModel: MainViewModel
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(products) { producto ->
            // ✅ Pasamos NavController y ViewModel al ProductCard
            ProductCard(
                producto = producto,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}


// 💥 MODIFICADA: Ahora usa el modelo Producto (del API) y permite navegación/acciones
@Composable
fun ProductCard(
    producto: Producto, // Usamos Producto (del API)
    navController: NavController,
    viewModel: MainViewModel
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(260.dp)
            .clickable {
                // Navegar al detalle del producto al hacer clic
                navController.navigate(Screen.DetalleProducto.createRoute(producto.id))
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = producto.imageResId, // Usamos el campo String de la URL
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
                // 🛑 Asumo que tu modelo Producto tiene una descripción
                text = producto.descripcion ?: "Sin descripción",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.weight(1f))

            // ✅ Usar formatPrice del ViewModel
            Text(
                text = "$ ${viewModel.formatPrice(producto.precio)}",
                style = MaterialTheme.typography.titleLarge.copy(fontFamily = OrbitronFamily),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}