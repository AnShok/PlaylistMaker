package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.ThemeSettingsInteractor

/**
 * Класс приложения, расширяющий Application.
 * Отвечает за инициализацию приложения, управление темой и взаимодействие с ThemeInteractor.
 */
class App : Application() {

    // Интерфейс для взаимодействия с настройками темы
    private lateinit var themeSettingsInteractor: ThemeSettingsInteractor

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this) // Инициализация приложения с помощью Creator
        themeSettingsInteractor =
            Creator.provideThemeSettingsInteractor() // Получение ThemeInteractor с использованием Creator
        switchTheme(themeSettingsInteractor.getThemeFromShared()) // Применение текущей темы
    }

    /**
     * Метод для смены темы приложения.
     *
     * @param darkThemeEnabled флаг, указывающий включена ли темная тема
     */
    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        themeSettingsInteractor.setThemeToShared(darkThemeEnabled) // Обновление настройки темы в ThemeInteractor
    }
}