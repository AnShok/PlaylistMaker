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

class TrackSearchViewModel(
    val trackInteractor: TrackInteractor,
    val trackHistoryInteractor: TrackHistoryInteractor,
) : ViewModel(), TrackInteractor.TracksConsumer {

    private val foundTracks: MutableLiveData<TrackSearchResult> =
        MutableLiveData(TrackSearchResult(tracks = emptyList(), SearchStatus.DEFAULT))

    fun getFoundTracks(): LiveData<TrackSearchResult> = foundTracks


    private var isClickAllowed = true

    // Обработчик для задержки выполнения поискового запроса
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        foundTracks.postValue(getLoadingStatus())
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
                this.foundTracks.postValue(foundTracks)
            }

            SearchStatus.NOTHING_FOUND, SearchStatus.NETWORK_ERROR, SearchStatus.DEFAULT -> {
                this.foundTracks.postValue(foundTracks)
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

    private fun getLoadingStatus(): TrackSearchResult {
        return TrackSearchResult(
            tracks = emptyList(),
            SearchStatus.LOADING
        )
    }

    fun changeTextSearch(text: String) {
        textSearch = text
    }


    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}


