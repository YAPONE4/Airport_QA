package com.example.helpaero.database

import androidx.room.Entity

@Entity(primaryKeys = ["userId", "flightId"])
data class UserFlightCrossRefDB(
    val userId: Long,
    val flightId: Long
)