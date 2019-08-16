package de.nickbw2003.stopinfo.common.util.extensions

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import de.nickbw2003.stopinfo.R

private const val undefinedPermissionGroupName = "android.permission-group.UNDEFINED"

fun Activity.checkPermissions(permissions: Array<String>, requestCode: Int): Pair<Boolean, Array<String>> {
    val requestPermissions: (Array<String>, Int) -> Unit = { a, b -> ActivityCompat.requestPermissions(this, a, b) }
    return checkPermissions(permissions, requestCode, requestPermissions)
}

fun Fragment.checkPermissions(permissions: Array<String>, requestCode: Int): Pair<Boolean, Array<String>> {
    return activity?.checkPermissions(permissions, requestCode, ::requestPermissions) ?: false to permissions
}

private fun Activity.checkPermissions(
    permissions: Array<String>,
    requestCode: Int,
    requestPermissions: (Array<String>, Int) -> Unit
): Pair<Boolean, Array<String>> {
    val checkPermission = PermissionChecker::checkSelfPermission
    val shouldRequestRationale = ActivityCompat::shouldShowRequestPermissionRationale

    val toRequest = permissions
        .filter { p -> checkPermission(this, p) != PermissionChecker.PERMISSION_GRANTED }
        .map { p -> p to shouldRequestRationale(this, p) }

    if (toRequest.isNotEmpty()) {
        val rationalePermissionsState = toRequest.filter { p -> p.second }

        if (rationalePermissionsState.isNotEmpty()) {
            val rationalePermissions = rationalePermissionsState.map { it.first }.toTypedArray()
            val descriptionPrefix = getString(R.string.permission_group_info_prefix)
            val descriptions = getReadablePermissionGroupInfo(rationalePermissions)
                .map { "$descriptionPrefix$it" }

            val message = "${getString(R.string.permission_request_dialog_text)}\n" +
                    descriptions.joinToString("\n")

            AlertDialog.Builder(this)
                .setTitle(R.string.permission_request_dialog_title)
                .setMessage(message)
                .setPositiveButton(R.string.permission_request_dialog_positive_option_title) { _, _ ->
                    requestPermissions(toRequest.map { p -> p.first }.toTypedArray(), requestCode)
                }
                .setNegativeButton(R.string.permission_request_dialog_negative_option_title) { _, _ -> }
                .create()
                .show()

            return false to toRequest.map { it.first }.toTypedArray()
        }

        requestPermissions(toRequest.map { p -> p.first }.toTypedArray(), requestCode)
        return false to toRequest.map { it.first }.toTypedArray()
    }

    return true to emptyArray()
}

fun Fragment.getReadablePermissionGroupInfo(permissions: Array<String>): Array<String> {
    val context = context ?: throw IllegalStateException("context must not be null!")
    return context.getReadablePermissionGroupInfo(permissions)
}

fun Context.getReadablePermissionGroupInfo(permissions: Array<String>): Array<String> {
    return permissions.mapNotNull { p -> getPermissionGroupDescription(p) }
        .groupBy { it }
        .keys
        .toTypedArray()
}

private fun Context.getPermissionGroupDescription(permission: String): String? {
    val permissionInfo = packageManager.getPermissionInfo(permission, 0)
    val permissionGroupInfo = permissionInfo.group?.let { packageManager.getPermissionGroupInfo(it, 0) }

    return if (arrayOf(null, undefinedPermissionGroupName).contains(permissionGroupInfo?.name)) {
        "${permissionInfo?.loadDescription(packageManager)}"
    } else {
        "${permissionGroupInfo?.loadLabel(packageManager)}: ${permissionGroupInfo?.loadDescription(packageManager)}"
    }
}