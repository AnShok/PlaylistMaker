package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.domain.models.SearchStatus
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.domain.api.track.TrackRepository
import com.example.playlistmaker.domain.models.TrackSearchResult
import com.example.playlistmaker.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doTrackSearchRequest(TrackSearchRequest(expression))

        if (response.resultCode == 200) {
            return (response as TrackSearchResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    if (it.releaseDate.isNullOrEmpty()) {
                        "unknown"
                    } else it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    if (it.previewUrl.isNullOrEmpty()) {
                        "unknown"
                    } else it.previewUrl
                )
            }
        } else {
            return emptyList()
        }
    }
}
