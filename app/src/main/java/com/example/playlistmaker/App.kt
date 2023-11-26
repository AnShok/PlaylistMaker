package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.settings.ThemeInteractor

/**
 * Класс приложения, расширяющий Application.
 * Отвечает за инициализацию приложения, управление темой и взаимодействие с ThemeInteractor.
 */
class App : Application() {

    // Интерфейс для взаимодействия с настройками темы
    private lateinit var themeInteractor: ThemeInteractor

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this) // Инициализация приложения с помощью Creator
        themeInteractor = Creator.provideThemeInteractor() // Получение ThemeInteractor с использованием Creator
        switchTheme(themeInteractor.getThemeFromShared()) // Применение текущей темы
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
        themeInteractor.setThemeToShared(darkThemeEnabled) // Обновление настройки темы в ThemeInteractor
    }
}