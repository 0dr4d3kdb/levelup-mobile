package com.example.level_upmovil

import com.example.level_upmovil.model.Producto
import com.example.level_upmovil.viewmodel.MainViewModel
import com.example.level_upmovil.navigation.NavigationEvent
import com.example.level_upmovil.navigation.Screen
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest : StringSpec({

    lateinit var viewModel: MainViewModel

    beforeTest {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = MainViewModel() // usando tu constructor real
    }

    afterTest {
        Dispatchers.resetMain()
    }

    // -----------------------------------------------------
    // TEST 1 — agregar producto al carrito
    // -----------------------------------------------------
    "agregarAlCarrito debe añadir un nuevo producto con cantidad 1" {
        val producto = Producto(1, "Mouse", 1000, "", "x","y")

        runTest {
            viewModel.agregarAlCarrito(producto)
            advanceUntilIdle()

            val items = viewModel.carrito.value

            items.size shouldBe 1
            items[0].producto.nombre shouldBe "Mouse"
            items[0].cantidad shouldBe 1
        }
    }

    // -----------------------------------------------------
    // TEST 2 — eliminar producto del carrito
    // -----------------------------------------------------
    "eliminarProductoDelCarrito debe dejar el carrito vacío" {
        val producto = Producto(1, "Mouse", 1000, "", "x","y")

        runTest {
            viewModel.agregarAlCarrito(producto)
            advanceUntilIdle()

            viewModel.eliminarProductoDelCarrito(producto)
            advanceUntilIdle()

            viewModel.carrito.value.isEmpty() shouldBe true
        }
    }

    // -----------------------------------------------------
    // TEST 3 — formatPrice da formato correcto
    // -----------------------------------------------------
    "formatPrice debe formatear el número al formato chileno" {
        val formatted = viewModel.formatPrice(15990)
        formatted shouldBe "15.990"
    }

    // -----------------------------------------------------
    // TEST 4 — navigateTo emite un NavigationEvent
    // -----------------------------------------------------
    "navigateTo debe emitir un NavigationEvent.NavigateTo" {
        runTest {
            val events = mutableListOf<NavigationEvent>()

            val job = launch {
                viewModel.navigationEvents.collect {
                    events.add(it)
                }
            }

            viewModel.navigateTo(Screen.Home)
            advanceUntilIdle()

            events.isNotEmpty() shouldBe true
            events[0] shouldBe NavigationEvent.NavigateTo(
                route = Screen.Home,
                popUpRoute = null,
                inclusive = false,
                singleTop = false
            )

            job.cancel()
        }
    }
})
