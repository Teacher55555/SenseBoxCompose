package com.example.sensebox.data.database

import com.example.sensebox.data.model.Box
import com.example.sensebox.data.model.FavBox
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavBoxRepository @Inject constructor(
    private val boxDao: BoxDao
) {
    fun getAllFavBoxesStream() : Flow<List<FavBox>> {
        return boxDao.getAllItems()
    }

    fun getFavBoxStream(id: String) : Flow<FavBox?> {
        return boxDao.getItem(id).filterNotNull()
    }

    suspend fun addItem(box: FavBox) {
        boxDao.addItem(box)
    }

    suspend fun deleteBox(id: String) {
        boxDao.deleteItem(id)
    }

    suspend fun deleteAll() {
        boxDao.deleteAllItems()
    }

}