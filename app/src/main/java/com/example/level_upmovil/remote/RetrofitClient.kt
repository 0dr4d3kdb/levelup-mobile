package com.example.level_upmovil.remote
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitClient {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8181/")
            .addConverterFactory(GsonConverterFactory.create()) // Conversor de JSON (GSON)
            .build()
    }
    val apiServiceUsuario: ApiServiceUsuario by lazy {
        retrofit.create(ApiServiceUsuario::class.java)
    }
}