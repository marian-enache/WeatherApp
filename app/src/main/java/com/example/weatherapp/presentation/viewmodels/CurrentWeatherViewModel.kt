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
import com.example.weatherapp.presentation.DrawerItem
import com.example.weatherapp.presentation.ScreenState
import com.example.weatherapp.presentation.model.UserCoordinates
import com.example.weatherapp.utils.Resource
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

    val locationSuggestions = mutableStateListOf<LocationSuggestion>()
    val favoriteLocations = mutableStateListOf<LocationModel>()
    val searchedLocation = mutableStateOf<LocationModel?>(null)
    val drawerSelectedText = mutableStateOf("")
    val screenState = mutableStateOf(ScreenState.MY_LOCATION)

    private val _currentWeather = MutableLiveData<Resource<WeatherModel?>>()
    val currentWeather: LiveData<Resource<WeatherModel?>> get() = _currentWeather

    private val _forecastWeather = MutableLiveData<Resource<List<ForecastModel>>>()
    val forecastWeather: LiveData<Resource<List<ForecastModel>>> get() = _forecastWeather

    private val currentWeatherCoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _currentWeather.postValue(Resource.error(throwable))
    }

    private val forecastWeatherCoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _forecastWeather.postValue(Resource.error(throwable))
    }

    fun onCoordinatesReceived(coordinates: UserCoordinates) {
        _currentWeather.postValue(Resource.loading())

        viewModelScope.launch(dispatchersProvider.io + currentWeatherCoroutineExceptionHandler) {
            val weatherModel = getCurrentWeather(coordinates.toCoordinatesModel())

            withContext(dispatchersProvider.main) {
                _currentWeather.postValue(Resource.success(weatherModel))
                onCurrentWeatherReceived(coordinates)
            }
        }
    }

    private fun onCurrentWeatherReceived(coordinates: UserCoordinates) {
        _forecastWeather.postValue(Resource.loading())

        viewModelScope.launch(dispatchersProvider.io + forecastWeatherCoroutineExceptionHandler) {
            val forecast = getWeatherForecast(coordinates.toCoordinatesModel())

            withContext(dispatchersProvider.main) {
                _forecastWeather.postValue(Resource.success(forecast))
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
            DrawerItem.MY_LOCATION_ITEM -> {
                screenState.value = ScreenState.MY_LOCATION

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