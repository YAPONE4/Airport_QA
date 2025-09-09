package com.example.helpaero

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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

        val btnBook = findViewById<Button>(R.id.btnBook)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        if (prefs.getBoolean("admin", false) == true) {
            btnBook.isVisible = false
            btnDelete.isVisible = true
        }

        findViewById<TextView>(R.id.tvFlightDetailNumber).text = number
        findViewById<TextView>(R.id.tvFlightDetailTime).text = time
        findViewById<TextView>(R.id.tvFlightDetailDestination).text = destination

        btnBook.setOnClickListener {
            val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val userId = prefs.getLong("user_id", -1)
            android.util.Log.d("FlightDetailActivity", "Кнопка нажата")

            if (userId != -1L) {
                android.util.Log.d("FlightDetailActivity", "Прошло проверку")
                lifecycleScope.launch(Dispatchers.IO) {
                    val flightDb = db.flightDao().getFlightByNumber(number)
                    android.util.Log.d("FlightDetailActivity", flightDb.toString())
                    if (flightDb != null) {
                        db.userDao().insertUserFlights(
                            listOf(UserFlightCrossRefDB(userId = userId, flightId = flightDb.id))
                        )
                        startActivity(Intent(this@FlightDetailActivity, FlightsActivity::class.java))
                        finish()
                    }
                }
            }
        }

        btnDelete.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val flightDb = db.flightDao().getFlightByNumber(number)
                if (flightDb != null) {
                    db.flightDao().deleteFlight(number)
                    startActivity(Intent(this@FlightDetailActivity, FlightsActivity::class.java))
                    finish()
                }
            }
        }
    }
}