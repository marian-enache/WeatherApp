package com.example.weatherapp.presentation.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.presentation.ui.Teal200
import com.example.weatherapp.presentation.ui.Transparent30

@Preview
@Composable
fun ProgressBarComposable() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Transparent30)
    ) {
        CircularProgressIndicator(
            color = Teal200,
            modifier = Modifier
                .wrapContentSize()
        )
    }
}