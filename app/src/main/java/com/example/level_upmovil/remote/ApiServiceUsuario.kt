package com.example.level_upmovil.remote
 import com.example.level_upmovil.model.Producto
 import com.example.level_upmovil.model.Usuario
 import retrofit2.Response
 import retrofit2.http.GET
 import retrofit2.http.POST
 import retrofit2.http.Path
 import retrofit2.http.Body

interface ApiServiceUsuario {
 @POST("api/auth/registro")
 suspend fun saveUsuario(
  @Body usuario: Usuario
 ): Response<Usuario>
 @GET("api/usuarios")
 suspend fun getAllUsuarios(): Response<List<Usuario>>
 //@GET("api/usuarios/{id}")
 //suspend fun getProductoPorId(@Path("id") productoId: Int): Producto
 @GET("/api/productos")
 suspend fun getAllProductos(): Response<List<Producto>>
 @GET("api/productos/{id}")
 suspend fun getProductoPorId(@Path("id") productoId: Int): Producto
}