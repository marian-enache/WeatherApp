package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.FavoriteLocationsDataSource
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
class AddLocationToFavoritesTest {

    private val dataSource: FavoriteLocationsDataSource = mock()

    private val addLocationToFavorites: AddLocationToFavorites by lazy {
        AddLocationToFavoritesImpl(dataSource)
    }

    @Test
    fun `test addLocationToFavorites did not add`() =
        runTest(UnconfinedTestDispatcher()) {
            dataSource.stub {
                onBlocking { add(any()) } doReturn 0
            }

            val actual = addLocationToFavorites(mock())

            verify(dataSource).add(any())
            Assert.assertFalse(actual)
        }

    @Test
    fun `test addLocationToFavorites successful add`() =
        runTest(UnconfinedTestDispatcher()) {
            dataSource.stub {
                onBlocking { add(any()) } doReturn 1
            }

            val actual = addLocationToFavorites(mock())

            verify(dataSource).add(any())
            Assert.assertTrue(actual)
        }
}