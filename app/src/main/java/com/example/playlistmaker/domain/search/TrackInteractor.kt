package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.TrackSearchResult
import kotlinx.coroutines.flow.Flow


/**
 * Интерфейс для поиска треков с использованием слушателя результатов.
 */
interface TrackInteractor {
    suspend fun searchTracks(expression: String): Flow<TrackSearchResult>

}