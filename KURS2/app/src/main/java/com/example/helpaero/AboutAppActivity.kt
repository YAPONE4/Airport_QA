package com.example.helpaero

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.appbar.MaterialToolbar

class AboutAppActivity : AppCompatActivity() {

    private lateinit var topAppBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)

        topAppBar = findViewById(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener {
            finish()
        }
    }
}