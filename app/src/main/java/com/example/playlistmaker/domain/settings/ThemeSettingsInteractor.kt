package com.example.playlistmaker.domain.settings

import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

/**
 * Интерфейс ThemeInteractor определяет операции для взаимодействия с настройками темы.
 * Этот интерфейс предоставляет методы для получения текущей настройки темы и установки новой настройки темы.
 */
interface ThemeSettingsInteractor {
    fun applyTheme(): ThemeMode //Получение текущей настройки темы.
    fun setThemeToShared(status: ThemeMode) //Установка новой настройки темы.
//    fun switchTheme(darkThemeEnabled: Boolean)

    sealed class ThemeMode {
        object Night: ThemeMode()
        object Light: ThemeMode()
    }
}
