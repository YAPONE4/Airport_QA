package com.example.helpaero

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpaero.data.Flight
import com.example.helpaero.database.AppDatabase
import com.example.helpaero.database.FlightDB
import com.example.helpaero.database.UserFlightCrossRefDB
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyFlightsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FlightsAdapter
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_flights)

        recyclerView = findViewById(R.id.recyclerViewMyFlights)
        recyclerView.layoutManager = LinearLayoutManager(this)

        db = AppDatabase.getDatabase(this)

        // Достаём текущего пользователя из SharedPreferences
        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1)

        if (userId == -1L) {
            // если не нашли — можно редиректить на LoginActivity или выдать заглушку
            return
        }

        lifecycleScope.launch {
            android.util.Log.d("MyFlightsActivity", "Корутина стартовала")
            var userWithFlights = withContext(Dispatchers.IO) {
                db.userDao().getUserWithFlights(userId)
            }

            if (true) {
                withContext(Dispatchers.IO) {
                    val sampleFlights = listOf(
                        FlightDB(number = "SU123", time = "08:30", destination = "Москва → Санкт-Петербург"),
                        FlightDB(number = "AF456", time = "10:00", destination = "Париж → Москва"),
                        FlightDB(number = "BA789", time = "12:45", destination = "Лондон → Санкт-Петербург")
                    )

                    // Добавляем рейсы (если их ещё нет)
                    android.util.Log.d("MyFlightsActivity", "AAAA" + sampleFlights.toString())

                    db.flightDao().insertFlights(sampleFlights)

                    // Привязываем пользователя к рейсам
                    val crossRefs = sampleFlights.map { flight ->
                        UserFlightCrossRefDB(userId = userId, flightId = flight.id)
                    }
                    android.util.Log.d("MyFlightsActivity", "BBBB" + crossRefs.toString())
                    db.userDao().insertUserFlights(crossRefs)
                }

                userWithFlights = withContext(Dispatchers.IO) {
                    db.userDao().getUserWithFlights(userId)
                }
            }

            // преобразуем FlightDB в UI-модель Flight
            val flights = userWithFlights?.flights?.map {
                Flight(it.number, it.time, it.destination)
            } ?: emptyList()

            adapter = FlightsAdapter(flights)
            recyclerView.adapter = adapter
        }

        // нижняя навигация
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_map -> {
                    startActivity(android.content.Intent(this, MapActivity::class.java))
                    true
                }
                R.id.nav_flights -> {
                    startActivity(android.content.Intent(this, FlightsActivity::class.java))
                    true
                }
                R.id.nav_my_flights -> {
                    // мы уже здесь
                    true
                }
                else -> false
            }
        }
    }
}