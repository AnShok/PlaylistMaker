package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.TrackHistoryInteractor
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.model.SearchStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.model.TrackSearchResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrackSearchViewModel(
    val trackInteractor: TrackInteractor,
    val trackHistoryInteractor: TrackHistoryInteractor,
) : ViewModel() {

    private val _foundTracks: MutableLiveData<TrackSearchResult> =
        MutableLiveData(TrackSearchResult(tracks = emptyList(), SearchStatus.DEFAULT))

    val foundTracks: LiveData<TrackSearchResult> get() = _foundTracks

    private var isClickAllowed = true
    private var searchJob: Job? = null

    var textSearch: String = ""

    fun search() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            if (textSearch.length >= MIN_SEARCH_LENGTH) {
                _foundTracks.postValue(getLoadingStatus())
                performSearch()
            }
        }
    }

    // Выполнение поискового запроса
    private suspend fun performSearch() {
        // Проверка наличия текста перед выполнением запроса
        if (textSearch.isNotEmpty()) {
            trackInteractor.searchTracks(textSearch).collect { result ->
                _foundTracks.postValue(result)
            }
        }
    }

    fun loadSearchHistory(): List<Track> =
        trackHistoryInteractor.loadSearchHistory()

    fun clearSearchHistory() {
        trackHistoryInteractor.clearSearchHistory()
    }

    fun addToSearchHistory(track: Track) {
        trackHistoryInteractor.addToSearchHistory(track)
        loadSearchHistory()
    }

    // Метод позволяет нажимать на элемент списка не чаще одного раза в секунду
    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun changeTextSearch(text: String) {
        textSearch = text
    }

    private fun getLoadingStatus(): TrackSearchResult {
        return TrackSearchResult(
            tracks = emptyList(),
            SearchStatus.LOADING
        )
    }

    companion object {
        // Задержка для избегания многократных кликов (в миллисекундах)
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        // Задержка для избегания многократных запросов поиска (в миллисекундах)
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        // Минимальная длина текста для выполнения поискового запроса
        private const val MIN_SEARCH_LENGTH = 2
    }
}

