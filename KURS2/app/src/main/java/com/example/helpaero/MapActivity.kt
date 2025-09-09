package com.example.helpaero

import android.content.Context
import android.graphics.Matrix
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private val matrix = Matrix()
    private var scaleFactor = 1.0f

    // Для drag
    private var lastX = 0f
    private var lastY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        imageView = findViewById(R.id.imageViewMap)
        imageView.scaleType = ImageView.ScaleType.MATRIX
        imageView.imageMatrix = matrix

        // Масштабирование
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
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.x
                    lastY = event.y
                }
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
        if (prefs.getBoolean("admin", false) == true) {
            bottomNav.menu.findItem(R.id.nav_manage_flights).isVisible = true
            bottomNav.menu.findItem(R.id.nav_my_flights).isVisible = false
        }
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_map -> true
                R.id.nav_flights -> {
                    startActivity(android.content.Intent(this, FlightsActivity::class.java))
                    finish()
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
    }
}
