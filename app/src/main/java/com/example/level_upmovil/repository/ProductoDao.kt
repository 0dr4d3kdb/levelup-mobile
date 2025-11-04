package com.example.level_upmovil.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.level_upmovil.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Query("SELECT * FROM producto")
    fun getAllProductos(): Flow<List<Producto>>

    @Query("SELECT * FROM producto WHERE id = :productoId")
    suspend fun getProductoById(productoId: Int): Producto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(productos: List<Producto>)
}