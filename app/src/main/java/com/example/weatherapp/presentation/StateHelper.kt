package com.example.weatherapp.presentation

enum class ScreenState {
    MY_LOCATION, CUSTOM_LOCATION, LOCATION_SEARCH_MODAL, UNKNOWN
}

enum class DrawerItem(val displayText: String) {
    MY_LOCATION_ITEM("My location"), SEARCH_LOCATION_ITEM("Search location")
}
