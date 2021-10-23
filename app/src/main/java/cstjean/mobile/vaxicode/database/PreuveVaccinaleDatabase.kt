package cstjean.mobile.vaxicode.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cstjean.mobile.vaxicode.PreuveVaccinale

/**
 * Point d'accès principal de la base de données
 *
 * @author Marjorie Dudemaine 2021/09/16
 */
@Database(entities = [PreuveVaccinale::class], version=1, exportSchema = false)
@TypeConverters(PreuveVaccinaleTypeConverters::class)
abstract class PreuveVaccinaleDatabase : RoomDatabase() {
    abstract fun preuveVaccinaleDao(): PreuveVaccinaleDao
}