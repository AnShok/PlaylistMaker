package com.example.playlistmaker.data.settings.impl

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.app.App
import com.example.playlistmaker.domain.settings.ThemeSettingsRepository
import java.io.Serializable

/**
 * Реализация интерфейса ThemeRepository для работы с настройками темы.
 * Использует SharedPreferences для хранения настроек темы.
 */
class ThemeSettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) : ThemeSettingsRepository, Serializable, Application() {
    // SharedPreferences для хранения настроек темы
    //private val sharedPreferences =
    //    app.getSharedPreferences(APPLICATION_THEME_SHARED_FILE_NAME, MODE_PRIVATE)
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

    // Компаньон объект для хранения констант имен настроек темы
    private companion object {
        //const val APPLICATION_THEME_SHARED_FILE_NAME = "theme_shared_preferences"
        const val APPLICATION_THEME_SWITCHER_SETTINGS = "theme_switch_status"
    }
}
