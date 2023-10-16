package com.example.sensebox.data.fake

import com.example.sensebox.R
import com.example.sensebox.data.model.Box
import com.example.sensebox.data.model.BoxDetail
import com.example.sensebox.data.model.CurrentLocation
import com.example.sensebox.data.model.LastMeasurement
import com.example.sensebox.data.model.Loc
import com.example.sensebox.data.model.Sensor
import com.example.sensebox.data.model.SensorDetail
import com.example.sensebox.data.network.BoxApiService

/**
Fake data for testing and compose functions preview.
Also used as the compose functions at the moment do not work with hilt viewModel
 */

object FakeBoxDataProvider {

    val fakeBoxListItem = Box (
        id = "1",
        name = "Fake Box",
        sensors = listOf(
            Sensor(
                title = "PM10",
                unit = "µg/m³",
                sensorType = "SDS 011",
                icon = "osem-cloud",
                id = "223434342234",
                lastMeasurement = "LastMeasurement()"
            )
        ),
        exposure = "outdoor",
        createdAt = "2022-03-30T11:25:43.763Z",
        model = "luftdaten_sds011_bme280",
        currentLocation = CurrentLocation(
            coordinates = listOf(
                BoxApiService.BREMEN_LONGITUDE.toDouble(),
                BoxApiService.BREMEN_LATITUDE.toDouble()
            ),
            type = "Point",
            timestamp = "2019-09-09T15:12:44.670Z"
        ),
        updatedAt = "2022-03-30T11:25:43.763Z",
        groupTag = emptyList()
    )

    val fakeDetailBox = BoxDetail (
        id = "1",
        name = "Fake Box",
        sensors = listOf(
            SensorDetail(
                title = "PM10",
                unit = "µg/m³",
                sensorType = "SDS 011",
                icon = "osem-cloud",
                id = "223434342234",
                lastMeasurement = LastMeasurement(
                    value = "17.435345345345"
                )
            )
        ),
        exposure = "outdoor",
        createdAt = "2022-03-30T11:25:43.763Z",
        model = "luftdaten_sds011_bme280",
        currentLocation = CurrentLocation(
            coordinates = listOf(
                BoxApiService.BREMEN_LONGITUDE.toDouble(),
                BoxApiService.BREMEN_LATITUDE.toDouble()
            ),
            type = "Point",
            timestamp = "2019-09-09T15:12:44.670Z"
        ),
        updatedAt = "2022-03-30T11:25:43.763Z",
        groupTag = emptyList()
    )

}