package com.example.playlistmaker.data.settings.impl

import android.app.Application
import android.content.SharedPreferences
import com.example.playlistmaker.domain.settings.ThemeSettingsInteractor
import com.example.playlistmaker.domain.settings.ThemeSettingsRepository
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import java.io.Serializable

class ThemeSettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    ThemeSettingsRepository, Serializable, Application() {
//    private var darkTheme = getThemeFromShared()

    /**
     * Получение текущей настройки темы из SharedPreferences.
     *
     * @return текущая настройка темы
     */
    override fun getThemeFromShared(): ThemeSettingsInteractor.ThemeMode {
        // Чтение настройки темы из SharedPreferences
        return when(sharedPreferences.getBoolean(APPLICATION_THEME_SWITCHER_SETTINGS, false)){
            true -> ThemeSettingsInteractor.ThemeMode.Night
            false -> ThemeSettingsInteractor.ThemeMode.Light
        }
    }

    /**
     * Запись новой настройки темы в SharedPreferences.
     *
     * @param status новая настройка темы
     */
    override fun setThemeToShared(status: ThemeSettingsInteractor.ThemeMode) {
        // Запись настройки темы в SharedPreferences
        sharedPreferences.edit().putBoolean(APPLICATION_THEME_SWITCHER_SETTINGS,
        when(status){
            ThemeSettingsInteractor.ThemeMode.Light ->  false
            ThemeSettingsInteractor.ThemeMode.Night -> true
        }).apply()
    }

//    override fun switchTheme(darkThemeEnabled: Boolean) {
//        darkTheme = darkThemeEnabled

//    }

    companion object {
        // Ключ для сохранения статуса переключателя темы в SharedPreferences
        private const val APPLICATION_THEME_SWITCHER_SETTINGS = "theme_switch_status"

    }
}
