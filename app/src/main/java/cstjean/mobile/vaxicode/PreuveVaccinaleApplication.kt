package cstjean.mobile.vaxicode

import android.app.Application

/**
 * Classe représentant l'état général de l'application
 *
 * @author Marjorie Dudemaine 2021/09/16
 */
class PreuveVaccinaleApplication : Application() {
    /**
     * Lors de la création de l'application
     */
    override fun onCreate() {
        super.onCreate()
        PreuveVaccinaleRepository.initialize(this)
    }
}