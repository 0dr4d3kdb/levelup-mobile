package com.example.level_upmovil.viewmodel

import android.util.Log
import retrofit2.HttpException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_upmovil.model.CartItem
import com.example.level_upmovil.model.Producto
import com.example.level_upmovil.model.Usuario // Necesario si aún usas 'listaProductos' en getProductoById
import com.example.level_upmovil.navigation.NavigationEvent
import com.example.level_upmovil.navigation.Screen
import com.example.level_upmovil.remote.RetrofitClient
import kotlinx.coroutines.flow.* // Importamos todo para map, stateIn, SharingStarted
import kotlinx.coroutines.launch
import java.io.IOException

// --- ESTADOS DE LA UI ---
sealed class RegistrationStatus {
    data object Idle : RegistrationStatus()
    data object Loading : RegistrationStatus()
    data object Success : RegistrationStatus()
    data class Error(val message: String) : RegistrationStatus()
}

sealed class ProductListStatus {
    data object Idle : ProductListStatus()
    data object Loading : ProductListStatus()
    data class Success(val products: List<Producto>) : ProductListStatus()
    data class Error(val message: String) : ProductListStatus()
}

class MainViewModel : ViewModel() {

    // 1. INSTANCIA DEL SERVICIO (Usamos el cliente definido en remote/RetrofitClient.kt)
    private val apiService = RetrofitClient.apiServiceUsuario

    // --- LÓGICA DE PRODUCTOS (Reemplazamos lista estática por API) ---
    private val _productsStatus = MutableStateFlow<ProductListStatus>(ProductListStatus.Idle)
    private val _productoDetalle = MutableStateFlow<Producto?>(null)
    val productoDetalle: StateFlow<Producto?> = _productoDetalle.asStateFlow()
    val productsStatus: StateFlow<ProductListStatus> = _productsStatus.asStateFlow()

    // Flujo simplificado que extrae la lista de productos del estado ProductListStatus
    val productos: StateFlow<List<Producto>> = _productsStatus.map { status ->
        when (status) {
            is ProductListStatus.Success -> status.products
            else -> emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    // Inicializar la carga de productos al crear el ViewModel
    init {
        fetchProductos()
    }

    // Función para obtener los productos de la API
    fun fetchProductos() {
        viewModelScope.launch {
            _productsStatus.value = ProductListStatus.Loading
            try {
                val response = apiService.getAllProductos()

                if (response.isSuccessful && response.body() != null) {
                    _productsStatus.value = ProductListStatus.Success(response.body()!!)
                } else {
                    val errorDetail = response.errorBody()?.string() ?: "Error desconocido al obtener productos."
                    _productsStatus.value = ProductListStatus.Error("Fallo HTTP: ${response.code()}. Detalle: $errorDetail")
                }
            } catch (e: IOException) {
                Log.e("API_CALL", "Error de red: ${e.message}") // Añade esta línea
                _productsStatus.value = ProductListStatus.Error("Error de red: Sin conexión a internet.")
            } catch (e: HttpException) {
                _productsStatus.value = ProductListStatus.Error("Error del servidor: ${e.code()}")
            }

        }
    }
    fun fetchProductoDetalle(productoId: Int) {
        viewModelScope.launch {
            // Limpiamos el estado anterior para indicar que estamos cargando algo nuevo
            _productoDetalle.value = null

            try {
                // 🛑 Llama al método de tu ApiService que usa el endpoint /productos/{id}
                val producto = RetrofitClient.apiServiceUsuario.getProductoPorId(productoId)

                _productoDetalle.value = producto

            } catch (e: IOException) {
                Log.e("DetalleAPI", "Error de red al buscar producto $productoId: ${e.message}")
                // Podrías establecer un error específico si lo necesitas
            } catch (e: HttpException) {
                Log.e("DetalleAPI", "Error HTTP al buscar producto $productoId: ${e.code()}")
            } catch (e: Exception) {
                Log.e("DetalleAPI", "Error desconocido: ${e.message}")
            }
        }
    }

    // Función de búsqueda (se mantiene la búsqueda en la lista estática original, pero DEBERÍA usar la lista del API)
    // Para ser funcional, debería buscar en la lista obtenida del API: productos.value.find { ... }
    fun getProductoById(id: Int): Producto? {
        return productos.value.find { it.id == id } // Buscamos en el StateFlow 'productos' del API
    }

    // --- LÓGICA DE REGISTRO ---
    private val _registrationStatus = MutableStateFlow<RegistrationStatus>(RegistrationStatus.Idle)
    val registrationStatus: StateFlow<RegistrationStatus> = _registrationStatus.asStateFlow()

    fun registerNewUser(nombre: String, correo: String, password: String) {
        viewModelScope.launch {
            _registrationStatus.value = RegistrationStatus.Loading

            val newUser = Usuario(nombre = nombre, correo = correo, password = password)

            try {
                val response = RetrofitClient.apiServiceUsuario.saveUsuario(newUser)

                if (response.isSuccessful) {
                    _registrationStatus.value = RegistrationStatus.Success
                    navigateTo(Screen.Login, popUpRoute = Screen.Registro, inclusive = true)

                } else {
                    val errorDetail = response.errorBody()?.string() ?: "Error desconocido en el servidor."
                    _registrationStatus.value = RegistrationStatus.Error("Fallo en el registro: $errorDetail")
                }
            } catch (e: IOException) {
                _registrationStatus.value = RegistrationStatus.Error("Error de conexión. Revisa tu internet.")
            } catch (e: HttpException) {
                _registrationStatus.value = RegistrationStatus.Error("Error del servidor: ${e.message}")
            }
        }
    }

    fun resetRegistrationStatus() {
        _registrationStatus.value = RegistrationStatus.Idle
    }

    // --- LÓGICA DE NAVEGACIÓN ---
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    private val _uiEvents = MutableSharedFlow<String>()
    val uiEvents = _uiEvents.asSharedFlow()

    fun navigateTo(screen: Screen,
                   popUpRoute: Screen? = null,
                   inclusive: Boolean = false,
                   singleTop: Boolean = false){
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(route = screen,
                popUpRoute = popUpRoute,
                inclusive = inclusive,
                singleTop = singleTop))
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.PopBackStack)
        }
    }

    fun navigateUp(){
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateUp)
        }
    }

    // --- LÓGICA DEL CARRITO ---
    private val _carrito = MutableStateFlow<List<CartItem>>(emptyList())
    val carrito: StateFlow<List<CartItem>> = _carrito.asStateFlow()

    fun agregarAlCarrito(producto: Producto){
        // ... (lógica del carrito, no modificada)
        _carrito.update { currentItems ->
            // ... (código)
            val itemExistente = currentItems.find { it.producto.id == producto.id }
            if (itemExistente != null){
                currentItems.map { item ->
                    if(item.producto.id == producto.id){ item.copy(cantidad = item.cantidad + 1) } else { item }
                }
            } else { currentItems + CartItem(producto = producto, cantidad = 1) }
        }
        viewModelScope.launch { _uiEvents.emit("✅ ${producto.nombre} agregado al carro.") }
    }

    fun removerDelCarrito(producto: Producto){
        // ... (lógica del carrito, no modificada)
        _carrito.update { currentItems ->
            val itemExistente = currentItems.find { it.producto.id == producto.id }
            if (itemExistente != null) {
                if (itemExistente.cantidad > 1) {
                    currentItems.map { item ->
                        if (item.producto.id == producto.id){ item.copy(cantidad = item.cantidad -1) } else { item }
                    }
                } else { currentItems.filter { it.producto.id != producto.id } }
            } else { currentItems }
        }
        viewModelScope.launch { _uiEvents.emit("✅ Se ha removido 1 ${producto.nombre} del carro.") }
    }

    fun eliminarProductoDelCarrito(producto: Producto){
        // ... (lógica del carrito, no modificada)
        _carrito.update { currentItems -> currentItems.filter { it.producto.id != producto.id } }
        viewModelScope.launch { _uiEvents.emit("✅ Se ha eliminado ${producto.nombre} del carro.") }
    }
}