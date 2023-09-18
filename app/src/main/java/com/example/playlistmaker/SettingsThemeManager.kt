package com.example.playlistmaker

import android.content.Context

class SettingsThemeManager (context: Context) {
    private val sharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

    fun isDarkThemeEnabled(): Boolean {
        // Чтение настройки темы из SharedPreferences
        return sharedPreferences.getBoolean("isDarkThemeEnabled", false)
    }

    fun setDarkThemeEnabled(enabled: Boolean) {
        // Запись настройки темы в SharedPreferences
        sharedPreferences.edit().putBoolean("isDarkThemeEnabled", enabled).apply()
    }
}