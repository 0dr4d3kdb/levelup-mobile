package com.example.level_upmovil.remote
 import com.example.level_upmovil.model.Producto
 import com.example.level_upmovil.model.Usuario
 import retrofit2.Response
 import retrofit2.http.GET
 import retrofit2.http.POST
 import retrofit2.http.Path
 import retrofit2.http.Body

interface ApiServiceUsuario {
 @POST("api/usuarios/{id}")
 suspend fun saveUsuario(
  @Body usuario: Usuario
 ): Response<Usuario>
 @GET("api/usuarios")
 suspend fun getAllUsuarios(): Response<List<Usuario>>
 @GET("api/usuarios/{id}")
 suspend fun getUsuarioById(@Path("id") id: Long): Response<Usuario>
 @GET("/api/productos")
 suspend fun getAllProductos(): Response<List<Producto>>
}