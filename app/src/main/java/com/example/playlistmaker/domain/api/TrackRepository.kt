package com.example.playlistmaker.domain.api.track

import com.example.playlistmaker.domain.models.TrackSearchResult

interface TrackRepository {
    fun searchTracks(expression: String): TrackSearchResult
}
