package com.example.sensebox.data.network

import com.example.sensebox.data.model.Box
import com.example.sensebox.data.model.BoxDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BoxesRepository @Inject constructor(private val boxApiService: BoxApiService) {

    suspend fun getBoxes (maxDistance: Int) : List<Box> {
        return boxApiService.getBoxes(maxDistance = maxDistance)
    }

    suspend fun getBoxDetail(id: String) : BoxDetail {
        return boxApiService.getBoxDetail(id)
    }
}