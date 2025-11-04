/*package com.example.level_upmovil.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.level_upmovil.model.Producto
import com.example.level_upmovil.model.listaProductosPrecarga
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class AppDatabase : RoomDatabase() {

    abstract fun productoDao() : ProductoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "level_up_database"
                )
                    .addCallback(DatabaseCallback(scope)) // Ya no arroja error
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.productoDao())
                }
            }
        }

        suspend fun populateDatabase(productoDao: ProductoDao) {
            // Inserta los productos iniciales
            productoDao.insertAll(listaProductosPrecarga)
        }
    }
}*/