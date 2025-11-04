package com.example.level_upmovil.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.level_upmovil.R

@Entity(tableName = "producto")
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val precio: String,
    val imageResId: Int,
    val descripcion: String,
    val categoria: String,
    val precioInt: Int
)

val listaProductos = listOf(
    Producto(id = 1, nombre = "Catan",
        precio = "$29.990 clp", imageResId = R.drawable.catan,
        descripcion = "Un clásico juego de estrategia donde los jugadores compiten por colonizar" +
                " y expandirse en la isla de Catan. Ideal para 3-4 jugadores y perfecto para noches" +
                " de juego en familia o con amigos.", categoria = "Juegos de mesa", precioInt = 29990),
    Producto(id = 2, nombre = "Carcassonne",
        precio = "$24.990 clp", imageResId = R.drawable.carcassonne,
        descripcion = "Un juego de colocación de fichas donde los jugadores construyen el" +
                " paisaje alrededor de la fortaleza medieval de Carcassonne. " +
                "Ideal para 2-5 jugadores y fácil de aprender.", categoria = "Juegos de mesa", precioInt = 24990),
    Producto(id = 3, nombre = "Controlador Xbox Series X",
        precio = "$59.990 clp", imageResId = R.drawable.controlxbox,
        descripcion = "Ofrece una experiencia de juego cómoda con botones mapeables y" +
                " una respuesta táctil mejorada. Compatible con consolas Xbox y PC.", categoria = "Accesorios", precioInt = 59990),
    Producto(id = 4, nombre = "Auriculares Gamer HyperX Cloud II",
        precio = "$79.990 clp", imageResId = R.drawable.audifonos,
        descripcion = "Proporcionan un sonido envolvente de calidad con un micrófono desmontable" +
                " y almohadillas de espuma viscoelástica para mayor comodidad durante largas" +
                " sesiones de juego.", categoria = "Accesorios", precioInt = 79990),
    Producto(id = 5, nombre = "Play Station 5",
        precio = "$549.990 clp", imageResId = R.drawable.play5,
        descripcion = "La consola de última generación de Sony, que ofrece gráficos" +
                " impresionantes y tiempos de carga ultrarrápidos para una experiencia" +
                " de juego inmersiva.", categoria = "Consolas", precioInt = 549990),
    Producto(id = 6, nombre = "PC ASUS ROG Strix",
        precio = "$1.299.990 clp", imageResId = R.drawable.pcasus,
        descripcion = "Un potente equipo diseñado para los gamers más exigentes, equipado" +
                " con los últimos componentes para ofrecer un rendimiento" +
                " excepcional en cualquier juego.", categoria = "PC Gamer", precioInt = 1299990),
    Producto(id = 7, nombre = "Silla Gamer Secretlab",
        precio = "$349.990 clp", imageResId = R.drawable.silla_gamer,
        descripcion = "Diseñada para el máximo confort, esta silla ofrece un soporte ergonómico" +
                " y personalización ajustable para sesiones de juego prolongadas."
        , categoria = "Sillas Gamers", precioInt = 349990),
    Producto(id = 8, nombre = "Mouse Logitech G502 HERO",
        precio = "$49.990 clp", imageResId = R.drawable.mouse_logitech,
        descripcion = "Con sensor de alta precisión y botones personalizables, este mouse es" +
                " ideal para gamers que buscan un control preciso y personalización."
        , categoria = "Mouse", precioInt = 49990),
    Producto(id = 9, nombre = "Polera Gamer 'Level-Up'",
        precio = "$14.990 clp", imageResId = R.drawable.polera,
        descripcion = "Una camiseta cómoda y estilizada, con la posibilidad de personalizarla" +
                " con tu gamer tag o diseño favorito.",
        categoria = "Poleras Personalizadas", precioInt = 14990)
)
