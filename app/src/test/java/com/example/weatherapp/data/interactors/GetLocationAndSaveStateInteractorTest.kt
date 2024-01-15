package com.example.weatherapp.data.interactors

import com.example.weatherapp.data.model.LocationModel
import com.example.weatherapp.data.usecases.CheckLocationSavedAsFavorite
import com.example.weatherapp.data.usecases.GetLocationDetails
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
class GetLocationAndSaveStateInteractorTest {

    private val getLocationDetails: GetLocationDetails = mock()
    private val checkLocationSavedAsFavorite: CheckLocationSavedAsFavorite = mock()

    private val getLocation: GetLocationAndSaveStateInteractor by lazy {
        GetLocationAndSaveStateInteractor(getLocationDetails, checkLocationSavedAsFavorite)
    }

    private val inputMock = LocationModel("12", "name", 1.0, 2.0)

    private val unsavedOutputMock = LocationModel("12", "name", 1.0, 2.0)
    private val savedOutputMock = LocationModel("12", "name", 1.0, 2.0, true)


    @Test
    fun `test getLocationAndSaveStateInteractorTest should return location as not saved`() =
        runTest(UnconfinedTestDispatcher()) {
            getLocationDetails.stub {
                onBlocking { invoke(any()) } doReturn inputMock
            }
            checkLocationSavedAsFavorite.stub {
                onBlocking { invoke(any()) } doReturn false
            }

            val output = getLocation(mock())

            verify(getLocationDetails).invoke(any())
            verify(checkLocationSavedAsFavorite).invoke(any())
            Assert.assertEquals(unsavedOutputMock, output)
        }

    @Test
    fun `test getLocationAndSaveStateInteractorTest should return location as saved`() =
        runTest(UnconfinedTestDispatcher()) {
            getLocationDetails.stub {
                onBlocking { invoke(any()) } doReturn inputMock
            }
            checkLocationSavedAsFavorite.stub {
                onBlocking { invoke(any()) } doReturn true
            }

            val output = getLocation(mock())

            verify(getLocationDetails).invoke(any())
            verify(checkLocationSavedAsFavorite).invoke(any())
            Assert.assertEquals(savedOutputMock, output)
        }
}