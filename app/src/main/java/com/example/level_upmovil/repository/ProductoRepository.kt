package com.example.level_upmovil.repository

import android.app.Service
import com.example.level_upmovil.model.Producto
import com.example.level_upmovil.remote.ApiServiceUsuario
import com.example.level_upmovil.remote.RetrofitClient

class ProductoRepository (
    private val apiService: ApiServiceUsuario
){

    suspend fun getProductos(): List<Producto>{

        val response = apiService.getAllProductos()

        if (response.isSuccessful){
            return response.body() ?: emptyList()
        }else{
            println("Error al obtener productos: ${response.code()}")
            return emptyList()
        }
    }

    suspend fun getProductoPorId(id: Int): Producto? {
        val response = apiService.getProductoPorId(id)

        if (response.isSuccessful) {
            // Retorna el producto o null si el cuerpo está vacío
            return response.body()
        } else {
            // Manejar errores de la API (ej: 404 Not Found)
            println("Error al obtener producto ID $id: ${response.code()}")
            throw Exception("Fallo al cargar el producto") // Lanza una excepción que el ViewModel puede capturar
        }
    }
}