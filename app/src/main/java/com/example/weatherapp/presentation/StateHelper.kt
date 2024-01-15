package com.example.weatherapp.presentation

enum class ScreenState {
    DEVICE_LOCATION, CUSTOM_LOCATION, LOCATION_SEARCH_MODAL, UNKNOWN
}

enum class DrawerItem(val displayText: String) {
    DEVICE_LOCATION_ITEM("My location"), SEARCH_LOCATION_ITEM("Search location")
}
