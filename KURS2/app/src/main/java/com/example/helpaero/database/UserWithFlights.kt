package com.example.helpaero.database

import com.example.helpaero.database.UserFlightCrossRefDB
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserWithFlights(
    @Embedded val user: UserDB,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(UserFlightCrossRefDB::class, parentColumn = "userId", entityColumn = "flightId")
    )
    val flights: List<FlightDB>
)