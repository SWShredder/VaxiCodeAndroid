package cstjean.mobile.vaxicode

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.javafaker.Faker

/**
 * Le fragment qui affiche la liste de preuves vaccinales
 *
 * @author Yanik Sweeney 2021/09/19
 * @author Marjorie Dudemaine 2021/09/24
 */
class PreuvesVaccinalesListFragment : Fragment() {
    /**
     * @property preuvesVaccinalesListViewModel [PreuvesVaccinalesListViewModel]
     */
    private val preuvesVaccinalesListViewModel : PreuvesVaccinalesListViewModel by viewModels()

    /**
     * @property preuveVaccinaleDetailViewModel [PreuveVaccinaleDetailViewModel]
     */
    private val preuveVaccinaleDetailViewModel: PreuveVaccinaleDetailViewModel by activityViewModels()

    /**
     * @property preuvesVaccinalesRecyclerView RecyclerView de la liste de preuves vaccinales
     */
    private lateinit var preuvesVaccinalesRecyclerView : RecyclerView

    /**
     * @property adapter [PreuvesVaccinalesListFragment.PreuveVaccinaleAdapter]
     */
    private var adapter: PreuveVaccinaleAdapter? = PreuveVaccinaleAdapter(emptyList())

    /**
     * Méthode appelée à la création de la vue
     * @param savedInstanceState Le bundle pour partager de l'information entre les changements d'état
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /**
     * Méthode appelée à la création du menu
     * @param menu Le fragment menu
     * @param inflater Le inflater de fragment
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_preuves_vaccinales_list, menu)
    }

    /**
     * Lorsqu'on sélectionne un item dans les options
     * @param[item] Item sélectionné
     * @return Si on effectue l'action de l'item
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nouvelle_preuve_vaccinale -> {
                val faker = Faker()
                val prenom = faker.lordOfTheRings().character()
                val nom = faker.name().lastName()
                val preuveVaccinale = PreuveVaccinale(nom = "$prenom $nom", statut = Statut.PAS_VACCINE)
                preuvesVaccinalesListViewModel.addPreuveVaccinale(preuveVaccinale)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /**
     * Méthode appelée lorsque la vue est créée. Est utilisée pour assigner les valeurs aux
     * variables membres
     * @param inflater Une instance de LayoutInflater
     * @param container Le ViewGroup sert de container
     * @param savedInstanceState Un instance de Bundle
     * @return La vue instanciée
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_preuves_vaccinales_list, container, false)

        preuvesVaccinalesRecyclerView = view.findViewById(R.id.preuves_vaccinales_recycler_view)
        preuvesVaccinalesRecyclerView.layoutManager = LinearLayoutManager(context)
        preuvesVaccinalesRecyclerView.adapter = adapter

        return view
    }

    /**
     * Appelé après la création de vue. Est utilisée pour actualiser l'interface
     * @param view La view qui a été créée
     * @param savedInstanceState Une instance de bundle pour partager des données
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preuvesVaccinalesListViewModel.preuvesVaccinalesLiveData.observe(
            viewLifecycleOwner,
            { preuvesVaccinales ->
                preuvesVaccinales?.let {
                    updateUI(preuvesVaccinales)
                }
            }
        )
    }

    /**
     * Actualise l'interface en fonction des données
     */
    private fun updateUI(preuvesVaccinales: List<PreuveVaccinale>) {
        adapter = PreuveVaccinaleAdapter(preuvesVaccinales)
        preuvesVaccinalesRecyclerView.adapter = adapter
    }

    /**
     * Le ViewHolder pour PreuveVaccinale
     * @property preuveVaccinale Une instance de PreuveVaccinale
     * @property nomTextView Le TextView pour le nom associé à la preuve vaccinale
     * @property statutTextView Le TextView pour le statut de la preuve vaccinale
     * @property iconeStatutImageView Le ImageView associé à la preuve vaccinale
     * @property containerStatut Le ContraintLayout associé au statut de la preuve vaccinale
     */
    private inner class PreuveVaccinaleHolder(view: View) :
            RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var preuveVaccinale: PreuveVaccinale
        val nomTextView: TextView = itemView.findViewById(R.id.preuve_vaccinale_nom)
        val statutTextView: TextView = itemView.findViewById(R.id.texte_statut_vaccinal)
        val iconeStatutImageView: ImageView = itemView.findViewById(R.id.icone_statut_vaccinal)
        val containerStatut: ConstraintLayout = itemView.findViewById(R.id.container_statut)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(preuveVaccinale: PreuveVaccinale) {
            this.preuveVaccinale = preuveVaccinale
            nomTextView.text = this.preuveVaccinale.nom

            val statutTexte: String
            val codeCouleur: Int

            when(this.preuveVaccinale.statut) {
                Statut.PAS_VACCINE -> {
                    statutTexte = getString(R.string.texte_pas_vaccine)
                    codeCouleur = ContextCompat.getColor(requireContext(), R.color.red)
                    iconeStatutImageView.setImageResource(R.drawable.ic_baseline_non_vaccine_24)
                    iconeStatutImageView.scaleX = -1.0f
                }
                Statut.ADEQUATEMENT_VACCINE -> {
                    statutTexte = getString(R.string.texte_adequatement_vaccine)
                    codeCouleur = ContextCompat.getColor(requireContext(), R.color.green)
                    iconeStatutImageView.setImageResource(R.drawable.ic_baseline_adequatement_24)
                    iconeStatutImageView.scaleX = 1.0f
                }
                Statut.PARTIELLEMENT_VACCINE -> {
                    statutTexte = getString(R.string.texte_partiellement_vaccine)
                    codeCouleur = ContextCompat.getColor(requireContext(), R.color.yellow)
                    iconeStatutImageView.setImageResource(R.drawable.ic_baseline_partiellement_24)
                    iconeStatutImageView.scaleX = 1.0f
                }
            }
            statutTextView.text = statutTexte
            containerStatut.setBackgroundColor(codeCouleur)
        }

        override fun onClick(v: View?) {
            preuveVaccinaleDetailViewModel.selectIdPreuve(preuveVaccinale.id)
        }
    }

    /**
     * Adaptateur pour le recycler view et la liste de preuves vaccinales
     * @property preuvesVaccinales La liste des preuves vaccinales
     */
    private inner class PreuveVaccinaleAdapter(var preuvesVaccinales: List<PreuveVaccinale>)
        : RecyclerView.Adapter<PreuveVaccinaleHolder>() {

        /**
         * @param parent Le parent où ajouter le ViewHolder
         * @param viewType Une valeur entière qui représente le type de view
         * @return Une instance de ViewHolder
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreuveVaccinaleHolder {
            val view = layoutInflater.inflate(R.layout.list_item_preuve_vaccinale, parent, false)
            return PreuveVaccinaleHolder(view)
        }

        /**
         * @param holder L'instance du ViewHolder qui est lié
         * @param position La position dans la liste à charger
         */
        override fun onBindViewHolder(holder: PreuveVaccinaleHolder, position: Int) {
            holder.bind(preuvesVaccinales[position])
        }

        /**
         * @return La quantité d'items dans la liste fournie
         */
        override fun getItemCount(): Int = preuvesVaccinales.size

    }

}