package cstjean.mobile.vaxicode

import androidx.lifecycle.*
import java.util.*

/**
 * ViewModel du détail d'une preuve vaccinale
 *
 * @author Marjorie Dudemaine 2021/09/23
 */
class PreuveVaccinaleDetailViewModel : ViewModel() {
    /**
     * @property preuveRepository [PreuveVaccinaleRepository]
     */
    private val preuveRepository = PreuveVaccinaleRepository.get()

    /**
     * @property preuveSelectedIdLiveData Id de la preuve vaccinale
     */
    val preuveSelectedIdLiveData = MutableLiveData<UUID>()

    /**
     * @property preuveLiveData Preuve vaccinale complète
     */
    val preuveLiveData: LiveData<PreuveVaccinale?> =
        Transformations.switchMap(preuveSelectedIdLiveData) { preuveId ->
            preuveRepository.getPreuveVaccinale(preuveId).asLiveData()
        }

    /**
     * Pour sélectionner le id de la preuve voulue
     * @param[preuveId] Id de la preuve sélectionnée
     */
    fun selectIdPreuve(preuveId: UUID) {
        preuveSelectedIdLiveData.value = preuveId
    }

    /**
     * Pour sauvegarder une preuve vaccinale
     * @param[preuveVaccinale] Preuve vaccinale à sauvegarder
     */
    fun savePreuve(preuveVaccinale: PreuveVaccinale) {
        preuveRepository.updatePreuveVaccinale(preuveVaccinale)
    }

    /**
     * Pour supprimer une preuve vaccinale
     * @param[preuveVaccinale] Preuve à supprimer
     */
    fun deletePreuve(preuveVaccinale: PreuveVaccinale) {
        preuveRepository.deletePreuveVaccinale(preuveVaccinale)
    }
}