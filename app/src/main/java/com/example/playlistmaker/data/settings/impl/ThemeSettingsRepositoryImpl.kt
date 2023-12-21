package com.example.playlistmaker.data.settings.impl

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.settings.ThemeSettingsRepository
import com.example.playlistmaker.utils.APPLICATION_THEME_SWITCHER_SETTINGS
import java.io.Serializable

class ThemeSettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    ThemeSettingsRepository, Serializable, Application() {
    private var darkTheme = getThemeFromShared()

    /**
     * Получение текущей настройки темы из SharedPreferences.
     *
     * @return текущая настройка темы
     */
    override fun getThemeFromShared(): Boolean {
        // Чтение настройки темы из SharedPreferences
        return sharedPreferences.getBoolean(APPLICATION_THEME_SWITCHER_SETTINGS, false)
    }

    /**
     * Запись новой настройки темы в SharedPreferences.
     *
     * @param status новая настройка темы
     */
    override fun setThemeToShared(status: Boolean) {
        // Запись настройки темы в SharedPreferences
        sharedPreferences.edit().putBoolean(APPLICATION_THEME_SWITCHER_SETTINGS, status).apply()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
