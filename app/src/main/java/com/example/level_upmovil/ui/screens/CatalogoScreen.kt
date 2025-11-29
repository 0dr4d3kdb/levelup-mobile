package com.example.level_upmovil.ui.screens

import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.level_upmovil.model.Producto
import com.example.level_upmovil.navigation.Screen
import com.example.level_upmovil.ui.components.AppScaffold
import com.example.level_upmovil.viewmodel.MainViewModel
import com.example.level_upmovil.viewmodel.ProductListStatus // 💥 IMPORTACIÓN NECESARIA

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
){
    // 💥 1. Observar el estado de la API
    val productsStatus by viewModel.productsStatus.collectAsState()

    // 2. Variables de estado locales
    var searchText by remember { mutableStateOf("") }

    // 3. Obtener la lista de productos del estado (si es Success)
    val currentProducts = when (productsStatus) {
        is ProductListStatus.Success -> (productsStatus as ProductListStatus.Success).products
        else -> emptyList()
    }

    // 4. Inicializar y actualizar la lista filtrada con los datos del API
    var filteredProductos by remember(currentProducts) { mutableStateOf(currentProducts) }

    val onReviewAction: (Producto) -> Unit = { producto ->
        navController.navigate(Screen.DetalleProducto.createRoute(producto.id))
    }

    val onAddAction: (Producto) -> Unit = { producto ->
        viewModel.agregarAlCarrito(producto)
    }

    // 5. FUNCIÓN DE FILTRADO (Ahora opera sobre currentProducts)
    val performSearch: () -> Unit = {
        filteredProductos = if (searchText.isBlank()) {
            currentProducts
        } else {
            currentProducts.filter {
                it.nombre.contains(searchText, ignoreCase = true)
            }
        }
    }

    // 6. Asegurar que el filtro se aplique al cargar datos
    LaunchedEffect(currentProducts) {
        performSearch()
    }


    AppScaffold(
        navController = navController,
        viewModel = viewModel,
        title = "Catálogo"
    ) { innerPadding ->


        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            // 7. Mostrar la barra de búsqueda y el botón solo si no hay un error crítico
            if (productsStatus !is ProductListStatus.Error) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            if (it.isEmpty()) performSearch()
                        },
                        label = { Text("Buscar producto") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = performSearch,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF39FF14))
                    ) {
                        Text(text = "Buscar")
                    }
                }
            }


            // 8. BLOQUE CONDICIONAL PARA MANEJAR EL ESTADO DE CARGA/ERROR
            when (productsStatus) {

                ProductListStatus.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is ProductListStatus.Error -> {
                    val errorMessage = (productsStatus as ProductListStatus.Error).message
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "❌ Error al cargar productos: $errorMessage", textAlign = TextAlign.Center, modifier = Modifier.padding(24.dp))
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { viewModel.fetchHomeProductsByIds() }) {
                            Text("Reintentar Carga")
                        }
                    }
                }

                is ProductListStatus.Success, ProductListStatus.Idle -> {
                    // Mostrar la lista (se usará la lista filtrada)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredProductos){ producto ->
                            ProductoCard(
                                producto = producto,
                                onReviewClick = onReviewAction,
                                onAddToCartClick = onAddAction,
                                viewModel = viewModel,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

// ProductoCard (No necesita cambios si ya usa el modelo Producto)
@Composable
fun ProductoCard(
    producto: Producto,
    onReviewClick: (Producto) -> Unit,
    onAddToCartClick: (Producto) -> Unit,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .height(320.dp)
            .clickable{onReviewClick(producto)},
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 15.dp)
        ) {
            AsyncImage(
                // 🛑 Reemplaza 'imageResId' por 'imageUrl' si cambiaste el nombre del campo.
                model = producto.imageResId,
                contentDescription = producto.nombre,

                // Estos modificadores de estilo se mantienen iguales:
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .padding(top = 15.dp),

                // Añade el escalado si lo necesitas:
                contentScale = androidx.compose.ui.layout.ContentScale.Fit

            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = producto.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 2,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "$${viewModel.formatPrice(producto.precio)}", // Asumiendo que 'producto.precio' es String o tiene formato adecuado
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = {onAddToCartClick(producto)},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF39FF14))
            ) {
                Text(text = "Agregar al carro")
            }
        }
    }
}