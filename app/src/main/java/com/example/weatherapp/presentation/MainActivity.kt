package com.example.weatherapp.presentation

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import com.example.weatherapp.R
import com.example.weatherapp.presentation.ui.WeatherAppTheme
import com.example.weatherapp.presentation.ui.compose.CurrentWeatherScreen
import com.example.weatherapp.utils.LocationPermissionWrapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val locationPermissionWrapper: LocationPermissionWrapper by lazy {
        LocationPermissionWrapper(this, R.string.app_name)
    }
    private var checkLocation = false

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                CurrentWeatherScreen()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        handleLocationRequirements()
    }

    override fun onResume() {
        super.onResume()

        if (checkLocation) {
            checkLocation = false
            handleLocationRequirements()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionWrapper.onRequestPermissionsResult(requestCode)
    }

    private fun handleLocationRequirements() {
        if (!isLocationEnabled(getSystemService(Context.LOCATION_SERVICE) as LocationManager)) {
            showLocationDisabledDialog()
            return
        }

        checkLocationPermission()
    }

    // Only a simplified location handling as of now
    @SuppressLint("MissingPermission")
    private fun checkLocationPermission() {
        locationPermissionWrapper.requestPermission(
            onPermissionGranted = {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        location?.run {

                        }
                    }
            },
            onPermissionDenied = { },
            onPermissionPermanentlyDenied = { })
    }

    // Will add Compose later
    private fun showLocationDisabledDialog() {
        AlertDialog.Builder(this)
            .setTitle("")
            .setMessage("We need to access your location. Please enable it from the Settings page!")
            .setCancelable(false)
            .setNegativeButton("Not now") { d, _ ->
                finish()
                d.dismiss()
            }
            .setPositiveButton("Settings") { d, _ ->
                d.dismiss()
                val viewIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(viewIntent)
                checkLocation = true
            }
            .create()
            .show()
    }
}