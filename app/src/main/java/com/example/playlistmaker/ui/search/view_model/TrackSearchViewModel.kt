package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.search.TrackHistoryInteractor
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.model.SearchStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.model.TrackSearchResult
import com.example.playlistmaker.ui.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker.ui.SEARCH_DEBOUNCE_DELAY

class TrackSearchViewModel(
    val trackInteractor: TrackInteractor,
    val trackHistoryInteractor: TrackHistoryInteractor,
) : ViewModel(), TrackInteractor.TracksConsumer {

    private val _foundTracks: MutableLiveData<TrackSearchResult> =
        MutableLiveData(TrackSearchResult(tracks = emptyList(), SearchStatus.DEFAULT))

    val foundTracks: LiveData<TrackSearchResult> get() = _foundTracks

    private var isClickAllowed = true

    // Обработчик для задержки выполнения поискового запроса
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        _foundTracks.postValue(getLoadingStatus())
        performSearch()
    }

    var textSearch: String = ""

    // Выполнение поискового запроса
    private fun performSearch() {
        // Проверка наличия текста перед выполнением запроса
        if (textSearch.isNotEmpty()) {

            trackInteractor.searchTracks( // Выполнение поискового запроса через TrackInteractor
                textSearch, this
            )
        }
    }

    override fun consume(foundTracks: TrackSearchResult) {
        when (foundTracks.resultStatus) {
            SearchStatus.RESPONSE_RECEIVED -> {
                this._foundTracks.postValue(foundTracks)
            }

            SearchStatus.NOTHING_FOUND, SearchStatus.NETWORK_ERROR, SearchStatus.DEFAULT -> {
                this._foundTracks.postValue(foundTracks)
            }

            SearchStatus.LOADING -> {

            }
        }
    }


    fun removeCallbacks() {
        handler.removeCallbacks(searchRunnable)
    }

    fun loadSearchHistory(): ArrayList<Track> =
        trackHistoryInteractor.loadSearchHistory()


    fun clearSearchHistory() {
        trackHistoryInteractor.clearSearchHistory()
    }

    fun addToSearchHistory(track: Track) {
        trackHistoryInteractor.addToSearchHistory(track)
        loadSearchHistory()
    }


    //Функция отложенного запроса
    fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    // Метод позволяет нажимать на элемент списка не чаще одного раза в секунду
    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
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
}


