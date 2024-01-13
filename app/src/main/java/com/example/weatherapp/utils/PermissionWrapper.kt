package com.example.weatherapp.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat

/**
 * @see PermissionWrapper
 */
class LocationPermissionWrapper(activity: ComponentActivity) :
    PermissionWrapper(activity, Manifest.permission.ACCESS_FINE_LOCATION)

/**
 * When using a permission wrapper don't forget to call the [onRequestPermissionsResult] methods
 */
sealed class PermissionWrapper(
    private val activity: ComponentActivity,
    private val permission: String,
) {

    private var onPermissionGranted: (() -> Unit)? = null
    private var onPermissionDenied: (() -> Unit)? = null
    private var onPermissionPermanentlyDenied: (() -> Unit)? = null

    private val requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            when {
                isGranted -> {
                    onPermissionGranted?.invoke()
                }
                permissionPermanentlyDenied() -> {
                    onPermissionPermanentlyDenied?.invoke()
                }
                else -> {
                    onPermissionDenied?.invoke()
                }
            }
        }

    fun requestPermission(onPermissionGranted: () -> Unit,
                          onPermissionDenied: () -> Unit,
                          onPermissionPermanentlyDenied: () -> Unit) {
        this.onPermissionGranted = onPermissionGranted
        this.onPermissionDenied = onPermissionDenied
        this.onPermissionPermanentlyDenied = onPermissionPermanentlyDenied
        requestPermissionLauncher.launch(permission)
    }

    private fun hasPermission(): Boolean =
        ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED

    private fun permissionPermanentlyDenied(): Boolean =
        !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
}