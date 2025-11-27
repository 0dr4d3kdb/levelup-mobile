package com.example.level_upmovil.remote
 import com.example.level_upmovil.model.Producto
 import com.example.level_upmovil.model.RegistroResponse
 import com.example.level_upmovil.model.Usuario
 import retrofit2.Response
 import retrofit2.http.GET
 import retrofit2.http.POST
 import retrofit2.http.Path
 import retrofit2.http.Body
 import retrofit2.http.Query
interface ApiServiceUsuario {
 @POST("/api/auth/registro") // 🛑 Usa la ruta del AuthController
 suspend fun registerUsuario(@Body usuario: Usuario): Response<RegistroResponse>
 @POST("api/auth/login")
 suspend fun loginUsuario(@Body usuario: Usuario): Response<String>
 //@GET("api/usuarios/{id}")
 //suspend fun getProductoPorId(@Path("id") productoId: Int): Producto
 @GET("/api/productos")
 suspend fun getAllProductos(): Response<List<Producto>>
 @GET("api/productos/{id}")
 suspend fun getProductoPorId(@Path("id") productoId: Int): Producto
}
