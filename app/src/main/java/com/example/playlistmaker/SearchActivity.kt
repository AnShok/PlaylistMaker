package com.example.playlistmaker

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.appcompat.app.AppCompatDelegate


class SearchActivity : AppCompatActivity() {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)

    private lateinit var queryInput: EditText //глобальная переменная для EditText
    private lateinit var clearButton: ImageView
    private lateinit var tracksList: RecyclerView
    private lateinit var errorText: TextView
    private lateinit var refreshButton: Button
    private lateinit var nothingFoundPlaceholder: View
    private lateinit var errorPlaceholder: View
    private lateinit var searchHistoryLayout: View
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var clearSearchHistoryButton: Button
    private var isDayTheme: Boolean = true

    private val searchTracks = ArrayList<Track>() //массив результатов поиска
    private val historyTracks = ArrayList<Track>() //массив для историии поиска

    private val searchAdapter = SearchTracksAdapter()
    private val historyAdapter = HistoryTracksAdapter()

    private var textSearch: String = "" //глобальная переменная для хранения текста поискового запроса
    private var lastSearchText: String = "" //глобальная переменная для хранения последнего запроса

    private lateinit var searchHistory: SearchHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //Нахождение элементов интерфейса
        nothingFoundPlaceholder = findViewById(R.id.nothingFoundPlaceholder)
        errorPlaceholder = findViewById(R.id.errorPlaceholder)
        errorText = findViewById(R.id.errorText)
        clearButton = findViewById(R.id.clearIcon)
        tracksList = findViewById(R.id.recycler_view)
        queryInput = findViewById(R.id.inputEditText) // инициализация inputEditText в onCreate
        refreshButton = findViewById(R.id.refreshButton)
        searchHistoryLayout = findViewById(R.id.search_history_layout)
        historyRecyclerView = findViewById(R.id.history_recycler_view)
        clearSearchHistoryButton = findViewById(R.id.clearSearchHistoryButton)


        searchAdapter.searchTracks = searchTracks
        tracksList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracksList.adapter = searchAdapter

        historyAdapter.historyTracks = historyTracks
        historyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false )
        historyRecyclerView.adapter = historyAdapter

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
        queryInput.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryLayout.visibility = if (hasFocus && queryInput.text.isEmpty() && historyTracks.isNotEmpty()) View.VISIBLE else View.GONE
        }

        //Кнопка очистки поисковой строки
        clearButton.setOnClickListener {
            clearSearch()
        }

        clearSearchHistoryButton.setOnClickListener {
        clearSearchHistory()
        }

        //Кнопка обновить страницу
        refreshButton.setOnClickListener {
            if (lastSearchText.isNotEmpty()) { //для кнопки обновить используемпоследний запрос
                queryInput.setText(lastSearchText)
                performSearch()
            }
        }

        val backButton = findViewById<Button>(R.id.button_back)
        //Кнопка Назад - закрытие активити
        backButton.setOnClickListener {
            finish()
        }

        if (savedInstanceState != null) {
            lastSearchText = savedInstanceState.getString(TEXT_SEARCH, "") //Восстановление значения lastSearchText из сохраненного состояния
            textSearch = savedInstanceState.getString(TEXT_SEARCH, "") //Восстановление значения textSearch из сохраненного состояния
            queryInput.setText(textSearch) //Восстановление текст в EditText из сохраненного состояния
            if (textSearch.isNotEmpty()) {
                performSearch()
            }
        }

        searchHistory = SearchHistory(getSharedPreferences("SearchHistory", Context.MODE_PRIVATE))

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textSearch = s.toString() //Когда тект поискового запроса меняется, он сохраняется в переменную textSearch
                clearButton.visibility = clearButtonVisibility(s)
                searchHistoryLayout.visibility = if (queryInput.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        queryInput.addTextChangedListener(simpleTextWatcher)

        //Определение текущей темы день/ночь
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        isDayTheme = currentNightMode == Configuration.UI_MODE_NIGHT_NO

        //установка темы
        setTheme()

        loadSearchHistory()

        searchAdapter.setOnItemClickListener(object : SearchTracksAdapter.OnItemClickListener {
            override fun onItemClick(track: Track) {
                //обработка нажатия на трек
                //Будущее действие для выполнения после нажатия
                addToSearchHistory(track)
            }
        })
    }

    private fun clearSearch() {
        queryInput.setText("")
        hideKeyboard()
        searchTracks.clear()
        searchAdapter.notifyDataSetChanged()
        hidePlaceholders()
    }

    private fun performSearch() {
        val searchText = queryInput.text.toString()
        if (searchText.isNotEmpty()) {
            lastSearchText = searchText //Сохранение текста запроса
            itunesService.search(searchText).enqueue(object : Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                    if (response.isSuccessful) {
                        val tracksResponse = response.body()
                        if (tracksResponse != null && tracksResponse.results.isNotEmpty()) {
                            searchTracks.clear()
                            searchTracks.addAll(tracksResponse.results)
                            searchAdapter.notifyDataSetChanged()
                            hidePlaceholders()
                        } else {
                            showNothingFoundPlaceholder()
                        }
                    } else {
                        showErrorPlaceholder()
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showErrorPlaceholder()
                }
            })
        }
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
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(queryInput.windowToken, 0)
    }
    private fun hidePlaceholders() {
        nothingFoundPlaceholder.visibility = View.GONE
        errorPlaceholder.visibility = View.GONE
    }

    private fun showNothingFoundPlaceholder() {
        nothingFoundPlaceholder.visibility = View.VISIBLE
        errorPlaceholder.visibility = View.GONE
        refreshButton.visibility = View.GONE
    }

    private fun showErrorPlaceholder() {
        nothingFoundPlaceholder.visibility = View.GONE
        errorPlaceholder.visibility = View.VISIBLE
        refreshButton.visibility = View.VISIBLE
    }

    private fun setTheme() {
        if (isDayTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            //Подстановка изображения для дневной темы
            findViewById<ImageView>(R.id.nothingFoundImage).setImageResource(R.drawable.ic_nothing_found_day)
            findViewById<ImageView>(R.id.errorImage).setImageResource(R.drawable.ic_error_day)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            //Подстановка изображения для ночной темы
            findViewById<ImageView>(R.id.nothingFoundImage).setImageResource(R.drawable.ic_nothing_found_night)
            findViewById<ImageView>(R.id.errorImage).setImageResource(R.drawable.ic_error_night)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_DAY_THEME, isDayTheme)
        textSearch = queryInput.text.toString() // Сохранение значения текста поискового запроса в переменную
        outState.putString(TEXT_SEARCH, textSearch) //Сохранение значения textSearch в состояние активити
    }

    override fun onResume() {
        super.onResume()
        loadSearchHistory()
        historyAdapter.notifyDataSetChanged()

    }
    private fun loadSearchHistory() {
        val history = searchHistory.loadSearchHistory()
        historyTracks.clear()
        historyTracks.addAll(history)
        historyAdapter.notifyDataSetChanged()
    }

    private fun clearSearchHistory() {
        historyTracks.clear()
        searchHistory.clearSearchHistory()
        historyAdapter.notifyDataSetChanged()
        searchHistoryLayout.visibility = View.GONE

    }

    private fun addToSearchHistory(track: Track) {
        val history = searchHistory.loadSearchHistory()
        history.removeAll { it.trackId == track.trackId }
        history.add(0, track)
        if (history.size > SearchHistory.MAX_HISTORY_SIZE) {
            history.removeAt(SearchHistory.MAX_HISTORY_SIZE)
        }
        searchHistory.saveSearchHistory(history)
        loadSearchHistory()
        Log.d("MyApp", "addToSearchHistory: Добавлен трек $track в историю поиска")
    }

    private companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
        const val IS_DAY_THEME = "IS_DAY_THEME"
        const val ITUNES_URL = "https://itunes.apple.com"
    }
}

