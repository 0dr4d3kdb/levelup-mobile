package com.example.level_upmovil.repository


import com.example.level_upmovil.model.Usuario
import com.example.level_upmovil.remote.ApiServiceUsuario
import okio.IOException
import retrofit2.HttpException

class UsuarioRepository (
    private val apiService: ApiServiceUsuario
) {
    suspend fun loginUsuarios(usuario: Usuario): Boolean{
        try {
            val response = apiService.loginUsuario(usuario)

            if (response.isSuccessful){
                return true
            } else {
                val errorBody = response.errorBody()?.string()?: "Error de credenciales desconocido"

                throw Exception("Fallo en el login: verifique credenciales.")
            }
        }catch (e: IOException){
            throw Exception("Error de conexión")
        }catch (e: HttpException){
            throw Exception("Error de servidor HTTP: ${e.code()}")
        }

    }

    suspend fun registerUsuario(usuario: Usuario): Boolean{
        try {
            val response = apiService.registerUsuario(usuario)

            if (response.isSuccessful){
                return true
            }else{
                val errorBody = response.errorBody()?.string()?: "Error desconocido en el servidor."
                throw Exception("Fallo en el registro: $errorBody")
            }
        }catch (e: IOException) {
            throw Exception("Error de conexión. Revisa tu internet.")
        } catch (e: HttpException) {
            throw Exception("Error del servidor: ${e.code()}.")
        }
    }
}