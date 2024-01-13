package com.example.weatherapp.presentation.ui.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.presentation.ui.Teal200

@Preview
@Composable
fun ProgressBarComposable() {
    CircularProgressIndicator(
        color = Teal200,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
    )
}