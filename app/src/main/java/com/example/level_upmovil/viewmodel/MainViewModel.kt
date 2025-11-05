package com.example.level_upmovil.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_upmovil.model.CartItem
import com.example.level_upmovil.model.Producto
import com.example.level_upmovil.model.listaProductos // 1. Importar la lista estática
import com.example.level_upmovil.navigation.NavigationEvent
import com.example.level_upmovil.navigation.Screen
import kotlinx.coroutines.flow.Flow // Necesario para la firma anterior
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()

    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    private val _uiEvents = MutableSharedFlow<String>()
    val uiEvents = _uiEvents.asSharedFlow()

    fun navigateTo(screen: Screen){
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(route = screen))
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
