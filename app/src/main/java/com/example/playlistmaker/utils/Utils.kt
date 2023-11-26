package com.example.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

object Utils {

    fun formatTrackDuration(trackTimeMillis: Long): String {
        val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
        return formattedTime
    }

    // Форматирование даты релиза
    fun formattedReleaseDate(releaseDate: String?): String {
        return releaseDate?.take(4) ?: ""
    }

    // Улучшение качества обложки
    fun getArtworkUrl512(artworkUrl100: String): String {
        return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }
}