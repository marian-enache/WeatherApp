package com.example.weatherapp.presentation.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// No dark palette as it's not in scope as of now
private val LightColorPalette = lightColorScheme(
    primary = Purple500,
    secondary = Teal200
)

@Composable
fun WeatherAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorPalette,
        content = content
    )
}