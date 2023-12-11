package com.example.playlistmaker.ui.search.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.search.model.SearchStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.model.TrackSearchResult
import com.example.playlistmaker.ui.player.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.search.adapters.HistoryTracksAdapter
import com.example.playlistmaker.ui.search.adapters.SearchTracksAdapter
import com.example.playlistmaker.ui.search.view_model.TrackSearchViewModel
import com.example.playlistmaker.ui.search.view_model.TrackSearchViewModelFactory


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: TrackSearchViewModel

    private lateinit var searchTracks: ArrayList<Track>//массив результатов поиска
    private lateinit var historyTracks: ArrayList<Track>//массив для историии поиска
    private var editTextSearch = ""

    // Переменные для работы с темой
    private var isDayTheme: Boolean = true

    // Объявление адаптеров для результатов поиска и истории поиска
    private lateinit var searchAdapter: SearchTracksAdapter // Объявление адаптера для результатов поиска
    private lateinit var historyAdapter: HistoryTracksAdapter// Объявление адаптера для истории поиска

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel =
            ViewModelProvider(this, TrackSearchViewModelFactory())[TrackSearchViewModel::class.java]

        viewModel.getFoundTracks().observe(this) { it ->
            performSearching(it)
        }

        searchTracks = ArrayList<Track>()
        historyTracks = ArrayList<Track>()

        searchAdapter = SearchTracksAdapter()
        historyAdapter = HistoryTracksAdapter()

        setupAdapters()

        setupEditText()

        setupButtons()

        // Загрузка истории поиска при создании
        loadSearchHistory()

        setupTheme()

        setupListenersAdapters()

        restoreState(savedInstanceState)
    }

    private fun setupAdapters() {
        // Настройка адаптера для списка найденных треков
        searchAdapter.searchTracks = searchTracks
        binding.searchRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.searchRecyclerView.adapter = searchAdapter

        // Настройка адаптера для списка истории поиска
        historyAdapter.historyTracks = historyTracks
        binding.historyRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.historyRecyclerView.adapter = historyAdapter
    }

    private fun setupEditText() {
        // Обработка события нажатия на кнопку "Готово" на клавиатуре
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Выполнение поискового запроса
                viewModel.searchDebounce()
                true
            } else {
                false
            }
        }

        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputEditText.text.isEmpty() && viewModel.loadSearchHistory()
                    .isNotEmpty()
            ) {
                binding.searchHistoryLayout.visibility = View.VISIBLE
                viewModel.loadSearchHistory()
                historyAdapter.notifyDataSetChanged()
            } else if (viewModel.loadSearchHistory().isNotEmpty()) {
                binding.searchHistoryLayout.visibility = View.VISIBLE
            } else {
                binding.searchHistoryLayout.visibility = View.GONE
            }
        }

        // Настройка слушателя изменения текста в поисковой строке
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Когда тект поискового запроса меняется, он сохраняется в переменную textSearch
                viewModel.textSearch = s.toString()
                binding.clearIcon.visibility = clearButtonVisibility(s)
                // Изменение видимости истории поиска при наличии фокуса и пустом тексте
                binding.searchHistoryLayout.visibility =
                    if (binding.inputEditText.hasFocus() && s?.isEmpty() == true && viewModel.loadSearchHistory()
                            .isNotEmpty()
                    ) View.VISIBLE
                    else View.GONE
                editTextSearch = binding.inputEditText.text.toString()
                // Запуск отложенного поискового запроса только если текст не пустой
                if (editTextSearch.isEmpty()) {
                    hidePlaceholders()
                    viewModel.removeCallbacks()
                } else {
                    viewModel.changeTextSearch(binding.inputEditText.text.toString())
                    viewModel.searchDebounce()
                    hidePlaceholders()
                }

            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun setupButtons() {
        // Обработка нажатия на кнопку очистки поисковой строки
        binding.clearIcon.setOnClickListener {
            clearSearch()
        }

        // Обработка нажатия на кнопку очистки истории поиска
        binding.clearSearchHistoryButton.setOnClickListener {
            historyTracks.clear()
            viewModel.clearSearchHistory()
            historyAdapter.notifyDataSetChanged()
            binding.searchHistoryLayout.visibility = View.GONE
        }

        // Обработка нажатия на кнопку обновления страницы
        binding.refreshButton.setOnClickListener {
            if (viewModel.textSearch.isNotEmpty()) { //для кнопки обновить используемпоследний запрос
                binding.inputEditText.setText(viewModel.textSearch)
                viewModel.searchDebounce()
            }
        }

        // Настройка кнопки Назад для закрытия активити
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun setupTheme() {
        //Определение текущей темы день/ночь
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        isDayTheme = currentNightMode == Configuration.UI_MODE_NIGHT_NO

        //установка темы
        setTheme()
    }

    private fun setupListenersAdapters() {
        // Настройка слушателей нажатия для перехода на экран аудиоплеера
        searchAdapter.setOnItemClickListener(object : SearchTracksAdapter.OnItemClickListener {
            override fun onItemClick(track: Track) {
                startAudioPlayer(track)
            }
        })
        //Переход на экран аудиоплеера
        historyAdapter.setOnItemClickListener(object : HistoryTracksAdapter.OnItemClickListener {
            override fun onItemClick(track: Track) {

                startAudioPlayer(track)
            }
        })
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        // Восстановление состояния при повороте или восстановлении активити
        if (savedInstanceState != null) {
            viewModel.textSearch = savedInstanceState.getString(
                TEXT_SEARCH,
                ""
            ) //Восстановление значения textSearch из сохраненного состояния
            binding.inputEditText.setText(viewModel.textSearch) //Восстановление текст в EditText из сохраненного состояния
            if (viewModel.textSearch.isNotEmpty()) {
                viewModel.searchDebounce()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeCallbacks()
    }

    override fun onResume() {
        super.onResume()
        loadSearchHistory()
        historyAdapter.notifyDataSetChanged()

    }

    // Сохранение состояния при уничтожении активити
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.textSearch =
            binding.inputEditText.text.toString() // Сохранение значения текста поискового запроса в переменную
        outState.putString(
            TEXT_SEARCH,
            viewModel.textSearch
        ) //Сохранение значения textSearch в состояние активити
    }

    // Метод устанавливает тему (день/ночь)
    private fun setTheme() {
        if (isDayTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            //Подстановка изображения для дневной темы
            binding.nothingFoundImage.setImageResource(R.drawable.ic_nothing_found_day)
            binding.errorImage.setImageResource(R.drawable.ic_error_day)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            //Подстановка изображения для ночной темы
            binding.nothingFoundImage.setImageResource(R.drawable.ic_nothing_found_night)
            binding.errorImage.setImageResource(R.drawable.ic_error_night)
        }
    }

    // Метод для перехода на экран аудиоплеера
    private fun startAudioPlayer(track: Track) {
        if (viewModel.clickDebounce()) {
            //Интент для перехода на экран аудиоплеера
            val audioPlayerIntent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)

            //Данные о треке
            audioPlayerIntent.putExtra(TRACK, track)

            // Добавление флага FLAG_ACTIVITY_SINGLE_TOP
            audioPlayerIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(audioPlayerIntent)

            viewModel.addToSearchHistory(track)
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clearSearch() {
        binding.inputEditText.setText("")
        hideKeyboard()
        searchTracks.clear()
        searchAdapter.notifyDataSetChanged()
        hidePlaceholders()
    }

    private fun loadSearchHistory() {
        val history = viewModel.loadSearchHistory()
        historyTracks.clear()
        historyTracks.addAll(history)
        historyAdapter.notifyDataSetChanged()
    }

    //Функция скрытия клавиатуры после очистки
    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
    }

    private fun hidePlaceholders() {
        binding.nothingFoundPlaceholder.visibility = View.GONE
        binding.errorPlaceholder.visibility = View.GONE
    }

    private fun showNothingFoundPlaceholder() {
        binding.searchHistoryLayout.visibility = View.GONE
        binding.nothingFoundPlaceholder.visibility = View.VISIBLE
        binding.errorPlaceholder.visibility = View.GONE
        binding.refreshButton.visibility = View.GONE
    }

    private fun showErrorPlaceholder() {
        binding.nothingFoundPlaceholder.visibility = View.GONE
        binding.errorPlaceholder.visibility = View.VISIBLE
        binding.refreshButton.visibility = View.VISIBLE
    }

    private fun performSearching(foundTracks: TrackSearchResult) {
        searchTracks.clear()
        binding.searchHistoryLayout.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        when (foundTracks.resultStatus) {
            SearchStatus.RESPONSE_RECEIVED -> {
                binding.progressBar.visibility = View.GONE
                binding.searchRecyclerView.visibility = View.VISIBLE
                searchTracks.addAll(foundTracks.tracks)
                searchAdapter.notifyDataSetChanged()
            }

            SearchStatus.NOTHING_FOUND -> {
                binding.progressBar.visibility = View.GONE
                showNothingFoundPlaceholder()
            }

            SearchStatus.NETWORK_ERROR -> {
                binding.progressBar.visibility = View.GONE
                showErrorPlaceholder()
            }

            SearchStatus.DEFAULT -> {
                binding.progressBar.visibility = View.GONE
            }

            SearchStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private const val TEXT_SEARCH = "TEXT_SEARCH"
        private const val TRACK = "track"
    }
}
