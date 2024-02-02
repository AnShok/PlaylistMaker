package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.TrackSearchResult
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория для поиска треков.
 */
interface TrackRepository {

    suspend fun searchTracks(expression: String): Flow<TrackSearchResult>
}
