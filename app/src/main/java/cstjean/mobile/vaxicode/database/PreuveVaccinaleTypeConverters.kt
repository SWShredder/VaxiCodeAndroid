package cstjean.mobile.vaxicode.database

import androidx.room.TypeConverter
import cstjean.mobile.vaxicode.Statut
import java.util.*

/**
 * Convertisseur de types pour les données de preuves vaccinales
 *
 * @author Marjorie Dudemaine 2021/09/16
 */
class PreuveVaccinaleTypeConverters {
    /**
     * Pour passer d'un UUID à une string
     * @return UUID en string
     */
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    /**
     * Pour passer d'une string à un UUID
     * @return UUID
     */
    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    /**
     * Pour passer d'un Statut à une string
     * @return Statut en string
     */
    @TypeConverter
    fun fromStatut(statut: Statut?): String? {
        return statut?.toString()
    }

    /**
     * Pour passer d'une string à un Statut
     * @return Statut
     */
    @TypeConverter
    fun toStatut(statut: String?): Statut? {
        return statut?.let { Statut.valueOf(it) }
    }
}