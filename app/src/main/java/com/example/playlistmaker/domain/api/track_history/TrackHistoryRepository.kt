package com.example.playlistmaker.domain.api.track_history

import com.example.playlistmaker.domain.models.Track

interface TrackHistoryRepository {
    fun addToSearchHistory(track: Track)
    fun loadSearchHistory(): ArrayList<Track>
    fun saveSearchHistory(history: ArrayList<Track>)
    fun clearSearchHistory()
}