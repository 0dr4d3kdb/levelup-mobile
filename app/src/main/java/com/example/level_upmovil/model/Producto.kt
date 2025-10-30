package com.example.level_upmovil.model
import com.example.level_upmovil.R

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: String,
    val imageResId: Int
)

val listaProductos = listOf(
    Producto(1,"Play Station 5", "$549.990",R.drawable.play5)
)
