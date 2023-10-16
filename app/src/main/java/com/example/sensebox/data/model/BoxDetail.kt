package com.example.sensebox.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BoxDetail (
    @SerialName("_id"              ) val id              : String = "",
    @SerialName("name"             ) val name            : String = "",
    @SerialName("sensors"          ) val sensors         : List<SensorDetail> = emptyList(),
    @SerialName("exposure"         ) val exposure        : String = "",
    @SerialName("createdAt"        ) val createdAt       : String = "",
    @SerialName("model"            ) val model           : String = "",
    @SerialName("image"            ) val image           : String = "",
    @SerialName("currentLocation"  ) val currentLocation : CurrentLocation = CurrentLocation(),
    @SerialName("updatedAt"        ) val updatedAt       : String = "",
    @SerialName("description"      ) val description     : String = "",
    @SerialName("grouptag"         ) val groupTag        : List<String?> = emptyList(),
    @SerialName("lastMeasurementAt") val lastMeasurement : String = "",
    @SerialName("loc"              ) val loc             : List<Loc> = emptyList(),
)

@Serializable
data class SensorDetail (
    @SerialName("title"           ) val title           : String = "",
    @SerialName("unit"            ) val unit            : String = "",
    @SerialName("sensorType"      ) val sensorType      : String = "",
    @SerialName("icon"            ) val icon            : String = "",
    @SerialName("_id"             ) val id              : String = "",
    @SerialName("lastMeasurement" ) val lastMeasurement : LastMeasurement = LastMeasurement(),
)

@Serializable
data class LastMeasurement (
    @SerialName("value"     ) val value     : String = "",
    @SerialName("createdAt" ) val createdAt : String = ""
)

@Serializable
data class Loc (
    @SerialName("geometry" ) val geometry : Geometry = Geometry(),
    @SerialName("type"     ) val type     : String = ""
)

@Serializable
data class Geometry (
    @SerialName("timestamp"   ) val timestamp   : String = "",
    @SerialName("coordinates" ) val coordinates : List<Double> = emptyList(),
    @SerialName("type"        ) val type        : String = ""
)
