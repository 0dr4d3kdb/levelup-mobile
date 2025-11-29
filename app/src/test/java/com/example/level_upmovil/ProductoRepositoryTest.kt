package com.example.level_upmovil

import com.example.level_upmovil.model.Producto
import com.example.level_upmovil.remote.ApiServiceUsuario
import com.example.level_upmovil.repository.ProductoRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.coEvery
import io.mockk.mockk
import retrofit2.Response
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody

class ProductoRepositoryTest: StringSpec ({

    // --- Variables y Constantes ---
    val TEST_ID = 1
    val FAIL_ID = 999

    val dummyProducto = Producto(
        id = TEST_ID,
        nombre = "PlayStation 2 chipeada",
        precio = 100000,
        imageResId = "imagenplay.com",
        descripcion = "Play 2 chipeada",
        categoria = "consolas"
    )

    val fakeProductos = listOf(
        dummyProducto,
        Producto(2,"Audifonos",40000,"imagenaudifonos.com","Se escuchan bacán","accesorios"),
        Producto(3,"Mousepad hentai",15000,"imagenmousepad.com","Mousepad pa gente rara","mousepad")
    )

    // =================================================================
    // >>> TEST 1: getAllProductos() <<<
    // =================================================================

    "GetAllProductos() debe traer una lista de productos simulados" {

        val mockApi = mockk<ApiServiceUsuario>()
        coEvery { mockApi.getAllProductos() } returns Response.success(fakeProductos)

        val repo = ProductoRepository(mockApi)

        runTest {
            val result = repo.getProductos()
            result shouldContainExactly fakeProductos
        }
    }

    // =================================================================
    // >>> TEST 2: getProductoPorId() - Éxito <<<
    // =================================================================

    "getProductoPorId() debe devolver el producto en caso de éxito (HTTP 200)" {
        val mockApi = mockk<ApiServiceUsuario>()
        coEvery { mockApi.getProductoPorId(TEST_ID) } returns Response.success(dummyProducto)
        val repo = ProductoRepository(mockApi)

        runTest {
            val result = repo.getProductoPorId(TEST_ID)
            result shouldBe dummyProducto
        }
    }


    // >>> TEST 3: getProductoPorId() - Cuerpo Nulo <<<

    "getProductoPorId() debe devolver null si el cuerpo es nulo" {
        val mockApi = mockk<ApiServiceUsuario>()
        coEvery { mockApi.getProductoPorId(TEST_ID) } returns Response.success(null)
        val repo = ProductoRepository(mockApi)

        runTest {
            val result = repo.getProductoPorId(TEST_ID)
            result shouldBe null
        }
    }

    // =================================================================
    // >>> TEST 4: getProductoPorId() - Error HTTP <<<
    // =================================================================

    "getProductoPorId() debe lanzar una excepción en caso de error HTTP (ej: 404)" {
        val mockApi = mockk<ApiServiceUsuario>()

        // Setup para simular el error 404
        val errorBody = "{\"error\": \"No encontrado\"}".toResponseBody("application/json".toMediaTypeOrNull())
        coEvery { mockApi.getProductoPorId(FAIL_ID) } returns Response.error(404, errorBody)

        val repo = ProductoRepository(mockApi)

        // Usamos shouldThrow de Kotest
        shouldThrow<Exception> {
            runTest {
                repo.getProductoPorId(FAIL_ID)
            }
        }.message shouldBe "Fallo al cargar el producto"
    }
})