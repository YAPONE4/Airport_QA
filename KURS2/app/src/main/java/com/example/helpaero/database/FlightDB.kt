package com.example.helpaero.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flights")
data class FlightDB(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val number: String,
    val time: String,
    val destination: String,
    val userId: Int
)