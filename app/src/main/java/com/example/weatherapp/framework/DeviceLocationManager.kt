package com.example.weatherapp.framework

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.core.location.LocationManagerCompat
import com.example.weatherapp.WeatherApp
import com.example.weatherapp.presentation.BaseActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class DeviceLocationManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend fun requestLocationEnabled(): Boolean = suspendCancellableCoroutine { continuation ->
        setOnLocationEnableLauncherAction(continuation)
        getRequestLocationEnableLauncher().launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    fun isLocationEnabled() =
        LocationManagerCompat.isLocationEnabled(
            getActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        )

    private fun getRequestLocationEnableLauncher() =
        getActivity().getRequestLocationEnableLauncher()

    private fun getActivity() =
        (context as WeatherApp).getCurrentActivity() as BaseActivity

    private fun setOnLocationEnableLauncherAction(continuation: CancellableContinuation<Boolean>) {
        getActivity().setOnLocationEnableLauncherAction {
            if (continuation.isActive) {
                continuation.resumeWith(Result.success(isLocationEnabled()))
            }
        }
    }
}