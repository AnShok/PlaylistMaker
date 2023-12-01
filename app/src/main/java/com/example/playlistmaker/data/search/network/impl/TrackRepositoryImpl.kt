package com.example.playlistmaker.data.search.network.impl

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.search.model.SearchStatus
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.dto.TrackSearchResponse
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.search.model.TrackSearchResult
import com.example.playlistmaker.domain.search.model.Track

/**
 * Реализация интерфейса TrackRepository для выполнения поиска треков с использованием NetworkClient.
 */
class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    /**
     * Выполняет поиск треков по выражению.
     *
     * @param expression Выражение для поиска треков.
     * @return Результат поиска в виде TrackSearchResult.
     */
    override fun searchTracks(expression: String): TrackSearchResult {
        try {
            val response = networkClient.doTrackSearchRequest(TrackSearchRequest(expression))

            return when (response.resultStatus) {
                SearchStatus.RESPONSE_RECEIVED -> {
                    // Преобразование результатов поиска из TrackDto в Track
                    val tracks: List<Track> = (response as TrackSearchResponse).results.map {
                        convertToTrack(it)
                    }
                    TrackSearchResult(tracks).apply {
                        resultStatus = SearchStatus.RESPONSE_RECEIVED
                    }
                }

                else -> TrackSearchResult(emptyList()).apply {
                    resultStatus = SearchStatus.NETWORK_ERROR
                }
            }
        } catch (e: Exception) {
            // Возвращаем результат с ошибкой
            return TrackSearchResult(emptyList()).apply {
                resultStatus = SearchStatus.NETWORK_ERROR
            }
        }
    }

    /**
     * Преобразует объект TrackDto в объект Track.
     *
     * @param trackDto Объект TrackDto для преобразования.
     * @return Преобразованный объект Track.
     */
    private fun convertToTrack(trackDto: TrackDto): Track {
        return Track(
            trackDto.trackId,
            trackDto.trackName,
            trackDto.artistName,
            trackDto.trackTimeMillis,
            trackDto.artworkUrl100,
            trackDto.collectionName ?: "unknown",
            trackDto.releaseDate?.takeIf { it.isNotEmpty() } ?: "unknown",
            trackDto.primaryGenreName,
            trackDto.country,
            trackDto.previewUrl
        )
    }
}
