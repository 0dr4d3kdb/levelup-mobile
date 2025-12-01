package com.example.level_upmovil

import com.example.level_upmovil.model.LoginResponse
import com.example.level_upmovil.model.RegistroResponse
import com.example.level_upmovil.model.Usuario
import com.example.level_upmovil.remote.ApiServiceUsuario
import com.example.level_upmovil.repository.UsuarioRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.IOException

class UsuarioRepositoryTest: StringSpec({

    val usuarioFake = Usuario(
        1,
        "Daniel",
        "daniel@gmail.com",
        "abcd1234"
    )

    val apiService = mockk<ApiServiceUsuario>()

    val repository = UsuarioRepository(apiService)

    // --- PRUEBAS PARA registerUsuario ---

    "El registro debe retornar true al recibir una respuesta exitosa (HTTP 200/201)" {
        coEvery { apiService.registerUsuario(usuarioFake) } returns Response.success(RegistroResponse("Usuario registrado correctamente"))

        runTest {
            val result = repository.registerUsuario(usuarioFake)
            result.shouldBeTrue()
        }
    }

    "El registro debe lanzar una Exception si la respuesta es un error de la API (ej: HTTP 409 Conflict)" {
        val errorBody = "{\"error\": \"El correo ya está en uso\"}".toResponseBody("application/json".toMediaTypeOrNull())
        coEvery { apiService.registerUsuario(usuarioFake) } returns Response.error(409, errorBody)

        runTest {
            var caughtException: Exception? = null // Inicializamos una variable para capturar la excepción

            try {
                repository.registerUsuario(usuarioFake)
            } catch (e: Exception) {
                caughtException = e // Capturamos la excepción
            }

            // 1. Aserción 1: Verificamos que se haya capturado una excepción
            caughtException.shouldNotBeNull()
            // 2. Aserción 2: Verificamos que el mensaje no esté vacío
            caughtException!!.message.shouldNotBeEmpty()
        }
    }

    "El registro debe lanzar una Exception si hay un error de red (IOException)" {
        coEvery { apiService.registerUsuario(usuarioFake) } throws IOException("No hay conexión a internet")

        runTest {
            var caughtException: Exception? = null

            try {
                repository.registerUsuario(usuarioFake)
            } catch (e: Exception) {
                caughtException = e
            }

            // 1. Aserción 1: Verificamos que se haya capturado una excepción
            caughtException.shouldNotBeNull()
            // 2. Aserción 2: Verificamos el mensaje específico
            caughtException!!.message shouldBe "Error de conexión. Revisa tu internet."
        }
    }

    "El login debe lanzar una Exception si las credenciales son inválidas" {
        val errorBody = "{\"error\": \"Credenciales inválidas\"}".toResponseBody("application/json".toMediaTypeOrNull())
        coEvery { apiService.loginUsuario(usuarioFake) } returns Response.error(401, errorBody)

        runTest {
            var caughtException: Exception? = null

            try {
                repository.loginUsuarios(usuarioFake)
            } catch (e: Exception) {
                caughtException = e
            }

            // 1. Aserción 1: Verificamos que se haya capturado una excepción
            caughtException.shouldNotBeNull()
            // 2. Aserción 2: Verificamos que el mensaje no esté vacío
            caughtException!!.message.shouldNotBeEmpty()
        }
    }

})