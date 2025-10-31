package com.example.level_upmovil.ui.screens

import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import com.example.level_upmovil.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_upmovil.model.Producto
import com.example.level_upmovil.model.listaProductos
import com.example.level_upmovil.navigation.Screen
import com.example.level_upmovil.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    // val scrollState = rememberScrollState() // Innecesario con LazyVerticalGrid
    val productos = listaProductos

    // 1. ESTADO DE BÚSQUEDA Y FILTRADO
    var searchText by remember { mutableStateOf("") }
    var filteredProductos by remember { mutableStateOf(productos) }

    val onReviewAction: (Producto) -> Unit = { producto ->
        println("Abriendo reseña para: ${producto.nombre}")
        // Aquí puedes usar navController.navigate(...)
    }

    val onAddAction: (Producto) -> Unit = { producto ->
        println("Agregando al carrito: ${producto.nombre}")
        // Aquí iría la lógica del ViewModel
    }

    // 2. FUNCIÓN DE FILTRADO
    val performSearch: () -> Unit = {
        filteredProductos = if (searchText.isBlank()) {
            productos
        } else {
            productos.filter {
                it.nombre.contains(searchText, ignoreCase = true)
            }
        }
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(text = "Menú", modifier = Modifier.padding(16.dp))
                // ... (NavigationDrawerItems)
                NavigationDrawerItem(
                    label = {Text(text = "Inicio")},
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Home)
                    }
                )

                NavigationDrawerItem(
                    label = {Text("Perfil")},
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Profile)
                    }
                )

                NavigationDrawerItem(
                    label = {Text("Catálogo")},
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Catalogo)
                    }
                )
            }
        }

    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {Text(text = "Catálogo")},
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) { Icon(Icons.Default.Menu, contentDescription = "Menú") }
                    }
                )
            }
        ) { innerPadding ->

            // CONTENEDOR PRINCIPAL: COLUMN
            Column(
                modifier = Modifier
                    .padding(innerPadding) // Aplica el padding del TopBar
                    .fillMaxSize()
            ) {

                // --- BARRA DE BÚSQUEDA (ROW) ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    OutlinedTextField(
                        value = searchText,
                        // 3. ASIGNACIÓN CORREGIDA: searchText = it
                        onValueChange = {
                            searchText = it
                            // Opcional: Filtrar automáticamente cuando el usuario borra todo
                            if (it.isEmpty()) performSearch()
                        },
                        label = { Text("Buscar producto") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = performSearch, // Llama a la función de filtrado
                    ) {
                        Text(text = "Buscar")
                    }
                }

                // --- GRILLA DE PRODUCTOS (LAZYVERTICALGRID) ---
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),

                    modifier = Modifier
                        // Ocupa el espacio restante de la Column. Quitamos el .padding(innerPadding) duplicado
                        .fillMaxSize(),

                    // Reajustamos el padding para que no se duplique con el de la Column principal
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Usamos la lista filtrada
                    items(filteredProductos){ producto ->
                        ProductoCard(
                            producto = producto,
                            onReviewClick = onReviewAction,
                            onAddToCartClick = onAddAction,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoCard(
    producto: Producto,
    onReviewClick: (Producto) -> Unit,
    onAddToCartClick: (Producto) -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .clickable{onReviewClick(producto)},
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 15.dp)
        ) {
            Image(
                painter = painterResource(id = producto.imageResId),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .padding(top = 15.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = producto.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = producto.precio,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = {onAddToCartClick(producto)}
            ) {
                Text(text = "Agregar al carro")
            }
        }
    }
}