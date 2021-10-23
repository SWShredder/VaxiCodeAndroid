package cstjean.mobile.vaxicode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData

/**
 * ViewModel de la liste de preuves vaccinales
 *
 * @author Marjorie Dudemaine 2021/09/24
 */
class PreuvesVaccinalesListViewModel : ViewModel() {

    /**
     * @property preuveVaccinaleRepository Repository de preuves vaccinales
     */
    private val preuveVaccinaleRepository = PreuveVaccinaleRepository.get()

    /**
     * @property preuvesVaccinalesLiveData Liste de preuves vaccinales
     */
    val preuvesVaccinalesLiveData = preuveVaccinaleRepository.getPreuvesVaccinales().asLiveData()

    /*
    init {
        val listeStatut = listOf(
            Statut.ADEQUATEMENT_VACCINE,
            Statut.PARTIELLEMENT_VACCINE,
            Statut.PAS_VACCINE,
            Statut.ADEQUATEMENT_VACCINE,
            Statut.PARTIELLEMENT_VACCINE,
            Statut.PAS_VACCINE,
            Statut.ADEQUATEMENT_VACCINE,
            Statut.PARTIELLEMENT_VACCINE,
            Statut.PAS_VACCINE,
            Statut.ADEQUATEMENT_VACCINE,
            Statut.PARTIELLEMENT_VACCINE,
            Statut.PAS_VACCINE,
            )

        // Données de tests
        for (i in 0 until 10) {
            preuveVaccinaleRepository
                .addPreuveVaccinale(
                    PreuveVaccinale(nom = "Preuve Vaccinale #$i", statut = listeStatut[i]))
        }
    }
    */

    /**
     * Pour ajouter une preuve vaccinale
     * @param[preuveVaccinale] La nouvelle preuve
     */
    fun addPreuveVaccinale(preuveVaccinale: PreuveVaccinale) {
        preuveVaccinaleRepository.addPreuveVaccinale(preuveVaccinale)
    }
}