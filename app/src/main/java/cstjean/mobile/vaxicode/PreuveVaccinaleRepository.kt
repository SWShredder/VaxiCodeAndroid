package cstjean.mobile.vaxicode

import android.content.Context
import androidx.room.Room
import cstjean.mobile.vaxicode.database.PreuveVaccinaleDatabase
import kotlinx.coroutines.flow.Flow
import java.util.*
import java.util.concurrent.Executors


/**
 * Nom de la base de données
 */
private const val DATABASE_NAME = "preuveVaccinale-database"

/**
 * API pour les données de preuves vaccinales
 * @property Context Contexte de l'application
 *
 * @author Marjorie Dudemaine 2021/09/23
 */
class PreuveVaccinaleRepository private constructor(context: Context) {
    /**
     * @property database Base de données
     */
    private val database: PreuveVaccinaleDatabase = Room.databaseBuilder(
        context.applicationContext,
        PreuveVaccinaleDatabase::class.java,
        DATABASE_NAME
    ).build()

    /**
     * @property preuveVaccinaleDao Interface pour accéder aux données
     */
    private val preuveVaccinaleDao = database.preuveVaccinaleDao()

    /**
     * @property executor Exécuteur pour effectuer les appels à la base de données
     */
    private val executor = Executors.newSingleThreadExecutor()

    /**
     * Pour récupérer la liste de preuves vaccinales
     * @return Liste de preuves vaccinales
     */
    fun getPreuvesVaccinales(): Flow<List<PreuveVaccinale>> = preuveVaccinaleDao.getPreuvesVaccinales()

    /**
     * Pour récupérer une preuve vaccinale spécifique
     * @param[id] Id de la preuve voulue
     * @return La preuve voulue si elle existe
     */
    fun getPreuveVaccinale(id: UUID): Flow<PreuveVaccinale?> = preuveVaccinaleDao.getPreuveVaccinale(id)

    /**
     * Ajoute une données dans la base de données
     * @param[preuveVaccinale] La preuve vaccinale à ajouter
     */
    fun addPreuveVaccinale(preuveVaccinale: PreuveVaccinale) {
        executor.execute {
            preuveVaccinaleDao.addPreuveVaccinale(preuveVaccinale)
        }
    }

    /**
     * Met à jour une donnée dans la base de données
     * @param[preuveVaccinale] La preuve vaccinale à modifier
     */
    fun updatePreuveVaccinale(preuveVaccinale: PreuveVaccinale) {
        executor.execute {
            preuveVaccinaleDao.updatePreuveVaccinale(preuveVaccinale)
        }
    }

    /**
     * Supprime une donnée dans la base de données
     * @param[preuveVaccinale] Preuve à supprimer
     */
    fun deletePreuveVaccinale(preuveVaccinale: PreuveVaccinale) {
        executor.execute {
            preuveVaccinaleDao.deletePreuveVaccinale(preuveVaccinale)
        }
    }

    companion object {
        /**
         * @property INSTANCE Instance du repository
         */
        private var INSTANCE: PreuveVaccinaleRepository? = null

        /**
         * Pour initialiser l'instance du repository
         * @param[context] Contexte de l'application
         */
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = PreuveVaccinaleRepository(context)
            }
        }

        /**
         * Pour récupérer l'instance du repository
         * @return L'instance du repository
         * @throws IllegalStateException Si le repository n'a pas été initialisé
         */
        fun get(): PreuveVaccinaleRepository {
            return INSTANCE ?: throw IllegalStateException("TravailRepository must be initialized.")
        }
    }
}