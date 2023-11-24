package com.example.playlistmaker.presentation.activities.search

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.activities.player.AudioPlayerActivity
import com.example.playlistmaker.presentation.adapters.HistoryTracksAdapter
import com.example.playlistmaker.presentation.adapters.SearchTracksAdapter


class SearchActivity : AppCompatActivity() {

    private var isDayTheme: Boolean = true

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private val searchAdapter = SearchTracksAdapter() // Объявление адаптера для результатов поиска
    private val historyAdapter = HistoryTracksAdapter() // Объявление адаптера для истории поиска
    private val tracksSearchController = Creator.provideTracksSearchController(this, searchAdapter, historyAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        tracksSearchController.onCreate()

        val backButton = findViewById<Button>(R.id.button_back)
        //Кнопка Назад - закрытие активити
        backButton.setOnClickListener {
            finish()
        }

        if (savedInstanceState != null) {
            tracksSearchController.lastSearchText = savedInstanceState.getString(TEXT_SEARCH, "") //Восстановление значения lastSearchText из сохраненного состояния
            tracksSearchController.textSearch = savedInstanceState.getString(TEXT_SEARCH, "") //Восстановление значения textSearch из сохраненного состояния
            tracksSearchController.queryInput.setText(tracksSearchController.textSearch) //Восстановление текст в EditText из сохраненного состояния
            if (tracksSearchController.textSearch.isNotEmpty()) {
                tracksSearchController.performSearch()
            }
        }

        //Определение текущей темы день/ночь
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        isDayTheme = currentNightMode == Configuration.UI_MODE_NIGHT_NO

        //установка темы
        setTheme()


        //Переход на экран аудиоплеера
        tracksSearchController.searchAdapter.setOnItemClickListener(object : SearchTracksAdapter.OnItemClickListener {
            override fun onItemClick(track: Track) {
                startAudioPlayer(track)
            }
        })
        //Переход на экран аудиоплеера
        tracksSearchController.historyAdapter.setOnItemClickListener(object : HistoryTracksAdapter.OnItemClickListener {
            override fun onItemClick(track: Track) {
                startAudioPlayer(track)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        tracksSearchController.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        tracksSearchController.onResume()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracksSearchController.onSaveInstanceState(outState
        )
    }

    private fun setTheme() {
        if (isDayTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            //Подстановка изображения для дневной темы
            findViewById<ImageView>(R.id.nothing_found_image).setImageResource(R.drawable.ic_nothing_found_day)
            findViewById<ImageView>(R.id.error_image).setImageResource(R.drawable.ic_error_day)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            //Подстановка изображения для ночной темы
            findViewById<ImageView>(R.id.nothing_found_image).setImageResource(R.drawable.ic_nothing_found_night)
            findViewById<ImageView>(R.id.error_image).setImageResource(R.drawable.ic_error_night)
        }
    }

    //Функция позволяет нажать на элемент списка не чаще одного раза в секунду
    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun startAudioPlayer(track: Track) {
        if (clickDebounce()) {
            //Интент для перехода на экран аудиоплеера
            val audioPlayerIntent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)

            //Данные о треке
            audioPlayerIntent.putExtra("track", track)
            startActivity(audioPlayerIntent)

            tracksSearchController.addToSearchHistory(track)
        }
    }

    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}