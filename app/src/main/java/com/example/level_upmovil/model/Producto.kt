package com.example.level_upmovil.model
import com.example.level_upmovil.R

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: String,
    val imageResId: Int,
)

val listaProductos = listOf(
    Producto(1,"Catan", "$29.990 clp",R.drawable.catan),
    Producto(2,"Carcassonne","$24.990 clp",R.drawable.carcassonne),
    Producto(3,"Controlador Xbox Series X", "$59.990 clp",R.drawable.controlxbox),
    Producto(4,"Auriculares Gamer HyperX Cloud II", "$79.990 clp",R.drawable.audifonos),
    Producto(5,"Play Station 5", "$549.990 clp",R.drawable.play5),
    Producto(6,"PC ASUS ROG Strix", "$1.299.990 clp",R.drawable.pcasus)
)
