package com.example.weatherapp.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat

/**
 * @see PermissionWrapper
 */
class LocationPermissionWrapper(activity: Activity, @StringRes private var errorStringRes: Int) :
    PermissionWrapper(activity, Manifest.permission.ACCESS_FINE_LOCATION, 1006, errorStringRes)

/**
 * When using a permission wrapper don't forget to call the [onRequestPermissionsResult] methods
 */
sealed class PermissionWrapper(
    private val activity: Activity,
    private val permission: String,
    private val permissionRequestCode: Int,
    @StringRes private var errorStringRes: Int
) {

    private var onPermissionGranted: (() -> Unit)? = null
    private var onPermissionDenied: (() -> Unit)? = null
    private var onPermissionPermanentlyDenied: (() -> Unit)? = null

    fun requestPermission(onPermissionGranted: () -> Unit,
                          onPermissionDenied: () -> Unit,
                          onPermissionPermanentlyDenied: () -> Unit) {
        this.onPermissionGranted = onPermissionGranted
        this.onPermissionDenied = onPermissionDenied
        this.onPermissionPermanentlyDenied = onPermissionPermanentlyDenied
        ActivityCompat.requestPermissions(activity, arrayOf(permission), permissionRequestCode)
    }

    fun onRequestPermissionsResult(requestCode: Int) {
        if (requestCode == permissionRequestCode) {
            when {
                hasPermission() -> onPermissionGranted?.invoke()
                permissionPermanentlyDenied() -> onPermissionPermanentlyDenied?.invoke()
                else -> onPermissionDenied?.invoke()
            }
        }
    }

    private fun hasPermission(): Boolean =
        ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED

    private fun permissionPermanentlyDenied(): Boolean =
        !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
}