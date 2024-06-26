package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.TrackToPlaylistEntity
import com.example.playlistmaker.domain.search.model.Track

class TracksToPlaylistConverter {
    fun map(track: Track, addTime: Long): TrackToPlaylistEntity {
        return TrackToPlaylistEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            isFavorite = false,
            addTime
        )
    }

    fun map(track: TrackToPlaylistEntity, addTime: Long): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            isFavorite = false,
            addTime
        )
    }
}