package com.example.helpaero.database

import androidx.room.*
import com.example.helpaero.database.UserDB
import com.example.helpaero.database.UserWithFlights

@Dao
interface UserDao {
    @Insert
    suspend fun insert(userDB: UserDB)

    @Query("SELECT * FROM users WHERE login = :login AND password = :password LIMIT 1")
    suspend fun getUser(login: String, password: String): UserDB?

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserWithFlights(userId: Long): UserWithFlights?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserFlights(crossRefs: List<UserFlightCrossRefDB>)
}