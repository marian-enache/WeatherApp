package com.example.weatherapp.presentation.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.weatherapp.data.model.DeviceLocationState
import com.example.weatherapp.data.model.DeviceLocationState.*


@Composable
fun LocationCoordinatesDependentScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    deviceLocationState: DeviceLocationState,
    onStart: () -> Unit = {},
    onGoToLocationSettingsClicked: () -> Unit,
    onLocationDismissed: () -> Unit,
    onLocationPermissionNeededOkClicked: () -> Unit,
    content: @Composable () -> Unit
) {

    when (deviceLocationState) {
        LocationDisabled -> {
            LocationSettingsDialog(onGoToLocationSettingsClicked, onLocationDismissed)
        }
        LocationPermissionNeeded -> {
            LocationPermissionNeededDialog(onLocationPermissionNeededOkClicked)
        }
        PermissionPermanentlyDenied -> {
            LocationPermanentlyDeniedDialog { onLocationDismissed() }
        }
        PermissionDenied, Error -> {
            LocationDeniedDialog { onLocationDismissed() }
        }
        is LocationAvailable -> {
            content()
        }
        Loading -> {
            ProgressBarComposable()
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