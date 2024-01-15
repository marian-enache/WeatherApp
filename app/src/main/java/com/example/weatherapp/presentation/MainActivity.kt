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
import androidx.activity.viewModels
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import com.example.weatherapp.presentation.model.UserCoordinates
import com.example.weatherapp.presentation.ui.WeatherAppTheme
import com.example.weatherapp.presentation.ui.compose.CurrentWeatherScreen
import com.example.weatherapp.presentation.ui.compose.Drawer
import com.example.weatherapp.presentation.ui.compose.LocationCoordinatesDependentScreen
import com.example.weatherapp.presentation.viewmodels.CurrentWeatherViewModel
import com.example.weatherapp.utils.LocationPermissionWrapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val locationPermissionWrapper: LocationPermissionWrapper = LocationPermissionWrapper(this)
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    @Inject
    lateinit var uiStateHolder: LocationCoordinatesUIStateHolder

    private val viewModel: CurrentWeatherViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
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
                    content = {
                        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                        val scope = rememberCoroutineScope()

                        Drawer(drawerState = drawerState) {
                            CurrentWeatherScreen(coordinates = it) {
                                viewModel.onDrawerOpen()
                                scope.launch { drawerState.open() }
                            }
                        }
                    }
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
                    viewModel.onCoordinatesReceived(UserCoordinates(latitude, longitude))
                }
            }
    }
}