package com.example.helpaero

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpaero.FlightsActivity
import com.example.helpaero.data.Flight
import com.example.helpaero.database.AppDatabase
import com.example.helpaero.database.FlightDB
import com.example.helpaero.database.UserFlightCrossRefDB
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyFlightsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var db: AppDatabase
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_flights)

        drawerLayout = findViewById(R.id.drawerLayout)
        topAppBar = findViewById(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        recyclerView = findViewById(R.id.recyclerViewMyFlights)
        recyclerView.layoutManager = LinearLayoutManager(this)
        db = AppDatabase.getDatabase(this)

        // Получаем userId
        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1)
        if (userId == -1L) return

        lifecycleScope.launch(Dispatchers.IO) {
            val userWithFlights = db.userDao().getUserWithFlights(userId)
            val flights = userWithFlights?.flights?.map { Flight(it.number, it.time, it.destination) } ?: emptyList()
            withContext(Dispatchers.Main) {
                recyclerView.adapter = FlightsAdapter(flights)
            }
        }

        // Нижняя навигация
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_my_flights
        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_map -> { startActivity(Intent(this, MapActivity::class.java)); finish(); true }
                R.id.nav_flights -> { startActivity(Intent(this, FlightsActivity::class.java)); finish(); true }
                R.id.nav_my_flights -> true
                else -> false
            }
        }

        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_about_app -> {}
                R.id.nav_about_author -> {}
                R.id.nav_logout -> {
                    prefs.edit().clear().apply()
                    finish()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
}