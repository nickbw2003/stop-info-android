package de.nickbw2003.stopinfo.common.components

import android.app.Activity
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import de.nickbw2003.stopinfo.common.util.extensions.checkPermissions

class PermissionsComponent(
    private val host: PermissionHost,
    private val requiredPermissions: Array<String>,
    private val requestCode: Int,
    private val onAllPermissionsGranted: () -> Unit,
    private val onPermissionsMissing: (missingPermissions: Array<String>) -> Unit
) : LifecycleObserver {
    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        request()
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (this.requestCode == requestCode) {
            val allGranted = grantResults.all { g -> g == PackageManager.PERMISSION_GRANTED }

            if (allGranted) {
                onAllPermissionsGranted()
            } else {
                val requestResult = permissions.zip(grantResults.toTypedArray()).toMap()
                onPermissionsMissing(requestResult
                    .filter { it.value == PackageManager.PERMISSION_DENIED }
                    .map { it.key }
                    .toTypedArray())
            }
        }
    }

    private fun request() {
        val checkResult = when (host) {
            is Activity -> host.checkPermissions(requiredPermissions, requestCode)
            is Fragment -> host.checkPermissions(requiredPermissions, requestCode)
            else -> throw IllegalHostException()
        }

        if (checkResult.first) {
            onAllPermissionsGranted()
        } else {
            onPermissionsMissing(checkResult.second)
        }
    }

    interface PermissionHost
    class IllegalHostException : Exception("Illegal host - only Activities and Fragments are supported!")
}