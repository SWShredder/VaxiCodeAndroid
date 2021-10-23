package cstjean.mobile.vaxicode

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import android.graphics.Bitmap
import androidx.core.content.ContextCompat

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

import com.google.zxing.WriterException
import java.lang.IllegalArgumentException


/**
 * Fragment qui affiche le détail d'une preuve vaccinale
 *
 * @author Marjorie Dudemaine 2021/09/24
 * @author Yanik Sweeney 2021/09/23
 */
class PreuveVaccinaleFragment : Fragment() {
    /**
     * @property preuveVaccinaleDetailViewModel [PreuveVaccinaleDetailViewModel]
     */
    private val preuveVaccinaleDetailViewModel: PreuveVaccinaleDetailViewModel by activityViewModels()

    /**
     * @property preuveVaccinale [PreuveVaccinale]
     */
    private lateinit var preuveVaccinale: PreuveVaccinale

    /**
     * @property txtNom TextView du nom du détenteur
     */
    private lateinit var txtNom: TextView

    /**
     * @property checkDose1 CheckBox pour la première dose
     */
    private lateinit var checkDose1: CheckBox

    /**
     * @property checkDose2 CheckBox pour la deuxième dose
     */
    private lateinit var checkDose2: CheckBox

    /**
     * @property btnSupprimer Bouton pour supprimer la preuve
     */
    private lateinit var btnSupprimer: Button

    /**
     * @property imgCodeQr ImageView pour contenir le code QR
     */
    private lateinit var imgCodeQr: ImageView

    /**
     * Initialisation du Fragment.
     * @param savedInstanceState Les données conservées au changement d'état.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preuveVaccinale = PreuveVaccinale()
    }

    /**
     * Instanciation de l'interface.
     * @param inflater Pour instancier l'interface.
     * @param container Le parent qui contiendra notre interface.
     * @param savedInstanceState Les données conservées au changement d'état.
     *
     * @return La vue instanciée.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_preuve_vaccinale, container, false)

        txtNom = view.findViewById(R.id.txtNomPreuve)
        checkDose1 = view.findViewById(R.id.checkDose1)
        checkDose2 = view.findViewById(R.id.checkDose2)
        btnSupprimer = view.findViewById(R.id.btnSupprimer)
        imgCodeQr = view.findViewById(R.id.code_qr)

        return view
    }

    /**
     * Lorsque l'interface est instanciée
     * @param view La vue créée
     * @param savedInstanceState Les données conservées au changement d'état.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preuveVaccinaleDetailViewModel.preuveLiveData.observe(
            viewLifecycleOwner,
            {
                preuveVaccinale ->
                    preuveVaccinale?.let {
                        this.preuveVaccinale = preuveVaccinale
                        updateUI()
                    }
            }
        )
    }

    /**
     * Actualise l'interface en fonction des données
     */
    private fun updateUI() {
        txtNom.text = preuveVaccinale.nom

        when (preuveVaccinale.statut) {
            Statut.ADEQUATEMENT_VACCINE -> {
                checkDose1.apply {
                    isChecked = true
                    isEnabled = false
                }
                checkDose2.apply {
                    isChecked = true
                    isEnabled = true
                }
            }
            Statut.PARTIELLEMENT_VACCINE -> {
                checkDose1.apply {
                    isChecked = true
                    isEnabled = true
                }
                checkDose2.apply {
                    isChecked = false
                    isEnabled = true
                }
            }
            Statut.PAS_VACCINE -> {
                checkDose1.apply {
                    isChecked = false
                    isEnabled = true
                }
                checkDose2.apply {
                    isChecked = false
                    isEnabled = false
                }
            }
        }

        try {
            val width = imgCodeQr.layoutParams.width
            val bitmap = encodeAsBitmap(getStringCodeQr(), width, width)
            imgCodeQr.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    /**
     * Démarrage du Fragment.
     */
    override fun onStart() {
        super.onStart()

        checkDose1.setOnClickListener {
            if (checkDose1.isChecked) {
                preuveVaccinale.statut = Statut.PARTIELLEMENT_VACCINE
            } else {
                preuveVaccinale.statut = Statut.PAS_VACCINE
            }
            preuveVaccinaleDetailViewModel.savePreuve(preuveVaccinale)
        }

        checkDose2.setOnClickListener {
            if (checkDose2.isChecked) {
                preuveVaccinale.statut = Statut.ADEQUATEMENT_VACCINE
            } else {
                preuveVaccinale.statut = Statut.PARTIELLEMENT_VACCINE
            }
            preuveVaccinaleDetailViewModel.savePreuve(preuveVaccinale)
        }

        btnSupprimer.setOnClickListener {
            preuveVaccinaleDetailViewModel.deletePreuve(preuveVaccinale)
            setFragmentResult(DELETE_RESULT, bundleOf())
        }
    }

    /**
     * Méthode pour dessiner un code QR
     * @param str La string qui sera encodée dans le code QR
     * @param width La largeur du code QR désirée
     * @param height La hauteur du code QR désiré
     * @return Une image bitmap du code QR
     * @throws WriterException Lance une exception si l'écriture du code QR ne fonctionne pas
     */
    @Throws(WriterException::class)
    fun encodeAsBitmap(str: String?, width: Int, height: Int): Bitmap? {
        val black = ContextCompat.getColor(requireContext(), R.color.black)
        val white = ContextCompat.getColor(requireContext(), R.color.white)
        val result: BitMatrix = try {
            MultiFormatWriter().encode(
                str,
                BarcodeFormat.QR_CODE, width, height, null
            )
        } catch (iae: IllegalArgumentException) {
            // Unsupported format
            return null
        }
        Log.d("DEBUG", width.toString())
        val w = result.width
        val h = result.height
        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            val offset = y * w
            for (x in 0 until w) {
                pixels[offset + x] = if (result[x, y]) black else white
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
        return bitmap
    }

    /**
     * Pour récupérer la string associée au code QR
     */
    private fun getStringCodeQr() : String {
        val nom = preuveVaccinale.nom
        val dose1 : String
        val dose2 : String
        when(preuveVaccinale.statut) {
            Statut.PAS_VACCINE -> {
                dose1 = "-"
                dose2 = "-"
            }
            Statut.PARTIELLEMENT_VACCINE -> {
                dose1 = "+"
                dose2 = "-"
            }
            Statut.ADEQUATEMENT_VACCINE -> {
                dose1 = "+"
                dose2 = "+"
            }
        }
        return "$nom $dose1" + "DOSE1 $dose2" + "DOSE2"
    }

    companion object {
        const val DELETE_RESULT = "travailfragmentdeleteresult"
    }
}