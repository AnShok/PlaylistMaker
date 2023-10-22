package com.example.playlistmaker

import android.content.Context
import android.content.res.Configuration
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {

    fun formatTrackDuration(trackTimeMillis: Long): String {
        val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
        return formattedTime
    }
}