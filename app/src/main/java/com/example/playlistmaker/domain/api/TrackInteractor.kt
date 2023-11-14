package com.example.playlistmaker.domain.api.track

import com.example.playlistmaker.domain.models.TrackSearchResult
import java.util.function.Consumer

interface TrackInteractor {
    fun searchTracks(expression: String, consumer: Consumer<TrackSearchResult>)

}