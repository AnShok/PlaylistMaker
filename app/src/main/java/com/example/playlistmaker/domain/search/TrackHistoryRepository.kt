package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.Track

interface TrackHistoryRepository {
    fun addToSearchHistory(track: Track)
    fun loadSearchHistory(): ArrayList<Track>
    fun saveSearchHistory(history: ArrayList<Track>)
    fun clearSearchHistory()
}