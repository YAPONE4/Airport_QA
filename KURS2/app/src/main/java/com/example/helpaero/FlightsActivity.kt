package com.example.helpaero

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpaero.FlightsAdapter
import com.example.helpaero.MapActivity
import com.example.helpaero.MyFlightsActivity
import com.example.helpaero.R
import com.example.helpaero.data.Flight
import com.example.helpaero.database.AppDatabase
import com.example.helpaero.database.FlightDB
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlightsActivity : AppCompatActivity() {

    private lateinit var rvFlights: RecyclerView
    private lateinit var db: AppDatabase
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flights)

        drawerLayout = findViewById(R.id.drawerLayout)
        topAppBar = findViewById(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        rvFlights = findViewById(R.id.rvFlights)
        rvFlights.layoutManager = LinearLayoutManager(this)

        db = AppDatabase.getDatabase(this)

        GlobalScope.launch(Dispatchers.IO) {
            val flightsFromDb = db.flightDao().getAllFlights().map {
                Flight(it.number, it.time, it.destination)
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
        bottomNav.selectedItemId = R.id.nav_flights
        if (prefs.getBoolean("admin", false) == true) {
            bottomNav.menu.findItem(R.id.nav_manage_flights).isVisible = true
            bottomNav.menu.findItem(R.id.nav_my_flights).isVisible = false
        }
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_map -> {
                    startActivity(android.content.Intent(this, MapActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_flights -> {
                    true
                }
                R.id.nav_my_flights -> {
                    startActivity(android.content.Intent(this, MyFlightsActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_manage_flights -> {
                    startActivity(android.content.Intent(this, ManageFlightsActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }


        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.nav_about_app -> {
                    startActivity(Intent(this, AboutAppActivity::class.java))
                }
                R.id.nav_about_author -> {
                    startActivity(Intent(this, AboutAuthorActivity::class.java))
                }
                R.id.nav_logout -> {
                    val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    prefs.edit().clear().apply()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
}