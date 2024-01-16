package com.example.weatherapp.presentation.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.interactors.GetLocationAndSaveStateInteractor
import com.example.weatherapp.data.model.ForecastModel
import com.example.weatherapp.data.model.LocationModel
import com.example.weatherapp.data.model.LocationSuggestion
import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.data.usecases.*
import com.example.weatherapp.di.DispatchersProvider
import com.example.weatherapp.presentation.*
import com.example.weatherapp.presentation.model.UserCoordinates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
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
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    var deviceLocationCoordinates =  mutableStateOf<UserCoordinates?>(null)
    var locationEnabled = mutableStateOf(false)
    var locationPermissionPermanentlyDenied = mutableStateOf(false)

    val locationSuggestions = mutableStateListOf<LocationSuggestion>()
    val favoriteLocations = mutableStateListOf<LocationModel>()
    val searchedLocation = mutableStateOf<LocationModel?>(null)
    val drawerSelectedText = mutableStateOf("")
    val screenState = mutableStateOf(ScreenState.DEVICE_LOCATION)
    val loadingState = mutableStateOf(LoadingState.NONE)

    val currentWeather = mutableStateOf<UiState<WeatherModel>>(UninitialisedState())
    val forecastWeather = mutableStateOf<UiState<List<ForecastModel>>>(UninitialisedState())

    private val currentWeatherCoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        loadingState.value = LoadingState.NONE
        currentWeather.value = ErrorState(throwable)
    }

    private val forecastWeatherCoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        loadingState.value = LoadingState.NONE
        forecastWeather.value = ErrorState(throwable)
    }

    fun onCoordinatesReceived(coordinates: UserCoordinates) {
        if (deviceLocationCoordinates.value == null) {
            deviceLocationCoordinates.value = coordinates
        }

        loadingState.value = LoadingState.FULL_SCREEN

        viewModelScope.launch(dispatchersProvider.io + currentWeatherCoroutineExceptionHandler) {
            val weatherModel = getCurrentWeather(coordinates.toCoordinatesModel())

            withContext(dispatchersProvider.main) {
                loadingState.value = LoadingState.NONE
                currentWeather.value = SuccessState(weatherModel!!)
                onCurrentWeatherReceived(coordinates)
            }
        }
    }

    private fun onCurrentWeatherReceived(coordinates: UserCoordinates) {
        loadingState.value = LoadingState.FORECAST_ONLY

        viewModelScope.launch(dispatchersProvider.io + forecastWeatherCoroutineExceptionHandler) {
            val forecast = getWeatherForecast(coordinates.toCoordinatesModel())

            withContext(dispatchersProvider.main) {
                loadingState.value = LoadingState.NONE
                forecastWeather.value = SuccessState(forecast)
            }
        }
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
        onCoordinatesReceived(location.toCoordinates())
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
                onCoordinatesReceived(deviceLocationCoordinates.value!!)
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

            onCoordinatesReceived(location.toCoordinates())
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

    private fun updateMarkedLocation(marked: Boolean) {
        searchedLocation.value = searchedLocation.value?.copy(isSaved = marked)
    }
}