package com.example.sensebox.data.database

import androidx.room.TypeConverter
import com.example.sensebox.data.model.Sensor
import com.example.sensebox.data.model.SensorDetail
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromList(value : List<SensorDetail>) = Json.encodeToString(value)

    @TypeConverter
    fun toList(value: String) = Json.decodeFromString<List<SensorDetail>>(value)
}
