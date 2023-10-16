package com.example.sensebox.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Box (
    @SerialName("_id"              ) val id              : String = "",
    @SerialName("name"             ) val name            : String = "",
    @SerialName("sensors"          ) val sensors         : List<Sensor> = emptyList(),
    @SerialName("exposure"         ) val exposure        : String = "",
    @SerialName("createdAt"        ) val createdAt       : String = "",
    @SerialName("model"            ) val model           : String = "",
    @SerialName("currentLocation"  ) val currentLocation : CurrentLocation = CurrentLocation(),
    @SerialName("updatedAt"        ) val updatedAt       : String = "",
    @SerialName("description"      ) val description     : String = "",
    @SerialName("grouptag"         ) val groupTag        : List<String?> = emptyList(),
    @SerialName("lastMeasurementAt") val lastMeasurement : String = "",
)

@Serializable
data class Sensor (
    @SerialName("title"           ) val title           : String = "",
    @SerialName("unit"            ) val unit            : String = "",
    @SerialName("sensorType"      ) val sensorType      : String = "",
    @SerialName("icon"            ) val icon            : String = "",
    @SerialName("_id"             ) val id              : String = "",
    @SerialName("lastMeasurement" ) val lastMeasurement : String = "",
)

@Serializable
data class CurrentLocation (
    @SerialName("coordinates" ) val coordinates : List<Double> = emptyList(),
    @SerialName("type"        ) val type        : String = "",
    @SerialName("timestamp"   ) val timestamp   : String = ""
)

