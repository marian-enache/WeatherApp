package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.mappers.LocationMapper
import com.example.weatherapp.data.model.LocationModel
import com.example.weatherapp.data.repositories.PlacesRepository
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
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
class GetLocationDetailsTest {

    private val placesRepository: PlacesRepository = mock()
    private val locationMapper = LocationMapper()

    private val getLocationDetails: GetLocationDetails by lazy {
        GetLocationDetailsImpl(placesRepository, locationMapper)
    }

    private val inputMock = Place
            .builder()
            .setId("12")
            .setLatLng(LatLng(1.0, 2.0))
            .setName("name")
            .build()

    private val outputMock = LocationModel("12", "name",1.0, 2.0)

    @Test
    fun `test getLocationDetails should return valid response`() =
        runTest(UnconfinedTestDispatcher()) {
            placesRepository.stub {
                onBlocking { requestLocationDetails(any()) } doReturn inputMock
            }

            val output = getLocationDetails(mock())

            verify(placesRepository).requestLocationDetails(any())
            Assert.assertEquals(outputMock, output)
        }
}