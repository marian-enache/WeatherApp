package com.example.weatherapp.presentation.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.interactors.DeviceLocationInteractor
import com.example.weatherapp.data.interactors.GetLocationAndSaveStateInteractor
import com.example.weatherapp.data.model.*
import com.example.weatherapp.data.usecases.*
import com.example.weatherapp.di.DispatchersProvider
import com.example.weatherapp.presentation.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val getCurrentWeather: GetCurrentWeather,
    private val getWeatherForecast: GetWeatherForecast,
    private val getLocationSuggestions: GetLocationSuggestions,
    private val getLocation: GetLocationAndSaveStateInteractor,
    private val addLocationToFavorites: AddLocationToFavorites,
    private val removeLocationFromFavorites: RemoveLocationFromFavorites,
    private val getFavoriteLocations: GetFavoriteLocations,
    private val deviceLocationInteractor: DeviceLocationInteractor,
    private val dispatchersProvider: DispatchersProvider,
) : ViewModel() {

    private lateinit var deviceLocationCoordinates: CoordinatesModel

    var locationState = mutableStateOf<DeviceLocationState>(DeviceLocationState.Loading)


    val locationSuggestions = mutableStateListOf<LocationSuggestion>()
    val favoriteLocations = mutableStateListOf<LocationModel>()
    val searchedLocation = mutableStateOf<LocationModel?>(null)
    val drawerSelectedText = mutableStateOf("")
    val screenState = mutableStateOf(ScreenState.DEVICE_LOCATION)
    val loadingState = mutableStateOf(LoadingState.NONE)

    val currentWeather = mutableStateOf<UiState<WeatherModel>>(UninitialisedState())
    val forecastWeather = mutableStateOf<UiState<List<ForecastModel>>>(UninitialisedState())

    private var locationStateJob: Job? = null

    private val currentWeatherCoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        loadingState.value = LoadingState.NONE
        currentWeather.value = ErrorState(throwable)
    }

    private val forecastWeatherCoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        loadingState.value = LoadingState.NONE
        forecastWeather.value = ErrorState(throwable)
    }

    fun onDrawerOpen() {
        favoriteLocations.clear()
        viewModelScope.launch(dispatchersProvider.io) {
            favoriteLocations.addAll(getFavoriteLocations())
        }
    }

    fun onFavoriteLocationClicked(location: LocationModel) {
        screenState.value = ScreenState.CUSTOM_LOCATION
        drawerSelectedText.value = location.name
        searchedLocation.value = location
        fetchCurrentWeather(location.toCoordinates())
    }

    fun onDrawerItemClicked(drawerItem: DrawerItem) {
        drawerSelectedText.value = drawerItem.displayText

        when (drawerItem) {
            DrawerItem.SEARCH_LOCATION_ITEM -> {
                screenState.value = ScreenState.LOCATION_SEARCH_MODAL
            }
            DrawerItem.DEVICE_LOCATION_ITEM -> {
                screenState.value = ScreenState.DEVICE_LOCATION
                searchedLocation.value = null
                fetchCurrentWeather(deviceLocationCoordinates)
            }
        }
    }

    fun onCloseSuggestions() {
        screenState.value = ScreenState.UNKNOWN
    }

    fun onLocationInputChanged(input: String) {
        locationSuggestions.clear()

        viewModelScope.launch(dispatchersProvider.io) {
            locationSuggestions.addAll(getLocationSuggestions(input))
        }
    }

    fun onSuggestionClicked(suggestion: LocationSuggestion) {
        screenState.value = ScreenState.CUSTOM_LOCATION
        locationSuggestions.clear()

        viewModelScope.launch(dispatchersProvider.io) {
            val location = getLocation(suggestion)

            searchedLocation.value = location

            fetchCurrentWeather(location.toCoordinates())
        }
    }

    fun onCurrentLocationFavoriteToggled() {
        val location = searchedLocation.value ?: return

        viewModelScope.launch(dispatchersProvider.io) {
            if (!location.isSaved) {
                val added = addLocationToFavorites(location)

                if (added) {
                    withContext(dispatchersProvider.main) {
                        updateMarkedLocation(true)
                    }
                }
            } else {
                val removed = removeLocationFromFavorites(location)

                if (removed) {
                    withContext(dispatchersProvider.main) {
                        updateMarkedLocation(false)
                    }
                }
            }
        }
    }

    fun onScreenStarted() {
        if (screenState.value == ScreenState.DEVICE_LOCATION) {
            viewModelScope.launch(dispatchersProvider.io) {
                locationState.value = deviceLocationInteractor.getDeviceLocationState()
            }
        }
    }

    fun onLocationPermissionNeededOkClicked() {
        if (locationStateJob?.isActive == true) return

        locationStateJob = viewModelScope.launch(dispatchersProvider.io) {
            locationState.value = deviceLocationInteractor.fetchForLocationPermission()

            if (locationState.value is DeviceLocationState.LocationAvailable) {
                withContext(dispatchersProvider.main) {
                    deviceLocationCoordinates = (locationState.value as DeviceLocationState.LocationAvailable).latLang
                    fetchCurrentWeather(deviceLocationCoordinates)
                }
            }
        }
    }

    fun onGoToLocationSettingsClicked() {
        viewModelScope.launch(dispatchersProvider.io) {
            deviceLocationInteractor.goToLocationSettings()
        }
    }


    private fun fetchCurrentWeather(coordinates: CoordinatesModel) {
        loadingState.value = LoadingState.FULL_SCREEN

        viewModelScope.launch(dispatchersProvider.io + currentWeatherCoroutineExceptionHandler) {
            val weatherModel = getCurrentWeather(coordinates)

            withContext(dispatchersProvider.main) {
                loadingState.value = LoadingState.NONE
                currentWeather.value = SuccessState(weatherModel!!)
                fetchWeatherForecast(coordinates)
            }
        }
    }

    private fun fetchWeatherForecast(coordinates: CoordinatesModel) {
        loadingState.value = LoadingState.FORECAST_ONLY

        viewModelScope.launch(dispatchersProvider.io + forecastWeatherCoroutineExceptionHandler) {
            val forecast = getWeatherForecast(coordinates)

            withContext(dispatchersProvider.main) {
                loadingState.value = LoadingState.NONE
                forecastWeather.value = SuccessState(forecast)
            }
        }
    }


    private fun updateMarkedLocation(marked: Boolean) {
        searchedLocation.value = searchedLocation.value?.copy(isSaved = marked)
    }
}