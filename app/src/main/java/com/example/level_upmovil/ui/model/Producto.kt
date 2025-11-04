package com.example.level_upmovil.ui.model

data class Producto(
    val id : Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val image: String
)

val productos = listOf<Producto>(
    Producto(
    1,
    "Teclado Mecánico RGB",
    "Teclado de alto rendimiento con iluminación RGB personalizable. Ideal para gamers profesionales.",
    120000.0,
    "https://i5.walmartimages.com/asr/b654b6e8-61af-431f-9b28-fe1a3a30ac00.184983326af0fd1a08375024b4189dc7.jpeg"
    ),
    Producto(
        2,
        "Mouse Gamer Ergonómico",
        "Mouse con sensor de precisión, diseño ergonómico y botones programables para una ventaja competitiva.",
        50000.0,
        "https://www.centec.cl/cdn/shop/files/pixelcut-export__2831_2920240524-24908-zkwvoi_1800x.png?v=1737574381"
    ),
    Producto(
      3,
        "Audífonos con Micrófono",
        "Sonido envolvente 7.1 y micrófono con cancelación de ruido para una comunicación clara en equipo.",
        85000.0,
        "https://factorytech.cl/cdn/shop/files/Audifonos-Gamer-Soyto-SY830-Notebooks-PS4-PC-XBOX-Blue-LED-1_dc1e9791-ce3e-4bb2-a03a-b6fb58e4a16b.jpg?v=1715442210"
    ),
    Producto(
        4,
        "Monitor Gamer 144Hz",
        "Monitor de 24 pulgadas con tasa de refresco de 144Hz y tiempo de respuesta de 1ms. Imágenes fluidas y nítidas.",
        250000.0,
        "https://www.acerstore.cl/cdn/shop/files/1_XZ342CU.png?v=1742563697"
    )
    )
