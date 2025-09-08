package com.example.helpaero

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpaero.data.Flight
import com.google.android.material.bottomnavigation.BottomNavigationView

class FlightsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flights)

        // RecyclerView
        val rvFlights = findViewById<RecyclerView>(R.id.rvFlights)
        rvFlights.layoutManager = LinearLayoutManager(this)

        // Заглушки рейсов
        val sampleFlights = listOf(
            Flight("SU123", "08:30", "Москва → Санкт-Петербург"),
            Flight("AF456", "10:00", "Париж → Москва"),
            Flight("BA789", "12:45", "Лондон → Санкт-Петербург"),
            Flight("EK321", "15:20", "Дубай → Москва"),
            Flight("LH654", "18:00", "Франкфурт → Санкт-Петербург"),
            Flight("SU123", "08:30", "Москва → Санкт-Петербург"),
            Flight("AF456", "10:00", "Париж → Москва"),
            Flight("BA789", "12:45", "Лондон → Санкт-Петербург"),
            Flight("EK321", "15:20", "Дубай → Москва"),
            Flight("LH654", "18:00", "Франкфурт → Санкт-Петербург")
        )

        rvFlights.adapter = FlightsAdapter(sampleFlights)

        // Нижняя панель (оставляем заглушки)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_map -> {
                    startActivity(android.content.Intent(this, MapActivity::class.java))
                    true
                }
                R.id.nav_flights -> {
                    //startActivity(android.content.Intent(this, FlightsActivity::class.java))
                    true
                }
                R.id.nav_my_flights -> {
                    startActivity(android.content.Intent(this, MyFlightsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}