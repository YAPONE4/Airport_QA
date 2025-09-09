package com.example.helpaero

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.helpaero.database.AppDatabase
import com.example.helpaero.database.FlightDB
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ManageFlightsActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_flights)

        db = AppDatabase.getDatabase(this)

        val etFlightNumber = findViewById<EditText>(R.id.etFlightNumber)
        val etFlightTime = findViewById<EditText>(R.id.etFlightTime)
        val etCityFrom = findViewById<EditText>(R.id.etCityFrom)
        val etCityTo = findViewById<EditText>(R.id.etCityTo)
        val btnAddFlight = findViewById<Button>(R.id.btnAddFlight)

        btnAddFlight.setOnClickListener {
            val number = etFlightNumber.text.toString().trim()
            val time = etFlightTime.text.toString().trim()
            val cityFrom = etCityFrom.text.toString().trim()
            val cityTo = etCityTo.text.toString().trim()

            if (number.isEmpty() || time.isEmpty() || cityFrom.isEmpty() || cityTo.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val destination = "$cityFrom → $cityTo"

            val flight = FlightDB(number = number, time = time, destination = destination)

            GlobalScope.launch(Dispatchers.IO) {
                db.flightDao().insertFlights(listOf(flight))
            }

            Toast.makeText(this, "Рейс добавлен!", Toast.LENGTH_SHORT).show()

            etFlightNumber.text.clear()
            etFlightTime.text.clear()
            etCityFrom.text.clear()
            etCityTo.text.clear()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_manage_flights
        bottomNav.menu.findItem(R.id.nav_manage_flights).isVisible = true
        bottomNav.menu.findItem(R.id.nav_my_flights).isVisible = false
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_map -> {
                    startActivity(Intent(this, MapActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_flights -> {
                    startActivity(Intent(this, FlightsActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_manage_flights -> true
                else -> false
            }
        }
    }
}