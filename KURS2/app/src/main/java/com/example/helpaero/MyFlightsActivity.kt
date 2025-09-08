package com.example.helpaero

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpaero.data.Flight
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.random.Random

class MyFlightsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FlightsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_flights)

        recyclerView = findViewById(R.id.recyclerViewMyFlights)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // создаём список случайных рейсов-заглушек
        val flights = generateRandomFlights()
        adapter = FlightsAdapter(flights)
        recyclerView.adapter = adapter

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


    private fun generateRandomFlights(): List<Flight> {
        val destinations = listOf("Москва", "Париж", "Берлин", "Лондон", "Рим", "Нью-Йорк")
        val flights = mutableListOf<Flight>()

        repeat(5) {
            val number = "SU${Random.nextInt(100, 999)}"
            val time = "${Random.nextInt(0, 3).toString().padStart(2, '0')}:${Random.nextInt(0, 59).toString().padStart(2, '0')}"
            val destination = destinations.random()
            flights.add(Flight(number, time, destination))
        }

        return flights
    }
}