package de.nickbw2003.stopinfo.common.ui.main

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.common.ui.ViewModelFactory
import de.nickbw2003.stopinfo.common.components.PermissionsComponent
import de.nickbw2003.stopinfo.common.data.models.Error
import de.nickbw2003.stopinfo.common.data.models.Info
import de.nickbw2003.stopinfo.common.ui.messages.MessageHandler
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), PermissionsComponent.PermissionHost, MessageHandler {
    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory.getInstance(this)).get(MainViewModel::class.java)
    }

    private var snackBar: Snackbar? = null

    private val permissionsComponent: PermissionsComponent by lazy {
        PermissionsComponent(
            this,
            REQUIRED_PERMISSIONS,
            PERMISSIONS_REQUEST_CODE,
            viewModel::onAllPermissionsGranted
        ) { viewModel.onPermissionsMissing() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(permissionsComponent)

        with(findNavController(R.id.nav_host)) {
            bottom_nav.setupWithNavController(this)
            addOnDestinationChangedListener { _, _, _ -> snackBar?.dismiss() }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsComponent.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun handleError(error: Error, retryAction: (() -> Unit)?) {
        snackBar?.dismiss()
        snackBar = Snackbar.make(root_view, error.userMessage, Snackbar.LENGTH_INDEFINITE)
            .apply {
                if (retryAction != null) {
                    setAction(R.string.error_retry_label) {
                        retryAction()
                    }
                }
            }
        snackBar?.show()
    }

    override fun handleInfo(info: Info) {
        snackBar?.dismiss()

        with(Snackbar
            .make(root_view, info.message, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.activity_main_info_got_it) { viewModel.onInfoGotItClicked(info) }) {
            snackBar = this
            show()
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 0
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}