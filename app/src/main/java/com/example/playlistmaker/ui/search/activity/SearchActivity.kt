package com.example.playlistmaker.ui.search.activity

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.player.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.search.adapters.HistoryTracksAdapter
import com.example.playlistmaker.ui.search.adapters.SearchTracksAdapter


class SearchActivity : AppCompatActivity() {

    // Переменные для работы с темой
    private var isDayTheme: Boolean = true

    private var isClickAllowed = true

    // Объявление хэндлера для работы с задачами в основном потоке
    private val handler = Handler(Looper.getMainLooper())

    // Объявление адаптеров для результатов поиска и истории поиска
    private val searchAdapter = SearchTracksAdapter() // Объявление адаптера для результатов поиска
    private val historyAdapter = HistoryTracksAdapter() // Объявление адаптера для истории поиска

    // Инициализация контроллера для поиска треков
    private val tracksSearchController = Creator.provideTracksSearchController(this, searchAdapter, historyAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Инициализация контроллера поиска при создании
        tracksSearchController.onCreate()

        // Настройка кнопки Назад для закрытия активити
        val backButton = findViewById<Button>(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }

        // Восстановление состояния при повороте или восстановлении активити
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


        // Настройка слушателей нажатия для перехода на экран аудиоплеера
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

    // Метод устанавливает тему (день/ночь)
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

    // Метод позволяет нажимать на элемент списка не чаще одного раза в секунду
    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    // Метод для перехода на экран аудиоплеера
    private fun startAudioPlayer(track: Track) {
        if (clickDebounce()) {
            //Интент для перехода на экран аудиоплеера
            val audioPlayerIntent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)

            //Данные о треке
            audioPlayerIntent.putExtra("track", track)

            // Добавление флага FLAG_ACTIVITY_SINGLE_TOP
            audioPlayerIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(audioPlayerIntent)

            tracksSearchController.addToSearchHistory(track)
        }
    }

    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}