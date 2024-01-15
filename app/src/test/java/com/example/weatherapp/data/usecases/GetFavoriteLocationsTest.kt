package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.FavoriteLocationsDataSource
import com.example.weatherapp.data.model.LocationModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class GetFavoriteLocationsTest {

    private val dataSource: FavoriteLocationsDataSource = mock()

    private val getFavoriteLocations: GetFavoriteLocations by lazy {
        GetFavoriteLocationsImpl(dataSource)
    }

    private val inputMock = listOf(
        LocationModel("1", "Name1", 0.1, 0.2),
        LocationModel("2", "Name2", 0.2, 0.3),
        LocationModel("3", "Name3", 0.3, 0.4)
    )

    private val outputMock = listOf(
        LocationModel("1", "Name1", 0.1, 0.2, true),
        LocationModel("2", "Name2", 0.2, 0.3, true),
        LocationModel("3", "Name3", 0.3, 0.4, true)
    )

    @Test
    fun `test getFavoriteLocations returned nothing`() =
        runTest(UnconfinedTestDispatcher()) {
            dataSource.stub {
                onBlocking { readAll() } doReturn emptyList()
            }

            val actual = getFavoriteLocations()

            verify(dataSource).readAll()
            Assert.assertEquals(0, actual.size)
        }

    @Test
    fun `test getFavoriteLocations returned successfully`() =
        runTest(UnconfinedTestDispatcher()) {
            dataSource.stub {
                onBlocking { readAll() } doReturn inputMock
            }

            val actual = getFavoriteLocations()

            verify(dataSource).readAll()
            Assert.assertArrayEquals(outputMock.toTypedArray(), actual.toTypedArray())
        }
}