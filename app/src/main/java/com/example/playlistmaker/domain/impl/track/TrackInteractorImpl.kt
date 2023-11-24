package com.example.playlistmaker.domain.impl.track

import com.example.playlistmaker.domain.api.track.TrackInteractor
import com.example.playlistmaker.domain.api.track.TrackRepository
import java.util.concurrent.Executors

/**
 * Реализация интерфейса [TrackInteractor] для выполнения асинхронного поиска треков.
 *
 * @param repository Репозиторий для поиска треков.
 */
class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String, consumer: TrackInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }
}
