package com.example.playlistmaker.domain.search.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Класс [Track] представляет собой модель трека в приложении.
 */
@Parcelize
data class Track(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) : Parcelable
