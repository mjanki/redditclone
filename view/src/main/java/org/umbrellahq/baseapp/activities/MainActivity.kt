package org.umbrellahq.baseapp.activities

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.umbrellahq.baseapp.R
import org.umbrellahq.baseapp.models.ErrorNetworkViewEntity
import org.umbrellahq.util.setupToolbar

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar(toolbar, showUp = false, title = getString(R.string.app_name))

        // Setup No-Up-Button Fragments
        val appBarConfiguration = AppBarConfiguration.Builder(setOf(R.id.mainFragment)).build()
        NavigationUI.setupActionBarWithNavController(
                this, findNavController(R.id.fNavHost), appBarConfiguration
        )
    }

    override fun handleErrorNetwork(errorNetworkViewEntity: ErrorNetworkViewEntity) {
        Snackbar.make(
                clContainer,
                errorNetworkViewEntity.message,
                Snackbar.LENGTH_LONG
        ).addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                resolveErrorNetwork()
            }
        }).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fNavHost).navigateUp()
    }
}