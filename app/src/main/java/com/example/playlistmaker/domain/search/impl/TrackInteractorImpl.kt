package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.search.model.TrackSearchResult
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Executors

/**
 * Реализация интерфейса [TrackInteractor] для выполнения асинхронного поиска треков.
 *
 * @param repository Репозиторий для поиска треков.
 */
class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    override suspend fun searchTracks(expression: String): Flow<TrackSearchResult> {
        return repository.searchTracks(expression)
    }
}
