package com.example.weatherapp.presentation.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.ForecastModel
import com.example.weatherapp.data.model.LocationModel
import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.data.usecases.GetCurrentWeather
import com.example.weatherapp.data.usecases.GetWeatherForecast
import com.example.weatherapp.di.DispatchersProvider
import com.example.weatherapp.data.model.LocationSuggestion
import com.example.weatherapp.data.usecases.GetLocation
import com.example.weatherapp.data.usecases.GetLocationSuggestions
import com.example.weatherapp.presentation.model.UserCoordinates
import com.example.weatherapp.utils.Resource
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
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
    private val getLocation: GetLocation,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    val locationSuggestions = mutableStateListOf<LocationSuggestion>()
    val locationSearchModalShown = mutableStateOf(false)
    val searchedLocation = mutableStateOf<LocationModel?>(null)

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

    fun getFavoriteLocations(): List<String> {
        return emptyList()
    }

    fun onFavoriteLocationClicked() { }

    fun onSearchLocationClicked() {
        locationSearchModalShown.value = true
    }

    fun onCloseSuggestions() {
        locationSearchModalShown.value = false
    }

    fun onLocationInputChanged(input: String) {
        locationSuggestions.clear()

        getLocationSuggestions.invoke(input,
            onSuggestionsFound = {
                locationSuggestions.addAll(it)
            },
            onSuggestionsNotFound = {
                _currentWeather.value = Resource.error()
            })
    }

    fun onSuggestionClicked(suggestion: LocationSuggestion) {
        locationSearchModalShown.value = false
        locationSuggestions.clear()

        getLocation.invoke(suggestion,
        onLocationFound = {
            searchedLocation.value = it

            onCoordinatesReceived(it.toCoordinates())
        },
        onLocationNotFound = {
            _currentWeather.value = Resource.error()
        })
    }

}