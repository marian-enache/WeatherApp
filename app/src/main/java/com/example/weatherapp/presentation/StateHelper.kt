package com.example.weatherapp.presentation

enum class ScreenState {
    DEVICE_LOCATION, CUSTOM_LOCATION, LOCATION_SEARCH_MODAL, UNKNOWN
}

enum class DrawerItem(val displayText: String) {
    DEVICE_LOCATION_ITEM("My location"), SEARCH_LOCATION_ITEM("Search location")
}

enum class LoadingState {
    FULL_SCREEN, FORECAST_ONLY, NONE
}

sealed class UiState<T> {
    class SuccessState<T>(val data: T) : UiState<T>()
    class ErrorState<T>(val throwable: Throwable?) : UiState<T>()
    class UninitialisedState<T>() : UiState<T>()
}
