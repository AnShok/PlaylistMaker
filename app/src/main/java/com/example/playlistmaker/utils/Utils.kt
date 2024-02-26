package com.example.playlistmaker.utils

import android.content.res.Resources
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
    fun getArtworkUrl512(artworkUrl100: String?): String {
        return artworkUrl100?.substringBeforeLast('/') + "512x512bb.jpg"
    }

    // Перевод dp в px
    fun dpToPx(dp: Float, resources: Resources): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}