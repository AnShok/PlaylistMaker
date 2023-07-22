package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.button_search)
        val mediaButton = findViewById<Button>(R.id.button_mediateka)
        val settingsButtons = findViewById<Button>(R.id.button_settings)

        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val SearchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(SearchIntent)
            }
        }

        searchButton.setOnClickListener(imageClickListener)


        mediaButton.setOnClickListener {
            val MeditekaIntent = Intent(this, MediatekaActivity::class.java)
            startActivity(MeditekaIntent)
        }


        settingsButtons.setOnClickListener {
            val SettingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(SettingsIntent)
        }
    }

}