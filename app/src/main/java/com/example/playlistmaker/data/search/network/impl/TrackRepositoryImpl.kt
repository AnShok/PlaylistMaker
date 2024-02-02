package com.example.playlistmaker.data.search.network.impl

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.search.model.SearchStatus
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.dto.TrackSearchResponse
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.search.model.TrackSearchResult
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    /**
     * Выполняет поиск треков по выражению.
     *
     * @param expression Выражение для поиска треков.
     * @return Результат поиска в виде TrackSearchResult.
     */
    override suspend fun searchTracks(expression: String): Flow<TrackSearchResult> = flow {
        val response = networkClient.doTrackSearchRequest(TrackSearchRequest(expression))

        when (response.resultStatus) {
            SearchStatus.RESPONSE_RECEIVED -> {
                // Преобразование результатов поиска из TrackDto в Track
                val tracks: List<Track> = (response as TrackSearchResponse).results.map {
                    convertToTrack(it)
                }
                emit(
                    TrackSearchResult(tracks).apply {
                        if (tracks.isEmpty()) {
                            resultStatus = SearchStatus.NOTHING_FOUND
                        } else {
                            resultStatus = SearchStatus.RESPONSE_RECEIVED
                        }
                    }
                )
            }

            else -> emit(
                TrackSearchResult(emptyList()).apply {
                    resultStatus = SearchStatus.NETWORK_ERROR
                }
            )
        }
    }.catch { _ ->
        // Возвращаем результат с ошибкой
        emit(
            TrackSearchResult(emptyList()).apply {
                resultStatus = SearchStatus.NETWORK_ERROR
            }
        )
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
            trackDto.releaseDate.takeIf { it.isNotEmpty() } ?: "unknown",
            trackDto.primaryGenreName,
            trackDto.country,
            trackDto.previewUrl
        )
    }
}
