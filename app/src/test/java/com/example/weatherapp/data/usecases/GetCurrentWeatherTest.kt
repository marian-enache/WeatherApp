package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.mappers.CurrentWeatherDataMapper
import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.data.model.WeatherType
import com.example.weatherapp.data.repositories.WeatherRepository
import com.example.weatherapp.domain.model.Main
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.model.WeatherResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class GetCurrentWeatherTest {

    private val weatherRepository: WeatherRepository = mock()
    private val weatherMapper = CurrentWeatherDataMapper()

    private val getCurrentWeather: GetCurrentWeather by lazy {
        GetCurrentWeatherImpl(weatherRepository, weatherMapper)
    }

    private val inputMock = WeatherResponse(
        listOf(Weather("Rain")),
        Main(23.54f, 20f, 30.1f, 25f)
    )

    private val outputMock =
        WeatherModel(
            WeatherType.RAIN,
            name = "Rain",
            currentTemp = "24",
            minTemp = "20",
            maxTemp = "30"
        )

    @Test
    fun `test getCurrentWeather should return null`() =
        runTest(UnconfinedTestDispatcher()) {
            weatherRepository.stub {
                onBlocking { getCurrentWeather(any(), any()) } doReturn null
            }

            val output = getCurrentWeather(mock())

            verify(weatherRepository).getCurrentWeather(any(), any())
            Assert.assertEquals(null, output)
        }

    @Test
    fun `test getCurrentWeather should return valid response`() =
        runTest(UnconfinedTestDispatcher()) {
            weatherRepository.stub {
                onBlocking { getCurrentWeather(any(), any()) } doReturn inputMock
            }

            val output = getCurrentWeather(mock())

            verify(weatherRepository).getCurrentWeather(any(), any())
            Assert.assertEquals(outputMock, output)
        }
}