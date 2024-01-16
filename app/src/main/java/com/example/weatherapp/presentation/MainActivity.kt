package com.example.weatherapp.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.weatherapp.presentation.ui.WeatherAppTheme
import com.example.weatherapp.presentation.ui.compose.CurrentWeatherScreen
import com.example.weatherapp.presentation.ui.compose.Drawer
import com.example.weatherapp.presentation.ui.compose.LocationCoordinatesDependentScreen
import com.example.weatherapp.presentation.viewmodels.CurrentWeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val viewModel: CurrentWeatherViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                LocationCoordinatesDependentScreen(
                    lifecycleOwner = this,
                    deviceLocationState = viewModel.locationState.value,
                    onStart = {
                        viewModel.onScreenStarted()
                    },
                    onGoToLocationSettingsClicked = {
                        viewModel.onGoToLocationSettingsClicked()
                    },
                    onLocationDismissed = { finish() },
                    onLocationPermissionNeededOkClicked = {
                        viewModel.onLocationPermissionNeededOkClicked()
                    },
                    content = {
                        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                        val scope = rememberCoroutineScope()

                        Drawer(drawerState = drawerState) {
                            CurrentWeatherScreen {
                                viewModel.onDrawerOpen()
                                scope.launch { drawerState.open() }
                            }
                        }
                    }
                )
            }
        }
    }
}