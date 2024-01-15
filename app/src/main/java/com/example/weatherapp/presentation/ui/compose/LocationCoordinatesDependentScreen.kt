package com.example.weatherapp.presentation.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.weatherapp.presentation.model.UserCoordinates
import com.example.weatherapp.presentation.viewmodels.CurrentWeatherViewModel


@Composable
fun LocationCoordinatesDependentScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    viewModel: CurrentWeatherViewModel = hiltViewModel(),
    onStart: () -> Unit = {},
    onLocationSettingsConfirmed: () -> Unit,
    onLocationDismissed: () -> Unit,
    content: @Composable (UserCoordinates) -> Unit
) {
    val locationEnabled = viewModel.locationEnabled
    if (!locationEnabled.value) {
        LocationSettingsDialog(onLocationSettingsConfirmed, onLocationDismissed)
    } else {
        val locationPermanentlyDenied = viewModel.locationPermissionPermanentlyDenied
        if (locationPermanentlyDenied.value) {
            LocationPermanentlyDeniedDialog { onLocationDismissed() }
        } else {
            val coordinates = viewModel.deviceLocationCoordinates.value
            if (coordinates != null) {
                content(coordinates)
            }
        }
    }

    val currentOnStart by rememberUpdatedState(onStart)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                currentOnStart()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}