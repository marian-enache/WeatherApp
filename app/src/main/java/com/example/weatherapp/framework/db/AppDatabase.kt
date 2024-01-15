package com.example.weatherapp.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(value = [RoomTypeConvertors::class])
@Database(entities = [FavoriteLocationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "weatherapp.db"
    }

    abstract fun favoriteLocationsDao(): FavoriteLocationsDao
}