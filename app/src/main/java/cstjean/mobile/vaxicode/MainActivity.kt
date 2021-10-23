package cstjean.mobile.vaxicode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

/**
 * Activity principale qui contient les fragments
 * @author Yanik Sweeney 2021/09/19
 * @author Marjorie Dudemaine 2021/09/21
 */
class MainActivity : AppCompatActivity() {

    /**
     * @property preuveVaccinaleDetailViewModel [PreuveVaccinaleDetailViewModel]
     */
    private val preuveVaccinaleDetailViewModel: PreuveVaccinaleDetailViewModel by viewModels()

    /**
     * Méthode appelé à la création de la vue
     * @param savedInstanceState Les donneés conservées au changement d'état
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = PreuvesVaccinalesListFragment()

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }

        preuveVaccinaleDetailViewModel.preuveSelectedIdLiveData.observe(this, {
            val fragment = PreuveVaccinaleFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        })

        supportFragmentManager.setFragmentResultListener(
            PreuveVaccinaleFragment.DELETE_RESULT, this) { _, _ ->
                supportFragmentManager.popBackStack()
            }
    }
}