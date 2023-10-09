package com.example.playlistmaker

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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
    val country: String
) : Parcelable {
    //Функция расширения для форматирования обложки
    fun  getArtworkUrl512(): String {
        return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }
}

data class TracksResponse(
    val resultCount: Int,
    val results: List<Track>
)
