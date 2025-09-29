package com.example.airportqa.database

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.helpaero.database.AppDatabase
import com.example.helpaero.database.UserDB
import com.example.helpaero.database.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrepopulateCallback(
    private val context: Context
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            val userDao: UserDao = AppDatabase.getDatabase(context).userDao()
            val adminUser = UserDB(
                login = "admin",
                password = "1111",
                admin = true
            )
            userDao.insert(adminUser)
        }
    }
}