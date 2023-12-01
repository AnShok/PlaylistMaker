package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.TrackSearchResult

/**
 * Интерфейс для поиска треков с использованием слушателя результатов.
 */
interface TrackInteractor {
    /**
     * Выполняет поиск треков по заданному выражению.
     *
     * @param expression Выражение для поиска треков.
     * @param consumer Слушатель результатов поиска.
     */
    fun searchTracks(expression: String, consumer: TracksConsumer)

    /**
     * Слушатель для обработки найденных треков.
     */
    interface TracksConsumer {
        /**
         * Обрабатывает результаты поиска треков.
         *
         * @param foundTracks Результаты поиска в виде [TrackSearchResult].
         */
        fun consume(foundTracks: TrackSearchResult)
    }

}