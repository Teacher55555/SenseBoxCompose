package com.example.sensebox.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favBoxes")
data class FavBox(
    @PrimaryKey val id : String,
    val name : String,
    val description : String,
    val sensorsCount : Int
)
