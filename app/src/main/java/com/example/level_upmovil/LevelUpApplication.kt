/*package com.example.level_upmovil

import android.app.Application
import com.example.level_upmovil.di.AppContainer

/**
 * Clase de aplicación personalizada.
 * Se utiliza para inicializar el contenedor de dependencias (AppContainer)
 * y hacerlo accesible a través de toda la aplicación.
 */
class LevelUpApplication : Application() {

    // Inicializa el contenedor de dependencias simple.
    // 'lateinit' asegura que se inicialice en onCreate().
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
*/