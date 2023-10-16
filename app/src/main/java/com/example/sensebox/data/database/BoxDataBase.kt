package com.example.sensebox.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sensebox.data.model.Box
import com.example.sensebox.data.model.FavBox

@TypeConverters(Converters::class)
@Database(entities = [FavBox::class], version = 1, exportSchema = false)
abstract class BoxDataBase : RoomDatabase() {
    abstract fun boxDao() : BoxDao
}