package cstjean.mobile.vaxicode.database

import androidx.room.*
import cstjean.mobile.vaxicode.PreuveVaccinale
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * Interface qui accède aux données de preuves vaccinales dans la base de données
 *
 * @author Marjorie Dudemaine 2021/09/23
 * @author Yanik Sweeney 2021/09/19
 */
@Dao
interface PreuveVaccinaleDao {
    /**
     * Pour récupérer toutes les preuves vaccinales
     * @return Liste de preuves vaccinales
     */
    @Query("SELECT * FROM preuvevaccinale")
    fun getPreuvesVaccinales(): Flow<List<PreuveVaccinale>>

    /**
     * Pour récupérer une preuve vaccinale spécifique
     * @param[id] Id de la preuve voulue
     * @return La preuve voulue si elle existe
     */
    @Query("SELECT * FROM preuvevaccinale WHERE id=(:id)")
    fun getPreuveVaccinale(id: UUID): Flow<PreuveVaccinale?>

    /**
     * Pour ajouter une preuve vaccinale
     * @param[preuveVaccinale] Nouvelle preuve vaccinale
     */
    @Insert
    fun addPreuveVaccinale(preuveVaccinale: PreuveVaccinale)

    /**
     * Pour modifier une preuve vaccinale
     * @param[preuveVaccinale] Preuve à modifier
     */
    @Update
    fun updatePreuveVaccinale(preuveVaccinale: PreuveVaccinale)

    /**
     * Pour supprimer une preuve vaccinale
     * @param[preuveVaccinale] Preuve à supprimer
     */
    @Delete
    fun deletePreuveVaccinale(preuveVaccinale: PreuveVaccinale)
}