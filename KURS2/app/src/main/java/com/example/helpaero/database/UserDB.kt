package com.example.helpaero.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserDB(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val login: String,
    val password: String,
    val admin: Boolean = false
)