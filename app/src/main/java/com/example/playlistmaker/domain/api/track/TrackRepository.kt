package com.example.playlistmaker.domain.api.track

import com.example.playlistmaker.domain.models.TrackSearchResult

/**
 * Интерфейс репозитория для поиска треков.
 */
interface TrackRepository {
    /**
     * Выполняет поиск треков по заданному выражению.
     *
     * @param expression Выражение для поиска треков.
     * @return Результаты поиска в виде [TrackSearchResult].
     */
    fun searchTracks(expression: String): TrackSearchResult
}
