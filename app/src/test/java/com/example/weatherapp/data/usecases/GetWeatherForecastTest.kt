package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.mappers.WeatherForecastDataMapper
import com.example.weatherapp.data.model.ForecastModel
import com.example.weatherapp.data.model.WeatherType
import com.example.weatherapp.data.repositories.WeatherRepository
import com.example.weatherapp.domain.model.*
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
class GetWeatherForecastTest {

    private val weatherRepository: WeatherRepository = mock()
    private val forecastMapper = WeatherForecastDataMapper()

    private val getWeatherForecast: GetWeatherForecast by lazy {
        GetWeatherForecastImpl(weatherRepository, forecastMapper)
    }

    private val inputMock = ForecastResponse(
        listOf(
            Forecast(
                Main(23.54f, 20f, 30.1f, 25f),
                listOf(Weather("Rain")), 21, "2024-01-11 9:00:00"
            ),
            Forecast(
                Main(23.54f, 20f, 30.1f, 25f),
                listOf(Weather("Rain")), 21, "2024-01-11 12:00:00"
            ),
            Forecast(
                Main(23.54f, 20f, 30.1f, 25f),
                listOf(Weather("Rain")), 21, "2024-01-11 15:00:00"
            ),
            Forecast(
                Main(34.04f, 20f, 30.1f, 25f),
                listOf(Weather("Sunny")), 21, "2024-01-12 15:00:00"
            ),
            Forecast(
                Main(10f, 20f, 30.1f, 25f),
                listOf(Weather("Fog")), 21, "2024-01-13 9:00:00"
            ),
            Forecast(
                Main(-2f, 20f, 30.1f, 25f),
                listOf(Weather("Snow")), 21, "2024-01-13 18:00:00"
            ),
            Forecast(
                Main(30.1f, 20f, 30.1f, 25f),
                listOf(Weather("Thunderstorm")), 21, "2024-01-14 15:00:00"
            ),
            Forecast(
                Main(23.54f, 20f, 30.1f, 25f),
                listOf(Weather("Rain")), 21, "2024-01-14 21:00:00"
            ),
            Forecast(
                Main(23.54f, 20f, 30.1f, 25f),
                listOf(Weather("Rain")), 21, "2024-01-15 9:00:00"
            ),
            Forecast(
                Main(-0.4f, 20f, 30.1f, 25f),
                listOf(Weather("Fog")), 21, "2024-01-15 12:00:00"
            ),
            Forecast(
                Main(23.54f, 20f, 30.1f, 25f),
                listOf(Weather("Rain")), 21, "2024-01-15 15:00:00"
            ),
            Forecast(
                Main(23.54f, 20f, 30.1f, 25f),
                listOf(Weather("Rain")), 21, "2024-01-15 18:00:00"
            ),
            Forecast(
                Main(23.54f, 20f, 30.1f, 25f),
                listOf(Weather("Rain")), 21, "2024-01-15 21:00:00"
            )

        ),
        City("Bucharest")
    )

    private val outputMock =
        listOf(
            ForecastModel(WeatherType.RAIN, "24", "Thursday"),
            ForecastModel(WeatherType.SUNNY, "34", "Friday"),
            ForecastModel(WeatherType.SNOW, "-2", "Saturday"),
            ForecastModel(WeatherType.THUNDERSTORM, "30", "Sunday"),
            ForecastModel(WeatherType.FOG, "0", "Monday")
        )

    @Test
    fun `test getWeatherForecast should return empty list`() =
        runTest(UnconfinedTestDispatcher()) {
            weatherRepository.stub {
                onBlocking { getNext5DayForecast(any(), any()) } doReturn null
            }

            val output = getWeatherForecast(mock())

            verify(weatherRepository).getNext5DayForecast(any(), any())
            Assert.assertEquals(0, output.size)
        }

    @Test
    fun `test getWeatherForecast should return valid response`() =
        runTest(UnconfinedTestDispatcher()) {
            weatherRepository.stub {
                onBlocking { getNext5DayForecast(any(), any()) } doReturn inputMock
            }

            val output = getWeatherForecast(mock())

            verify(weatherRepository).getNext5DayForecast(any(), any())
            Assert.assertArrayEquals(outputMock.toTypedArray(), output.toTypedArray())
        }
}