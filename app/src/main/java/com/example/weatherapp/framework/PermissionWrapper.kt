package com.example.weatherapp.framework

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import com.example.weatherapp.WeatherApp
import com.example.weatherapp.presentation.BaseActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

enum class PermissionResult {
    GRANTED, DENIED, PERMANENTLY_DENIED
}

/**
 * @see PermissionWrapper
 */
//@ActivityScoped
class LocationPermissionWrapper @Inject constructor(
    @ApplicationContext private val context: Context,
) : PermissionWrapper(
    context,
    Manifest.permission.ACCESS_FINE_LOCATION
)

sealed class PermissionWrapper(
    private val context: Context,
    private val permission: String,
) {

    fun hasPermission(): Boolean =
        PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED

    suspend fun requestPermission(): PermissionResult = suspendCancellableCoroutine { continuation ->
        setOnPermissionLauncherAction(continuation)
        getRequestPermissionLauncher().launch(permission)
    }

    private fun permissionPermanentlyDenied(): Boolean =
        !ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)

    private fun getRequestPermissionLauncher() =
        getActivity().getRequestPermissionLauncher()

    private fun getActivity() =
        (context as WeatherApp).getCurrentActivity() as BaseActivity

    private fun setOnPermissionLauncherAction(continuation: CancellableContinuation<PermissionResult>) {
        getActivity().setOnPermissionLauncherAction { isGranted: Boolean ->
            if (continuation.isActive) {
                continuation.resumeWith(
                    Result.success(
                        when {
                            isGranted -> PermissionResult.GRANTED
                            permissionPermanentlyDenied() -> PermissionResult.PERMANENTLY_DENIED
                            else -> PermissionResult.DENIED
                        }
                    )
                )
            }
        }
    }
}