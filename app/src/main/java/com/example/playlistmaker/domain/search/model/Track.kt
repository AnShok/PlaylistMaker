package com.example.playlistmaker.domain.search.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

/**
 * Класс [Track] представляет собой модель трека в приложении.
 */
@Parcelize
data class Track(
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
    var isFavorite: Boolean = false,
    var insertTime: Long?

) : Parcelable {
    init {
        insertTime = insertTime ?: Date().time
    }
}
