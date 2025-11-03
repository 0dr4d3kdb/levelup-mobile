package com.example.level_upmovil.viewmodel


import androidx.lifecycle.ViewModel
import com.example.level_upmovil.model.CartItem
import com.example.level_upmovil.model.Producto
import com.example.level_upmovil.navigation.NavigationEvent
import com.example.level_upmovil.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()

    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    fun navigateTo(screen: Screen){
        CoroutineScope(Dispatchers.Main).launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(route = screen))
        }
    }

    fun navigateBack() {
        CoroutineScope(Dispatchers.Main).launch {
            _navigationEvents.emit(NavigationEvent.PopBackStack)
        }
    }

    fun navigateUp(){
        CoroutineScope(Dispatchers.Main).launch {
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
    }
}