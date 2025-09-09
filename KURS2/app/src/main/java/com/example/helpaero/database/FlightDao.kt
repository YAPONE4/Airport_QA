package com.example.helpaero.database

import androidx.room.*
import com.example.helpaero.database.FlightDB
import com.example.helpaero.database.UserFlightCrossRefDB

@Dao
interface FlightDao {
    @Insert
    suspend fun insert(flightDB: FlightDB)

    @Query("SELECT * FROM flights")
    suspend fun getAllFlights(): List<FlightDB>

    @Query("SELECT * FROM flights WHERE number = :number LIMIT 1")
    suspend fun getFlightByNumber(number: String): FlightDB?

    @Insert
    suspend fun insertFlights(flights: List<FlightDB>)

    @Query("DELETE FROM flights WHERE number = :number")
    suspend fun deleteFlight(number: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserFlightCrossRef(ref: UserFlightCrossRefDB)
}