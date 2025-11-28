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
import com.example.level_upmovil.remote.RetrofitClient
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
    data object Idle : LoginStatus() // Estado inicial / listo para intentar
    data object Loading : LoginStatus() // Login en progreso
    data object Success : LoginStatus() // Login exitoso (token recibido)
    data class Error(val message: String) : LoginStatus() // Fallo en la API o credenciales incorrectas
}

class MainViewModel : ViewModel() {

    // 1. INSTANCIA DEL SERVICIO
    private val apiService = RetrofitClient.apiServiceUsuario

    // --- LÓGICA DE PRODUCTOS ---

    // Estado del Catálogo (usando ProductListStatus para errores y carga)
    private val _productsStatus = MutableStateFlow<ProductListStatus>(ProductListStatus.Idle)
    val productsStatus: StateFlow<ProductListStatus> = _productsStatus.asStateFlow()

    // 💥 [CORRECCIÓN 1]: Estado para el detalle de un producto
    private val _productoDetalle = MutableStateFlow<Producto?>(null)
    val productoDetalle: StateFlow<Producto?> = _productoDetalle.asStateFlow()

    // 💥 [CORRECCIÓN 2]: Estado para los productos de la Home Screen
    private val _homeProducts = MutableStateFlow<List<Producto>>(emptyList())
    val homeProducts: StateFlow<List<Producto>> = _homeProducts.asStateFlow()


    // Flujo simplificado que extrae la lista de productos del estado ProductListStatus (para búsqueda, etc.)
    val productos: StateFlow<List<Producto>> = _productsStatus.map { status ->
        when (status) {
            is ProductListStatus.Success -> status.products
            else -> emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())



    init {
        fetchCatalogoProductosByIds()
    }


    fun fetchCatalogoProductosByIds() {
        viewModelScope.launch {
            _productsStatus.value = ProductListStatus.Loading
            val idsToFetch = listOf(5, 6, 7, 8, 9, 10)
            val buffer = mutableListOf<Producto>()

            for (id in idsToFetch) {
                try {
                    val producto = apiService.getProductoPorId(id)
                    buffer.add(producto)
                } catch (e: Exception) {
                    // Si falla un ID, registra el error y actualiza el estado si es necesario
                    Log.e("CatalogoAPI", "Fallo al cargar producto ID $id: ${e.message}")
                    // Si falla, podrías decidir cambiar el estado a Error, pero lo mantendremos en buffer.
                }
            }

            // 3. Actualizamos el StateFlow de Catálogo
            if (buffer.isNotEmpty()) {
                _productsStatus.value = ProductListStatus.Success(buffer)
            } else {
                // Si el buffer está vacío después de las llamadas (ej: todos los IDs fallaron)
                _productsStatus.value = ProductListStatus.Error("No se pudieron cargar los productos del catálogo (IDs 5-10).")
            }
        }
    }

    private val _homeProductsById = MutableStateFlow<List<Producto>>(emptyList())
    val homeProductsById: StateFlow<List<Producto>> = _homeProductsById.asStateFlow()



    fun fetchHomeProductsByIds() {
        viewModelScope.launch {
            // Los IDs que quieres cargar (1 al 4)
            val idsToFetch = listOf(1, 2, 3, 4)
            val buffer = mutableListOf<Producto>()

            // Limpiamos la lista al empezar
            _homeProductsById.value = emptyList()

            for (id in idsToFetch) {
                try {
                    // Llama al endpoint GET /api/productos/{id} para cada ID
                    val producto = apiService.getProductoPorId(id)
                    buffer.add(producto)
                } catch (e: Exception) {
                    // Si falla un ID, registra el error y continúa con el siguiente
                    Log.e("HomeAPI", "Fallo al cargar producto ID $id: ${e.message}")
                }
            }

            // Actualizamos el StateFlow solo una vez con los resultados
            _homeProductsById.value = buffer
        }
    }

    // Función de búsqueda local (usa la lista del Catálogo)
    fun getProductoById(id: Int): Producto? {
        return productos.value.find { it.id == id }
    }
//logica de login
var authToken: String? = null
    private set
    private val _loginStatus = MutableStateFlow<LoginStatus>(LoginStatus.Idle)
    val loginStatus: StateFlow<LoginStatus> = _loginStatus.asStateFlow()

    fun loginUser(correo: String, password: String) {
        viewModelScope.launch {
            // 1. Iniciar estado de carga
            _loginStatus.value = LoginStatus.Loading

            // Asumo que el modelo Usuario se usa para enviar las credenciales
            val loginUser = Usuario(nombre = "", correo = correo, password = password)

            try {
                // 2. Llamada a la API (Retrofit)
                val response = RetrofitClient.apiServiceUsuario.loginUsuario(loginUser)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        // 3. Éxito: Guardar el token
                        authToken = loginResponse.token
                        Log.i("LOGIN_SUCCESS", "Token JWT recibido y guardado: $authToken")

                        // 4. Emitir éxito y navegar
                        _loginStatus.value = LoginStatus.Success
                        // Nota: Aquí se asume que tienes la función navigateTo disponible
                         navigateTo(Screen.Home, popUpRoute = Screen.Login, inclusive = true)
                    } else {
                        _loginStatus.value = LoginStatus.Error("Respuesta vacía del servidor al iniciar sesión.")
                    }
                } else {
                    // Fallo: error de credenciales (ej: HTTP 401 Unauthorized)
                    val errorBody = response.errorBody()?.string() ?: "Error de credenciales."
                    _loginStatus.value = LoginStatus.Error("Fallo en el login. Verifica tus credenciales.")
                    Log.e("LOGIN_API", "Fallo: ${response.code()} - $errorBody")
                }
            } catch (e: IOException) {
                // 5. Error de red
                _loginStatus.value = LoginStatus.Error("Error de conexión. No se pudo conectar al servidor.")
            } catch (e: HttpException) {
                // 6. Otro error HTTP del servidor
                _loginStatus.value = LoginStatus.Error("Error del servidor HTTP: ${e.code()}. Intenta más tarde.")
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
                val response = RetrofitClient.apiServiceUsuario.registerUsuario(newUser)

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
                _registrationStatus.value = RegistrationStatus.Error("Error del servidor: ${e.code()}")
            }
        }
    }

    fun resetRegistrationStatus() {
        _registrationStatus.value = RegistrationStatus.Idle
    }

    // --- LÓGICA DE NAVEGACIÓN (Tus funciones existentes) ---
    // ... (Navigation Events Code)
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

    // --- LÓGICA DEL CARRITO (Tus funciones existentes) ---
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

        // Usamos es-CL para el formato chileno (punto como separador de miles)
        val formatter = NumberFormat.getNumberInstance(Locale("es", "CL"))

        formatter.maximumFractionDigits = 0
        formatter.minimumFractionDigits = 0

        return formatter.format(price)
    }
}