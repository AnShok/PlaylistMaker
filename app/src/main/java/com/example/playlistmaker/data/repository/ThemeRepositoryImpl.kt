package com.example.playlistmaker.data.repository

import android.app.Application
import com.example.playlistmaker.domain.api.settings.ThemeRepository
import java.io.Serializable

/**
 * Реализация интерфейса ThemeRepository для работы с настройками темы.
 * Использует SharedPreferences для хранения настроек темы.
 */
class ThemeRepositoryImpl (app: Application): ThemeRepository, Serializable, Application() {
    // SharedPreferences для хранения настроек темы
    private val sharedPreferences = app.getSharedPreferences(APPLICATION_THEME_SHARED_FILE_NAME, MODE_PRIVATE)

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

    // Компаньон объект для хранения констант имен настроек темы
    private companion object {
        const val APPLICATION_THEME_SHARED_FILE_NAME = "theme_shared_preferences"
        const val APPLICATION_THEME_SWITCHER_SETTINGS = "theme_switch_status"
    }
}
