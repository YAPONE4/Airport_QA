package com.example.helpaero

import android.content.Context
import android.content.Intent
import android.graphics.Matrix
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MapActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private val matrix = Matrix()
    private var scaleFactor = 1.0f
    private var lastX = 0f
    private var lastY = 0f
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        drawerLayout = findViewById(R.id.drawerLayout)
        topAppBar = findViewById(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }

        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        imageView = findViewById(R.id.imageViewMap)
        imageView.scaleType = ImageView.ScaleType.MATRIX
        imageView.imageMatrix = matrix

        scaleGestureDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val factor = detector.scaleFactor
                scaleFactor = (scaleFactor * factor).coerceIn(0.5f, 3.0f)
                matrix.postScale(factor, factor, detector.focusX, detector.focusY)
                imageView.imageMatrix = matrix
                return true
            }
        })

        imageView.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> { lastX = event.x; lastY = event.y }
                MotionEvent.ACTION_MOVE -> {
                    if (!scaleGestureDetector.isInProgress) {
                        val dx = event.x - lastX
                        val dy = event.y - lastY
                        matrix.postTranslate(dx, dy)
                        imageView.imageMatrix = matrix
                        lastX = event.x
                        lastY = event.y
                    }
                }
            }
            true
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_map
        if (prefs.getBoolean("admin", false)) {
            bottomNav.menu.findItem(R.id.nav_manage_flights).isVisible = true
            bottomNav.menu.findItem(R.id.nav_my_flights).isVisible = false
        }
        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_map -> true
                R.id.nav_flights -> { startActivity(Intent(this, FlightsActivity::class.java)); finish(); true }
                R.id.nav_my_flights -> { startActivity(Intent(this, MyFlightsActivity::class.java)); finish(); true }
                R.id.nav_manage_flights -> { startActivity(Intent(this, ManageFlightsActivity::class.java)); finish(); true }
                else -> false
            }
        }

        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
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
