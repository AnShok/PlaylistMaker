package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.TrackSearchResult

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
