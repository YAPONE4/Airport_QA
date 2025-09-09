package com.example.helpaero

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpaero.FlightsAdapter
import com.example.helpaero.MapActivity
import com.example.helpaero.MyFlightsActivity
import com.example.helpaero.R
import com.example.helpaero.data.Flight
import com.example.helpaero.database.AppDatabase
import com.example.helpaero.database.FlightDB
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlightsActivity : AppCompatActivity() {

    private lateinit var rvFlights: RecyclerView
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flights)

        rvFlights = findViewById(R.id.rvFlights)
        rvFlights.layoutManager = LinearLayoutManager(this)

        db = AppDatabase.getDatabase(this)

        // Подгружаем рейсы из БД
        GlobalScope.launch(Dispatchers.IO) {
            // Если базы ещё нет, можно вставить начальные рейсы
            if (db.flightDao().getAllFlights().isEmpty()) {
                db.flightDao().insertFlights(listOf<FlightDB>(
                    FlightDB(number = "SU123", time = "08:30", destination = "Москва → Санкт-Петербург"),
                    FlightDB(number = "AF456", time = "10:00", destination = "Париж → Москва"),
                    FlightDB(number = "BA789", time = "12:45", destination = "Лондон → Санкт-Петербург")
                    )
                )
            }

            val flightsFromDb = db.flightDao().getAllFlights().map {
                Flight(it.number, it.time, it.destination) // конвертируем в твой класс Flight
            }

            withContext(Dispatchers.Main) {
                rvFlights.adapter = FlightsAdapter(flightsFromDb) { flight ->
                    val intent = Intent(this@FlightsActivity, FlightDetailActivity::class.java)
                    intent.putExtra("flight_number", flight.number)
                    intent.putExtra("flight_time", flight.time)
                    intent.putExtra("flight_destination", flight.destination)
                    startActivity(intent)
                }
            }
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_map -> {
                    startActivity(android.content.Intent(this, MapActivity::class.java))
                    true
                }
                R.id.nav_flights -> true
                R.id.nav_my_flights -> {
                    startActivity(android.content.Intent(this, MyFlightsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}