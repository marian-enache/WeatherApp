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
class RemoveLocationFromFavoritesTest {

    private val dataSource: FavoriteLocationsDataSource = mock()

    private val removeLocationFromFavorites: RemoveLocationFromFavorites by lazy {
        RemoveLocationFromFavoritesImpl(dataSource)
    }

    @Test
    fun `test removeLocationFromFavorites did not remove`() =
        runTest(UnconfinedTestDispatcher()) {
            dataSource.stub {
                onBlocking { remove(any()) } doReturn 0
            }

            val actual = removeLocationFromFavorites(mock())

            verify(dataSource).remove(any())
            Assert.assertFalse(actual)
        }

    @Test
    fun `test removeLocationFromFavorites successful remove`() =
        runTest(UnconfinedTestDispatcher()) {
            dataSource.stub {
                onBlocking { remove(any()) } doReturn 1
            }

            val actual = removeLocationFromFavorites(mock())

            verify(dataSource).remove(any())
            Assert.assertTrue(actual)
        }
}