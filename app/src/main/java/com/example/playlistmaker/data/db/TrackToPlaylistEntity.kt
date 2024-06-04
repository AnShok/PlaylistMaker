package com.example.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TRACKS_IN_PLAYLIST)
data class TrackToPlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    var isFavorite: Boolean = false
)

const val TRACKS_IN_PLAYLIST = "tracks_in_playlist"