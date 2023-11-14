package com.example.playlistmaker.data.repository

import android.content.Context

class SettingsThemeManager (context: Context) {
    private val sharedPreferences = context.getSharedPreferences(AppSettings, Context.MODE_PRIVATE)

    fun isDarkThemeEnabled(): Boolean {
        // Чтение настройки темы из SharedPreferences
        return sharedPreferences.getBoolean(IS_DARK_THEME_ENABLED, false)
    }

    fun setDarkThemeEnabled(enabled: Boolean) {
        // Запись настройки темы в SharedPreferences
        sharedPreferences.edit().putBoolean(IS_DARK_THEME_ENABLED, enabled).apply()
    }

    private companion object {
        const val IS_DARK_THEME_ENABLED = "isDarkThemeEnabled"
        const val AppSettings = "AppSettings"
    }
}