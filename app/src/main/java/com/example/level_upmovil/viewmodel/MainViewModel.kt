package com.example.level_upmovil.viewmodel

import android.util.Log
import retrofit2.HttpException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_upmovil.model.CartItem
import com.example.level_upmovil.model.Producto
import com.example.level_upmovil.model.Usuario
import com.example.level_upmovil.navigation.NavigationEvent
import com.example.level_upmovil.navigation.Screen
import com.example.level_upmovil.remote.ApiServiceUsuario
import com.example.level_upmovil.remote.RetrofitClient
import com.example.level_upmovil.repository.ProductoRepository
import com.example.level_upmovil.repository.UsuarioRepository // Importación necesaria
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.NumberFormat
import java.util.Locale

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
sealed class LoginStatus {
    data object Idle : LoginStatus()
    data object Loading : LoginStatus()
    data object Success : LoginStatus() // Login exitoso
    data class Error(val message: String) : LoginStatus()
}

class MainViewModel : ViewModel() {

    // 1. INSTANCIA DEL SERVICIO (Necesaria para inyectar en Repositorios)
    private val apiService = RetrofitClient.apiServiceUsuario

    private val productoRepository = ProductoRepository(apiService)

    private val usuarioRepository = UsuarioRepository(apiService)

    // --- LÓGICA DE PRODUCTOS ---

    private val _productsStatus = MutableStateFlow<ProductListStatus>(ProductListStatus.Idle)
    val productsStatus: StateFlow<ProductListStatus> = _productsStatus.asStateFlow()

    private val _productoDetalle = MutableStateFlow<Producto?>(null)
    val productoDetalle: StateFlow<Producto?> = _productoDetalle.asStateFlow()

    private val _homeProducts = MutableStateFlow<List<Producto>>(emptyList())
    val homeProducts: StateFlow<List<Producto>> = _homeProducts.asStateFlow()


    val productos: StateFlow<List<Producto>> = _productsStatus.map { status ->
        when (status) {
            is ProductListStatus.Success -> status.products
            else -> emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    init {
        fetchCatalogoProductosByIds()
    }

    fun fetchAllProductos() {
        viewModelScope.launch {
            _productsStatus.value = ProductListStatus.Loading

            try {
                val productos = productoRepository.getProductos()  // 💥 Obtener TODOS desde la API/BD
                _productsStatus.value = ProductListStatus.Success(productos)
            } catch (e: Exception) {
                _productsStatus.value = ProductListStatus.Error("Error: ${e.message}")
            }
        }
    }

    fun fetchCatalogoProductosByIds() {
        viewModelScope.launch {
            _productsStatus.value = ProductListStatus.Loading
            val idsToFetch = listOf(5, 6, 7, 8, 9, 10)
            val buffer = mutableListOf<Producto>()

            for (id in idsToFetch) {
                try {
                    val producto = productoRepository.getProductoPorId(id)
                    if (producto != null) {
                        buffer.add(producto)
                    }
                } catch (e: Exception) {
                    Log.e("CatalogoAPI", "Fallo al cargar producto ID $id: ${e.message}")
                }
            }

            if (buffer.isNotEmpty()) {
                _productsStatus.value = ProductListStatus.Success(buffer)
            } else {
                _productsStatus.value = ProductListStatus.Error("No se pudieron cargar los productos del catálogo (IDs 5-10).")
            }
        }
    }

    private val _homeProductsById = MutableStateFlow<List<Producto>>(emptyList())
    val homeProductsById: StateFlow<List<Producto>> = _homeProductsById.asStateFlow()


    fun fetchHomeProductsByIds() {
        viewModelScope.launch {
            val idsToFetch = listOf(1, 2, 3, 4)
            val buffer = mutableListOf<Producto>()
            _homeProductsById.value = emptyList()

            for (id in idsToFetch) {
                try {
                    val producto = productoRepository.getProductoPorId(id)
                    if (producto != null) {
                        buffer.add(producto)
                    }
                } catch (e: Exception) {
                    Log.e("HomeAPI", "Fallo al cargar producto ID $id: ${e.message}")
                }
            }
            _homeProductsById.value = buffer
        }
    }

    fun fetchProductoDetalle(id: Int) {
        viewModelScope.launch {
            try {
                _productoDetalle.value = productoRepository.getProductoPorId(id)
            } catch (e: Exception) {
                Log.e("DetalleProducto", "Error cargando producto $id: ${e.message}")
                _productoDetalle.value = null
            }
        }
    }


    fun getProductoById(id: Int): Producto? {
        return productos.value.find { it.id == id }
    }

    // --- LÓGICA DE LOGIN ---

    var authToken: String? = null
        private set

    private val _loginStatus = MutableStateFlow<LoginStatus>(LoginStatus.Idle)
    val loginStatus: StateFlow<LoginStatus> = _loginStatus.asStateFlow()

    fun loginUser(correo: String, password: String) {
        viewModelScope.launch {
            _loginStatus.value = LoginStatus.Loading
            val loginUser = Usuario(nombre = "", correo = correo, password = password)

            try {

                val success = usuarioRepository.loginUsuarios(loginUser)

                if (success) {

                    _loginStatus.value = LoginStatus.Success
                    navigateTo(Screen.Home, popUpRoute = Screen.Login, inclusive = true)
                }

            } catch (e: Exception) {

                Log.e("LOGIN_ERROR", "Login failed: ${e.message}")
                val errorMessage = e.message ?: "Error desconocido al iniciar sesión."
                _loginStatus.value = LoginStatus.Error(errorMessage)
            }
        }
    }

    fun resetLoginStatus() {
        _loginStatus.value = LoginStatus.Idle
    }


    // --- LÓGICA DE REGISTRO ---
    private val _registrationStatus = MutableStateFlow<RegistrationStatus>(RegistrationStatus.Idle)
    val registrationStatus: StateFlow<RegistrationStatus> = _registrationStatus.asStateFlow()

    fun registerNewUser(nombre: String, correo: String, password: String) {
        viewModelScope.launch {
            _registrationStatus.value = RegistrationStatus.Loading
            val newUser = Usuario(nombre = nombre, correo = correo, password = password)

            try {

                usuarioRepository.registerUsuario(newUser)


                _registrationStatus.value = RegistrationStatus.Success
                navigateTo(Screen.Login, popUpRoute = Screen.Registro, inclusive = true)

            } catch (e: Exception) {

                val errorMessage = e.message ?: "Fallo desconocido en el registro."
                _registrationStatus.value = RegistrationStatus.Error(errorMessage)
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

    fun navigateTo(screen: Screen, popUpRoute: Screen? = null, inclusive: Boolean = false, singleTop: Boolean = false){
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(route = screen, popUpRoute = popUpRoute, inclusive = inclusive, singleTop = singleTop))
        }
    }

    fun navigateBack() {
        viewModelScope.launch { _navigationEvents.emit(NavigationEvent.PopBackStack) }
    }

    fun navigateUp(){
        viewModelScope.launch { _navigationEvents.emit(NavigationEvent.NavigateUp) }
    }
    // --------------------------------------------------------

    // --- LÓGICA DEL CARRITO ---
    private val _carrito = MutableStateFlow<List<CartItem>>(emptyList())
    val carrito: StateFlow<List<CartItem>> = _carrito.asStateFlow()

    fun agregarAlCarrito(producto: Producto){
        _carrito.update { currentItems ->
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
        _carrito.update { currentItems -> currentItems.filter { it.producto.id != producto.id } }
        viewModelScope.launch { _uiEvents.emit("✅ Se ha eliminado ${producto.nombre} del carro.") }
    }

    // --- LÓGICA DE FORMATO ---
    fun formatPrice(price: Number?): String {
        if (price == null) return "Precio no disponible"

        val formatter = NumberFormat.getNumberInstance(Locale("es", "CL"))
        formatter.maximumFractionDigits = 0
        formatter.minimumFractionDigits = 0

        return formatter.format(price)
    }
}