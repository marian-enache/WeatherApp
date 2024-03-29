package com.example.weatherapp.presentation.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.data.model.*
import com.example.weatherapp.presentation.LoadingState
import com.example.weatherapp.presentation.ScreenState
import com.example.weatherapp.presentation.UiState
import com.example.weatherapp.presentation.ui.Cloudy
import com.example.weatherapp.presentation.ui.Rainy
import com.example.weatherapp.presentation.ui.Sunny
import com.example.weatherapp.presentation.viewmodels.CurrentWeatherViewModel

@Preview
@Composable
fun CurrentWeatherScreen(
    viewModel: CurrentWeatherViewModel = hiltViewModel(),
    onDrawerButtonClicked: () -> Unit = {}
) {
    val currentWeather = viewModel.currentWeather.observeAsState().value
    val forecast by viewModel.forecastWeather
    val loadingState = viewModel.loadingState

    Box {
        when (currentWeather) {
            is UiState.ErrorState -> GeneralErrorComposable()
            is UiState.SuccessState -> {
                CurrentWeather(
                    loadingState = loadingState.value,
                    weather = currentWeather.data,
                    location = viewModel.searchedLocation.value,
                    forecast = forecast,
                    screenState = viewModel.screenState.value,
                    locationSuggestions = viewModel.locationSuggestions,
                    onCurrentLocationFavoriteToggled = { viewModel.onCurrentLocationFavoriteToggled() },
                    onDrawerButtonClicked = onDrawerButtonClicked,
                    onLocationInputChange = { viewModel.onLocationInputChanged(it) },
                    onLocationSuggestionClicked = { viewModel.onSuggestionClicked(it) },
                    onLocationSuggestionsCloseButtonClicked = { viewModel.onCloseSuggestions() }
                )
            }
            else -> {}
        }

        if (loadingState.value == LoadingState.FULL_SCREEN) {
            ProgressBarComposable()
        }
    }
}

@Composable
fun CurrentWeather(
    loadingState: LoadingState = LoadingState.NONE,
    weather: WeatherModel,
    location: LocationModel? = null,
    forecast: UiState<List<ForecastModel>>?,
    screenState: ScreenState = ScreenState.UNKNOWN,
    locationSuggestions: List<LocationSuggestion>,
    onCurrentLocationFavoriteToggled: () -> Unit,
    onDrawerButtonClicked: () -> Unit,
    onLocationInputChange: (String) -> Unit = {},
    onLocationSuggestionClicked: (LocationSuggestion) -> Unit = {},
    onLocationSuggestionsCloseButtonClicked: () -> Unit = {},
) {
    Box {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    color = when {
                        weather.type.isRainy() -> Rainy
                        weather.type.isCloudy() -> Cloudy
                        else -> Sunny
                    }
                )
        ) {
            CurrentWeatherMain(name = weather.name, temperature = weather.currentTemp, type = weather.type)

            LocationTitle(
                location,
                onCurrentLocationFavoriteToggled,
                Modifier.align(CenterHorizontally)
            )

            CurrentWeatherExtra(
                minTemp = weather.minTemp,
                currentTemp = weather.currentTemp,
                maxTemp = weather.maxTemp
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color.White)
            )

            Box {
                when (forecast) {
                    is UiState.SuccessState -> ForecastWeather(forecast.data)
                    is UiState.ErrorState -> GeneralErrorComposable()
                    else -> {}
                }

                if (loadingState == LoadingState.FORECAST_ONLY) {
                    ProgressBarComposable()
                }
            }
        }

        IconButton(
            onClick = { onDrawerButtonClicked() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp, 30.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                tint = Color.White,
            )
        }

        LocationTextField(
            isShown = screenState == ScreenState.LOCATION_SEARCH_MODAL,
            locationSuggestions = locationSuggestions,
            onInputChange = onLocationInputChange,
            onSuggestionClicked = onLocationSuggestionClicked,
            onCloseButtonClicked = onLocationSuggestionsCloseButtonClicked,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
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
fun LocationTitle(location: LocationModel?,
    onLocationFavoriteToggled: () -> Unit,
    modifier: Modifier
) {
    if (location == null || !location.isValid())
        return

    ConstraintLayout(modifier = modifier.fillMaxWidth(0.75f)) {
        val (iconButton, text) = createRefs()

        IconButton(onClick = {
            onLocationFavoriteToggled()
        },
            modifier = Modifier.constrainAs(iconButton) {
                end.linkTo(text.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }) {
            Icon(
                imageVector = if (location.isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = Color.White,
            )
        }
        Text(
            text = location.name,
            color = Color.White,
            fontSize = TextUnit(20f, TextUnitType.Sp),
            fontWeight = FontWeight.SemiBold,
            style = TextStyle(letterSpacing = 2.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    centerHorizontallyTo(parent)
                }
        )
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