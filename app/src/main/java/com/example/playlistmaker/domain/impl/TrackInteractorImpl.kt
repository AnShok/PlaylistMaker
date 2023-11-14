package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.models.TrackSearchResult
import com.example.playlistmaker.domain.api.track.TrackInteractor
import com.example.playlistmaker.domain.api.track.TrackRepository
import java.util.concurrent.Executors
import java.util.function.Consumer

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String, consumer: Consumer<TrackSearchResult>) {
        executor.execute {
            consumer.accept(repository.searchTracks(expression))
        }
    }
}
