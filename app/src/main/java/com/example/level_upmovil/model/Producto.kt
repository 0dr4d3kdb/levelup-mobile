package com.example.level_upmovil.model
import com.example.level_upmovil.R
import com.google.gson.annotations.SerializedName


data class Producto(
    val id: Int,
    val nombre: String,
    val precio: String,
    @SerializedName("imagenUrl")
    val imageResId: String,
    @SerializedName("descripcion")
    val descripcion: String?= "Sin descripcion disponible",
    val categoria: String? = null, // Si el API no la manda, no falla
    val precioInt: Int? = null
)

//val listaProductos = listOf(
//    Producto(1,"Catan",
//        "$29.990 clp",R.drawable.catan,
//        "Un clásico juego de estrategia donde los jugadores compiten por colonizar" +
//                " y expandirse en la isla de Catan. Ideal para 3-4 jugadores y perfecto para noches" +
//                " de juego en familia o con amigos.","Juegos de mesa",29990),
//    Producto(2,"Carcassonne",
//        "$24.990 clp",R.drawable.carcassonne,
//        "Un juego de colocación de fichas donde los jugadores construyen el" +
//                " paisaje alrededor de la fortaleza medieval de Carcassonne. " +
//                "Ideal para 2-5 jugadores y fácil de aprender.","Juegos de mesa",24990),
//    Producto(3,"Controlador Xbox Series X",
//        "$59.990 clp",R.drawable.controlxbox,
//        "Ofrece una experiencia de juego cómoda con botones mapeables y" +
//                " una respuesta táctil mejorada. Compatible con consolas Xbox y PC.","Accesorios",59990),
//    Producto(4,"Auriculares Gamer HyperX Cloud II",
//        "$79.990 clp",R.drawable.audifonos,
//        "Proporcionan un sonido envolvente de calidad con un micrófono desmontable" +
//                " y almohadillas de espuma viscoelástica para mayor comodidad durante largas" +
//                " sesiones de juego.","Accesorios",79990),
//    Producto(5,"Play Station 5",
//        "$549.990 clp",R.drawable.play5,
//        "La consola de última generación de Sony, que ofrece gráficos" +
//                " impresionantes y tiempos de carga ultrarrápidos para una experiencia" +
//                " de juego inmersiva.","Consolas",549990),
//    Producto(6,"PC ASUS ROG Strix",
//        "$1.299.990 clp",R.drawable.pcasus,
//        "Un potente equipo diseñado para los gamers más exigentes, equipado" +
//                " con los últimos componentes para ofrecer un rendimiento" +
//                " excepcional en cualquier juego.", "PC Gamer",1299990),
//    Producto(7,"Silla Gamer Secretlab",
//        "$349.990 clp",R.drawable.silla_gamer,
//        "Diseñada para el máximo confort, esta silla ofrece un soporte ergonómico" +
//                " y personalización ajustable para sesiones de juego prolongadas."
//        ,"Sillas Gamers",349990),
//    Producto(8,"Mouse Logitech G502 HERO",
//        "$49.990 clp",R.drawable.mouse_logitech,
//        "Con sensor de alta precisión y botones personalizables, este mouse es" +
//                " ideal para gamers que buscan un control preciso y personalización."
//        ,"Mouse",49990),
//    Producto(10,"Polera Gamer 'Level-Up'",
//        "$14.990 clp",R.drawable.polera,
//        "Una camiseta cómoda y estilizada, con la posibilidad de personalizarla" +
//                " con tu gamer tag o diseño favorito.",
//        "Poleras Personalizadas",14990)
//)