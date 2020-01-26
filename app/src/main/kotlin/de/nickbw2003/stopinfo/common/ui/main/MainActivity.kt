package de.nickbw2003.stopinfo.common.ui.main

import android.Manifest
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import de.nickbw2003.stopinfo.R
import de.nickbw2003.stopinfo.common.components.PermissionsComponent
import de.nickbw2003.stopinfo.common.data.models.Error
import de.nickbw2003.stopinfo.common.data.models.Info
import de.nickbw2003.stopinfo.common.ui.ViewModelFactory
import de.nickbw2003.stopinfo.common.ui.messages.MessageHandler
import de.nickbw2003.stopinfo.common.ui.navigation.NavigationHandler
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(R.layout.activity_main), PermissionsComponent.PermissionHost, MessageHandler, NavigationHandler {
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(MainViewModel::class.java)
    }

    private val navController: NavController by lazy { findNavController(R.id.nav_host) }

    private val actionBarHeight: Int?
        get() {
            val typedValue = TypedValue()

            return if (theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
                TypedValue.complexToDimensionPixelSize(typedValue.data,resources.displayMetrics)
            } else {
                null
            }
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

        lifecycle.addObserver(permissionsComponent)

        bottom_nav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            viewModel.onScreenChanged(destination.id)
            snackBar?.dismiss()
        }

        viewModel.navigationVisible.observe(this, Observer { visible ->
            val navHostLayoutParams = nav_host.view?.layoutParams as? CoordinatorLayout.LayoutParams

            if (navHostLayoutParams != null) {
                navHostLayoutParams.bottomMargin = if (visible) actionBarHeight ?: 0 else 0
                nav_host.view?.layoutParams = navHostLayoutParams
            }

            val bottomNavLayoutParams = bottom_nav.layoutParams as? CoordinatorLayout.LayoutParams

            if (bottomNavLayoutParams != null) {
                bottomNavLayoutParams.height = if (visible) ViewGroup.LayoutParams.WRAP_CONTENT else 0
                bottom_nav.layoutParams = bottomNavLayoutParams
            }
        })

        viewModel.navigationAction.observe(this, Observer { navigate(it) })

        viewModel.onViewCreated(savedInstanceState == null)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsComponent.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun navigate(action: Int, args: Bundle?) {
        navController.navigate(action, args)
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