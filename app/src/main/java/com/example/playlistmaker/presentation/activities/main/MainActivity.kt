package com.example.playlistmaker.presentation.activities.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.activities.mediateka.MediatekaActivity
import com.example.playlistmaker.presentation.activities.search.SearchActivity
import com.example.playlistmaker.presentation.activities.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.button_search)
        val mediaButton = findViewById<Button>(R.id.button_mediateka)
        val settingsButton = findViewById<Button>(R.id.button_settings)

        searchButton.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        mediaButton.setOnClickListener {
            val meditekaIntent = Intent(this, MediatekaActivity::class.java)
            startActivity(meditekaIntent)
        }


        settingsButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }

}