package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    lateinit var settingsThemeManager: SettingsThemeManager

    override fun onCreate() {
        super.onCreate()
        settingsThemeManager = SettingsThemeManager(applicationContext)
        applyThemeFromSettings()
    }

    fun switchTheme(darkthemeEnabled: Boolean) {
        settingsThemeManager.setDarkThemeEnabled(darkthemeEnabled)
        applyThemeFromSettings()
    }

    private fun applyThemeFromSettings() {
        //Проверка состояния перекл.чателя из sharedPref
        val isDarkThemeEnabled = settingsThemeManager.isDarkThemeEnabled()

        // Установка темы приложения в зависимости от системных настроек
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM //слежка за системной темой
            }
        )
    }
}