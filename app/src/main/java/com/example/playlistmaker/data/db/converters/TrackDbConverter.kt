package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.TrackEntity
import com.example.playlistmaker.domain.search.model.Track
import java.util.Date

class TrackDbConverter {
    fun map(track: Track): TrackEntity {

        return TrackEntity(
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
            Date().time
        )
    }

    fun map(track: TrackEntity): Track {

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
            Date().time
        )
    }
}