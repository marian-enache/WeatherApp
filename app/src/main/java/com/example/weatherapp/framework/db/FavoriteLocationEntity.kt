package com.example.weatherapp.framework.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapp.data.model.LocationModel

@Entity(tableName = "favorite_locations")
data class FavoriteLocationEntity(
    @PrimaryKey val location: LocationModel
)