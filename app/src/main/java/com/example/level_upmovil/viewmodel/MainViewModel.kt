package com.example.level_upmovil.viewmodel


import retrofit2.HttpException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_upmovil.model.CartItem
import com.example.level_upmovil.model.Producto
import com.example.level_upmovil.model.Usuario
import com.example.level_upmovil.model.listaProductos
import com.example.level_upmovil.navigation.NavigationEvent
import com.example.level_upmovil.navigation.Screen
import com.example.level_upmovil.remote.ApiServiceUsuario
import com.example.level_upmovil.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException


sealed class RegistrationStatus {
    data object Idle : RegistrationStatus()
    data object Loading : RegistrationStatus()
    data object Success : RegistrationStatus()
    data class Error(val message: String) : RegistrationStatus()
}


// 2. Quitamos 'productoDao' del constructor
class MainViewModel : ViewModel() {

    // 3. Reemplazamos el Flow de Room con la lista estática
    //    (Usamos un StateFlow para que CatalogoScreen siga funcionando si usaba collectAsState)
    private val _productos = MutableStateFlow(listaProductos)
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    // 4. Creamos la función de búsqueda en la lista estática (usando Int)
    fun getProductoById(id: Int): Producto? {
        return listaProductos.find { it.id == id }
    }
    private val _registrationStatus = MutableStateFlow<RegistrationStatus>(RegistrationStatus.Idle)
    val registrationStatus: StateFlow<RegistrationStatus> = _registrationStatus.asStateFlow()

    // 2. Función de Registro
    fun registerNewUser(nombre: String, correo: String, password: String) {
        // Ejecutamos en el background
        viewModelScope.launch {
            _registrationStatus.value = RegistrationStatus.Loading // Mostrar spinner

            val newUser = Usuario(nombre = nombre, correo = correo, password = password)

            try {
                // 🛑 Asumimos que tienes una instancia de ApiService aquí (Ej: RetrofitClient.apiService)
                val response = RetrofitClient.apiServiceUsuario.saveUsuario(newUser)

                if (response.isSuccessful) {
                    _registrationStatus.value = RegistrationStatus.Success // Éxito!
                    // Navegar al Login o a Home (usando tu método navigateTo)
                    navigateTo(Screen.Login, popUpRoute = Screen.Registro, inclusive = true)

                } else {
                    // Manejar errores 4xx, 5xx del servidor
                    val errorDetail = response.errorBody()?.string() ?: "Error desconocido en el servidor."
                    _registrationStatus.value = RegistrationStatus.Error("Fallo en el registro: $errorDetail")
                }
            } catch (e: IOException) {
                // Error de red (sin internet)
                _registrationStatus.value = RegistrationStatus.Error("Error de conexión. Revisa tu internet.")
            } catch (e: HttpException) {
                // Otros errores HTTP no manejados
                _registrationStatus.value = RegistrationStatus.Error("Error del servidor: ${e.message}")
            }
        }
    }

    // 3. Método para resetear el estado después de un error
    fun resetRegistrationStatus() {
        _registrationStatus.value = RegistrationStatus.Idle
    }

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

    private val _carrito = MutableStateFlow<List<CartItem>>(emptyList())
    val carrito: StateFlow<List<CartItem>> = _carrito.asStateFlow()

    fun agregarAlCarrito(producto: Producto){
        _carrito.update { currentItems ->
            val itemExistente = currentItems.find { it.producto.id == producto.id }

            if (itemExistente != null){
                currentItems.map { item ->
                    if(item.producto.id == producto.id){
                        item.copy(cantidad = item.cantidad + 1)
                    } else {
                        item
                    }
                }
            } else {
                currentItems + CartItem(producto = producto, cantidad = 1)
            }
        }

        viewModelScope.launch {
            _uiEvents.emit("✅ ${producto.nombre} agregado al carro.")
        }
    }

    fun removerDelCarrito(producto: Producto){
        _carrito.update { currentItems ->
            val itemExistente = currentItems.find { it.producto.id == producto.id }

            if (itemExistente != null) {
                if (itemExistente.cantidad > 1) {
                    currentItems.map { item ->
                        if (item.producto.id == producto.id){
                            item.copy(cantidad = item.cantidad -1)
                        } else {
                            item
                        }
                    }
                } else {
                    currentItems.filter { it.producto.id != producto.id }
                }
            } else {
                currentItems
            }
        }

        viewModelScope.launch {
            _uiEvents.emit("✅ Se ha removido 1 ${producto.nombre} del carro.")
        }
    }

    fun eliminarProductoDelCarrito(producto: Producto){
        _carrito.update { currentItems ->
            currentItems.filter { it.producto.id != producto.id }
        }
        viewModelScope.launch {
            _uiEvents.emit("✅ Se ha eliminado ${producto.nombre} del carro.")
        }
    }
}
