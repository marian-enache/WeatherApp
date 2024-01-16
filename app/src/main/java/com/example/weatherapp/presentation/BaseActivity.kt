package com.example.weatherapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.weatherapp.WeatherApp

open class BaseActivity : ComponentActivity() {
    private var weatherApp: WeatherApp? = null

    private var onPermissionLauncherAction: (Boolean) -> Unit = {}
    private var requestPermissionLauncher: ActivityResultLauncher<String> = getNewLocationPermissionLauncher()

    private var onLocationEnableLauncherAction: (ActivityResult) -> Unit = {}
    private var requestLocationEnableLauncher: ActivityResultLauncher<Intent> = getNewLocationEnableLauncher()

    public override fun onCreate(savedInstanceState: Bundle?) {
        requestLocationEnableLauncher = getNewLocationEnableLauncher()
        requestPermissionLauncher = getNewLocationPermissionLauncher()

        super.onCreate(savedInstanceState)
        weatherApp = this.applicationContext as WeatherApp
    }

    override fun onStart() {
        super.onStart()
        weatherApp?.setCurrentActivity(this)
    }

    override fun onResume() {
        super.onResume()
        weatherApp?.setCurrentActivity(this)
    }

    override fun onDestroy() {
        requestPermissionLauncher.unregister()
        requestLocationEnableLauncher.unregister()
        clearReferences()
        super.onDestroy()
    }

    private fun clearReferences() {
        weatherApp?.checkAndClearCurrentActivity(this)
    }

    fun setOnPermissionLauncherAction(action: (Boolean) -> Unit) {
        onPermissionLauncherAction = action
    }

    fun getRequestPermissionLauncher() = requestPermissionLauncher

    fun setOnLocationEnableLauncherAction(action: (ActivityResult) -> Unit) {
        onLocationEnableLauncherAction = action
    }

    fun getRequestLocationEnableLauncher() = requestLocationEnableLauncher

    private fun getNewLocationEnableLauncher() = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        onLocationEnableLauncherAction(it)
    }

    private fun getNewLocationPermissionLauncher() = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        onPermissionLauncherAction(it)
    }
}