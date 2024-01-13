package com.example.weatherapp.presentation.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R


@Preview
@Composable
fun GeneralErrorComposable() {
        Text(
            text = stringResource(id = R.string.error_message),
            fontSize = TextUnit(16f, TextUnitType.Sp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = TextUnit(18f, TextUnitType.Sp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .wrapContentSize()
        )
}