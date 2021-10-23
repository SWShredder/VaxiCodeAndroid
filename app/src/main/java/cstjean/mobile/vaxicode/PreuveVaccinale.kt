package cstjean.mobile.vaxicode

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Entité pour représenter une Preuve Vaccinale
 *
 * @property id Id de la preuve
 * @property nom Nom de la personne à qui appartient la preuve
 * @property statut Statut vaccinal de la personne
 *
 * @author Marjorie Dudemaine 2021/09/16
 */
@Entity
data class PreuveVaccinale(@PrimaryKey val id: UUID = UUID.randomUUID(),
                           var nom: String = "",
                           var statut: Statut = Statut.PAS_VACCINE)