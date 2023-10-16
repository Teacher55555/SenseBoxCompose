package com.example.sensebox.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sensebox.data.model.Box
import com.example.sensebox.data.model.FavBox
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface BoxDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(box: FavBox)

    @Query("DELETE FROM favBoxes")
    suspend fun deleteAllItems()

    @Query("DELETE FROM favBoxes WHERE id = :id ")
    suspend fun deleteItem(id: String)

    @Query("SELECT * FROM favBoxes")
    fun getAllItems() : Flow<List<FavBox>>

    @Query("SELECT * FROM favBoxes WHERE id = :id ")
    fun getItem(id: String) : Flow<FavBox?>
}