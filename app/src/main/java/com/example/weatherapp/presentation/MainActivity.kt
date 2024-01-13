package com.example.weatherapp.presentation

import android.annotation.SuppressLint
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
import com.example.weatherapp.presentation.model.UserCoordinates
import com.example.weatherapp.presentation.ui.WeatherAppTheme
import com.example.weatherapp.presentation.ui.compose.CurrentWeatherScreen
import com.example.weatherapp.presentation.ui.compose.LocationCoordinatesDependentScreen
import com.example.weatherapp.utils.LocationPermissionWrapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val locationPermissionWrapper: LocationPermissionWrapper = LocationPermissionWrapper(this)
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    @Inject
    lateinit var uiStateHolder: LocationCoordinatesUIStateHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                LocationCoordinatesDependentScreen(this,
                    uiStateHolder = uiStateHolder,
                    onStart = {
                        val locationEnabled = isLocationEnabled()
                        uiStateHolder.locationEnabled.value = locationEnabled
                        if (locationEnabled) {
                            requestLocationPermission()
                        }
                    },
                    onLocationSettingsConfirmed = {
                        val viewIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(viewIntent)
                    },
                    onLocationDismissed = { finish() },
                    content = { CurrentWeatherScreen(coordinates = it) }
                )
            }
        }
    }

    private fun requestLocationPermission() {
        locationPermissionWrapper.requestPermission(
            onPermissionGranted = {
                getCoordinates()
                uiStateHolder.locationPermissionPermanentlyDenied.value = false
            },
            onPermissionDenied = {
                finish()
            },
            onPermissionPermanentlyDenied = {
                uiStateHolder.locationPermissionPermanentlyDenied.value = true
            })
    }

    private fun isLocationEnabled() =
        isLocationEnabled(getSystemService(Context.LOCATION_SERVICE) as LocationManager)

    @SuppressLint("MissingPermission")
    private fun getCoordinates() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                location?.run {
                    uiStateHolder.locationCoordinates.value = UserCoordinates(latitude, longitude)
                }
            }
    }
}