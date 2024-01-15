package com.example.weatherapp.framework.db

import androidx.room.TypeConverter
import com.example.weatherapp.data.model.LocationModel
import com.google.gson.Gson

class RoomTypeConvertors {
    @TypeConverter
    fun convertLocationModelToJSONString(location: LocationModel): String =
        Gson().toJson(location)

    @TypeConverter
    fun convertJSONStringToLocationModel(jsonString: String): LocationModel =
        Gson().fromJson(jsonString, LocationModel::class.java)
}