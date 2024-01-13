package com.example.weatherapp.presentation.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.data.model.ForecastModel
import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.data.model.WeatherType
import com.example.weatherapp.presentation.ui.Cloudy
import com.example.weatherapp.presentation.ui.Rainy
import com.example.weatherapp.presentation.ui.Sunny
import com.example.weatherapp.utils.Resource
import com.example.weatherapp.presentation.viewmodels.CurrentWeatherViewModel

@Preview
@Composable
fun CurrentWeatherScreen(viewModel: CurrentWeatherViewModel = hiltViewModel()) {
    val currentWeather by viewModel.currentWeather.observeAsState(Resource.loading())
    val forecast by viewModel.forecastWeather.observeAsState(Resource.loading())

    when (val status = currentWeather.status) {
        Resource.Status.LOADING -> ProgressBarComposable()
        Resource.Status.ERROR -> GeneralErrorComposable()
        Resource.Status.SUCCESS -> {
            CurrentWeather(currentWeather.data!!, forecast)
            LaunchedEffect(key1 = status, block = {
                viewModel.onCurrentWeatherReceived()
            })
        }
    }
}

@Composable
fun CurrentWeather(data: WeatherModel, forecast: Resource<List<ForecastModel>>) {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                color = when {
                    data.type.isRainy() -> Rainy
                    data.type.isCloudy() -> Cloudy
                    else -> Sunny
                }
            )
    ) {
        CurrentWeatherMain(name = data.name, temperature = data.currentTemp, type = data.type)
        CurrentWeatherExtra(minTemp = data.minTemp, currentTemp = data.currentTemp, maxTemp = data.maxTemp)
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color = Color.White)
        )

        when (forecast.status) {
            Resource.Status.LOADING -> ProgressBarComposable()
            Resource.Status.ERROR -> GeneralErrorComposable()
            Resource.Status.SUCCESS -> ForecastWeather(forecast.data!!)
        }
    }
}

@Composable
fun CurrentWeatherMain(name: String, temperature: String, type: WeatherType) {
    Box(Modifier.fillMaxWidth()) {
        Image(
            painterResource(
                when {
                    type.isRainy() -> com.example.weatherapp.R.drawable.forest_rainy
                    type.isCloudy() -> com.example.weatherapp.R.drawable.forest_cloudy
                    else -> com.example.weatherapp.R.drawable.forest_sunny
                }
            ),
            "content description",
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(86.dp)
        ) {
            Text(
                text = "$temperature\u00B0",
                color = Color.White,
                fontSize = TextUnit(60f, TextUnitType.Sp),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = name.uppercase(),
                color = Color.White,
                fontSize = TextUnit(24f, TextUnitType.Sp),
                fontWeight = FontWeight.Medium,
                style = TextStyle(letterSpacing = 8.sp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun CurrentWeatherExtra(minTemp: String, currentTemp: String, maxTemp: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 14.dp, top = 4.dp, end = 14.dp, bottom = 8.dp),
    ) {
        TemperatureInfoText(temperature = minTemp, label = "min")
        TemperatureInfoText(temperature = currentTemp, label = "current")
        TemperatureInfoText(temperature = maxTemp, label = "max")
    }
}

@Composable
fun ForecastWeather(forecastList: List<ForecastModel>) {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp)
    ) {
        items(forecastList) { item ->
            ForecastItem(day = item.day, temperature = item.currentTemp, type = item.type)
        }
    }
}


@Composable
fun TemperatureInfoText(temperature: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "${temperature}\u00B0",
            color = Color.White,
            fontSize = TextUnit(16f, TextUnitType.Sp),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = label,
            color = Color.White,
            fontSize = TextUnit(16f, TextUnitType.Sp),
            fontWeight = FontWeight.Normal,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
fun ForecastItem(day: String, temperature: String, type: WeatherType) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = day,
            color = Color.White,
            fontSize = TextUnit(20f, TextUnitType.Sp),
            fontWeight = FontWeight.Light,
            style = TextStyle(letterSpacing = 2.sp),
            modifier = Modifier.align(Alignment.CenterStart)
        )
        Image(
            painterResource(
                when {
                    type.isRainy() -> com.example.weatherapp.R.drawable.ic_rain
                    type.isCloudy() -> com.example.weatherapp.R.drawable.ic_partlysunny
                    else -> com.example.weatherapp.R.drawable.ic_clear
                }
            ),
            "content description",
            modifier = Modifier
                .align(Alignment.Center)
                .size(28.dp)
        )
        Text(
            text = "${temperature}\u00B0",
            color = Color.White,
            fontSize = TextUnit(18f, TextUnitType.Sp),
            fontWeight = FontWeight.Normal,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}