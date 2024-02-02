package com.example.playlistmaker.ui.search.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.model.SearchStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.model.TrackSearchResult
import com.example.playlistmaker.ui.player.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.search.adapters.HistoryTracksAdapter
import com.example.playlistmaker.ui.search.adapters.SearchTracksAdapter
import com.example.playlistmaker.ui.search.view_model.TrackSearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel by viewModel<TrackSearchViewModel>()

    // Объявление адаптеров для результатов поиска и истории поиска
    private val searchAdapter = SearchTracksAdapter() // Объявление адаптера для результатов поиска
    private val historyAdapter = HistoryTracksAdapter()// Объявление адаптера для истории поиска

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.foundTracks.observe(viewLifecycleOwner) { it ->
            performSearching(it)
        }

        setupAdapters()

        setupEditText()

        setupButtons()

        // Загрузка истории поиска при создании
        loadSearchHistory()

        setupListenersAdapters()

        restoreState(savedInstanceState)
    }

    private fun setupAdapters() {
        // Настройка адаптера для списка найденных треков
        binding.searchRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.searchRecyclerView.adapter = searchAdapter

        // Настройка адаптера для списка истории поиска
        binding.historyRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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
                binding.clearIcon.isVisible = !s.isNullOrEmpty()
                // Изменение видимости истории поиска при наличии фокуса и пустом тексте
                binding.searchHistoryLayout.visibility =
                    if (binding.inputEditText.hasFocus() && s?.isEmpty() == true && viewModel.loadSearchHistory()
                            .isNotEmpty()
                    ) View.VISIBLE
                    else View.GONE
                // Запуск отложенного поискового запроса только если текст не пустой
                if (binding.inputEditText.text.toString().isEmpty()) {
                    hidePlaceholders()
                    //viewModel.removeCallbacks() //Может вернуть?
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
            historyAdapter.historyTracks.clear()
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
    }

    private fun setupListenersAdapters() {
        // Настройка слушателей нажатия для перехода на экран аудиоплеера
        searchAdapter.itemClickListener = object : SearchTracksAdapter.OnItemClickListener {
            override fun onItemClick(track: Track) {
                startAudioPlayer(track)
            }
        }

        // Переход на экран аудиоплеера
        historyAdapter.itemClickListener = object : HistoryTracksAdapter.OnItemClickListener {
            override fun onItemClick(track: Track) {
                startAudioPlayer(track)
            }
        }
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        // Восстановление состояния при повороте или восстановлении активити
        if (savedInstanceState != null) {
            viewModel.textSearch = savedInstanceState.getString(
                TEXT_SEARCH,
                ""
            ) ?: "" //Восстановление значения textSearch из сохраненного состояния
            binding.inputEditText.setText(viewModel.textSearch) //Восстановление текст в EditText из сохраненного состояния
            if (viewModel.textSearch.isNotEmpty()) {
                viewModel.searchDebounce()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //viewModel.removeCallbacks() //Может вернуть?
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

    // Метод для перехода на экран аудиоплеера
    private fun startAudioPlayer(track: Track) {
        if (viewModel.clickDebounce()) {
            //Интент для перехода на экран аудиоплеера
            val audioPlayerIntent = Intent(requireContext(), AudioPlayerActivity::class.java)

            //Данные о треке
            audioPlayerIntent.putExtra(TRACK, track)

            // Добавление флага FLAG_ACTIVITY_SINGLE_TOP
            audioPlayerIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(audioPlayerIntent)

            viewModel.addToSearchHistory(track)
        }
    }

    private fun clearSearch() {
        binding.inputEditText.setText("")
        hideKeyboard()
        searchAdapter.searchTracks.clear()
        searchAdapter.notifyDataSetChanged()
        hidePlaceholders()
    }

    private fun loadSearchHistory() {
        val history = viewModel.loadSearchHistory()
        historyAdapter.historyTracks.clear()
        historyAdapter.historyTracks.addAll(history)
        historyAdapter.notifyDataSetChanged()
    }

    //Функция скрытия клавиатуры после очистки
    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
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
        searchAdapter.searchTracks.clear()
        binding.searchHistoryLayout.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        when (foundTracks.resultStatus) {
            SearchStatus.RESPONSE_RECEIVED -> {
                binding.progressBar.visibility = View.GONE
                binding.searchRecyclerView.visibility = View.VISIBLE
                searchAdapter.searchTracks.addAll(foundTracks.tracks)
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
