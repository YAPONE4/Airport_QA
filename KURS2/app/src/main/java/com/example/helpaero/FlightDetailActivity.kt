package com.example.helpaero

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.helpaero.database.AppDatabase
import com.example.helpaero.database.UserFlightCrossRefDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FlightDetailActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_detail)

        db = AppDatabase.getDatabase(this)

        val number = intent.getStringExtra("flight_number") ?: ""
        val time = intent.getStringExtra("flight_time") ?: ""
        val destination = intent.getStringExtra("flight_destination") ?: ""

        findViewById<TextView>(R.id.tvFlightDetailNumber).text = number
        findViewById<TextView>(R.id.tvFlightDetailTime).text = time
        findViewById<TextView>(R.id.tvFlightDetailDestination).text = destination

        val btnBook = findViewById<Button>(R.id.btnBook)

        btnBook.setOnClickListener {
            val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val userId = prefs.getLong("user_id", -1)
            android.util.Log.d("FlightDetailActivity", "Кнопка нажата")

            if (userId != -1L) {
                android.util.Log.d("FlightDetailActivity", "Прошло проверку")
                lifecycleScope.launch(Dispatchers.IO) {
                    // получаем flightId из базы по номеру
                    val flightDb = db.flightDao().getFlightByNumber(number)
                    android.util.Log.d("FlightDetailActivity", flightDb.toString())
                    if (flightDb != null) {
                        db.userDao().insertUserFlights(
                            listOf(UserFlightCrossRefDB(userId = userId, flightId = flightDb.id))
                        )
                    }
                }
            }
        }
    }
}