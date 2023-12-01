package com.example.playlistmaker.ui.search.view_model

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.model.SearchStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.model.TrackSearchResult
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.ui.search.adapters.HistoryTracksAdapter
import com.example.playlistmaker.ui.search.adapters.SearchTracksAdapter

class TracksSearchController(
    private val activity: Activity,
    val searchAdapter: SearchTracksAdapter,
    val historyAdapter: HistoryTracksAdapter
) {
    // Интерактор для работы с треками
    private val trackInteractor = Creator.provideTrackInteractor()

    // Интерактор для работы с историей поиска
    private val trackHistoryInteractor = Creator.provideTrackHistoryInteractor()

    // Обработчик для задержки выполнения поискового запроса
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { performSearch() }

    // Элементы интерфейса
    lateinit var queryInput: EditText //глобальная переменная для EditText
    private lateinit var clearButton: ImageView
    private lateinit var tracksList: RecyclerView
    private lateinit var errorText: TextView
    private lateinit var refreshButton: Button
    private lateinit var nothingFoundPlaceholder: View
    private lateinit var errorPlaceholder: View
    private lateinit var searchHistoryLayout: View
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var clearSearchHistoryButton: Button
    private lateinit var progressBar: View

    var textSearch: String = "" //глобальная переменная для хранения текста поискового запроса
    var lastSearchText: String = "" //глобальная переменная для хранения последнего запроса
    private val searchTracks = ArrayList<Track>() //массив результатов поиска
    private val historyTracks = ArrayList<Track>() //массив для историии поиска

    // Инициализация контроллера при создании
    fun onCreate() {
        // Нахождение и инициализация элементов интерфейса
        nothingFoundPlaceholder = activity.findViewById(R.id.nothing_found_placeholder)
        errorPlaceholder = activity.findViewById(R.id.error_placeholder)
        errorText = activity.findViewById(R.id.error_text)
        clearButton = activity.findViewById(R.id.clear_icon)
        tracksList = activity.findViewById(R.id.recycler_view)
        queryInput =
            activity.findViewById(R.id.input_edit_text) // инициализация inputEditText в onCreate
        refreshButton = activity.findViewById(R.id.refresh_button)
        searchHistoryLayout = activity.findViewById(R.id.search_history_layout)
        historyRecyclerView = activity.findViewById(R.id.history_recycler_view)
        clearSearchHistoryButton = activity.findViewById(R.id.clear_search_history_button)
        progressBar = activity.findViewById(R.id.progressBar)

        // Настройка адаптера для списка найденных треков
        searchAdapter.searchTracks = searchTracks
        tracksList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        tracksList.adapter = searchAdapter

        // Настройка адаптера для списка истории поиска
        historyAdapter.historyTracks = historyTracks
        historyRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        historyRecyclerView.adapter = historyAdapter

        // Обработка события нажатия на кнопку "Готово" на клавиатуре
        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Выполнение поискового запроса
                performSearch()
                true
            } else {
                false
            }
        }

        //изменение видимости истории поиска от фокуса на вводе текста
        queryInput.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryLayout.visibility =
                if (hasFocus && queryInput.text.isEmpty() && historyTracks.isNotEmpty()) View.VISIBLE
                else View.GONE
        }
        // Обработка нажатия на кнопку очистки поисковой строки
        clearButton.setOnClickListener {
            clearSearch()
        }

        // Обработка нажатия на кнопку очистки истории поиска
        clearSearchHistoryButton.setOnClickListener {
            clearSearchHistory()
        }

        // Обработка нажатия на кнопку обновления страницы
        refreshButton.setOnClickListener {
            if (lastSearchText.isNotEmpty()) { //для кнопки обновить используемпоследний запрос
                queryInput.setText(lastSearchText)
                performSearch()
            }
        }

        // Настройка слушателя изменения текста в поисковой строке
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Когда тект поискового запроса меняется, он сохраняется в переменную textSearch
                textSearch = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
                // Изменение видимости истории поиска при наличии фокуса и пустом тексте
                searchHistoryLayout.visibility =
                    if (queryInput.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
                // Запуск отложенного поискового запроса
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        queryInput.addTextChangedListener(simpleTextWatcher)

        // Загрузка истории поиска при создании
        loadSearchHistory()

    }

    private fun clearSearch() {
        queryInput.setText("")
        hideKeyboard()
        searchTracks.clear()
        searchAdapter.notifyDataSetChanged()
        hidePlaceholders()
    }

    // Выполнение поискового запроса
    fun performSearch() {
        // Получение текста из поисковой строки
        val searchText = queryInput.text.toString()

        // Проверка наличия текста перед выполнением запроса
        if (searchText.isNotEmpty()) {

            // Скрытие истории поиска и отображение индикатора загрузки перед выполнением запроса
            searchHistoryLayout.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            lastSearchText = searchText //Сохранение текста запроса
            trackInteractor.searchTracks( // Выполнение поискового запроса через TrackInteractor
                searchText,
                object : TrackInteractor.TracksConsumer {
                    override fun consume(foundTracks: TrackSearchResult) {
                        handler.post {      // Обработка результатов поиска в основном потоке
                            progressBar.visibility = View.GONE      // Скрытие индикатора загрузки после выполнения запроса

                            when (foundTracks.resultStatus) {       // Обработка результатов в зависимости от статуса запроса

                                SearchStatus.RESPONSE_RECEIVED -> {
                                    if (foundTracks.tracks.isNotEmpty()) {
                                        // Очистка и обновление списка найденных треков
                                        searchTracks.clear()
                                        searchTracks.addAll(foundTracks.tracks)
                                        tracksList.visibility = View.VISIBLE
                                        searchAdapter.notifyDataSetChanged()
                                        hidePlaceholders()
                                    } else {
                                        // Показывает nothingFoundPlaceholder в случае пустого результата
                                        showNothingFoundPlaceholder()
                                    }
                                }
                                // Обработка сетевой ошибки
                                SearchStatus.NETWORK_ERROR -> {
                                    // Показывает errorPlaceholder в случае ошибки
                                    showErrorPlaceholder()
                                }
                                // Обработка других статусов
                                else -> {
                                    showErrorPlaceholder()// Обработка других статусов, если необходимо}
                                }
                            }
                        }
                    }
                })
        }
    }


    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    //Функция скрытия клавиатуры после очистки
    private fun hideKeyboard() {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(queryInput.windowToken, 0)
    }

    private fun hidePlaceholders() {
        nothingFoundPlaceholder.visibility = View.GONE
        errorPlaceholder.visibility = View.GONE
    }

    private fun showNothingFoundPlaceholder() {
        searchHistoryLayout.visibility = View.GONE
        nothingFoundPlaceholder.visibility = View.VISIBLE
        errorPlaceholder.visibility = View.GONE
        refreshButton.visibility = View.GONE
    }

    private fun showErrorPlaceholder() {
        nothingFoundPlaceholder.visibility = View.GONE
        errorPlaceholder.visibility = View.VISIBLE
        refreshButton.visibility = View.VISIBLE
    }

    // Сохранение состояния при уничтожении активити
    fun onSaveInstanceState(outState: Bundle) {
        textSearch =
            queryInput.text.toString() // Сохранение значения текста поискового запроса в переменную
        outState.putString(
            SearchActivity.TEXT_SEARCH,
            textSearch
        ) //Сохранение значения textSearch в состояние активити
    }

    // Восстановление состояния при возврате в активити
    fun onResume() {
        loadSearchHistory()
        historyAdapter.notifyDataSetChanged()

    }

    private fun loadSearchHistory() {
        val history = trackHistoryInteractor.loadSearchHistory()
        historyTracks.clear()
        historyTracks.addAll(history)
        historyAdapter.notifyDataSetChanged()
    }

    private fun clearSearchHistory() {
        historyTracks.clear()
        trackHistoryInteractor.clearSearchHistory()
        historyAdapter.notifyDataSetChanged()
        searchHistoryLayout.visibility = View.GONE

    }

    fun addToSearchHistory(track: Track) {
        trackHistoryInteractor.addToSearchHistory(track)
        loadSearchHistory()
    }


    //Функция отложенного запроса
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}