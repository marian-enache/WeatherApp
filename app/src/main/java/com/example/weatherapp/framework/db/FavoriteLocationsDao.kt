package com.example.weatherapp.framework.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.weatherapp.data.model.LocationModel

@Dao
interface FavoriteLocationsDao {

  @Insert(onConflict = REPLACE)
  suspend fun addFavoriteLocation(favoriteLocation: FavoriteLocationEntity): Long

  @Query("SELECT * FROM favorite_locations")
  suspend fun getAllFavoriteLocation(): List<FavoriteLocationEntity>

  @Delete
  suspend fun removeFavoriteLocation(favoriteLocation: FavoriteLocationEntity): Int

  @Query("SELECT EXISTS(SELECT * FROM favorite_locations WHERE location = :location)")
  suspend fun isLocationSaved(location: LocationModel) : Boolean
}