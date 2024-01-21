package com.example.playlistmaker.domain.settings

/**
 * Интерфейс ThemeInteractor определяет операции для взаимодействия с настройками темы.
 * Этот интерфейс предоставляет методы для получения текущей настройки темы и установки новой настройки темы.
 */
interface ThemeSettingsInteractor {
    fun applyTheme(): ThemeMode //Получение текущей настройки темы.
    fun setThemeToShared(status: ThemeMode) //Установка новой настройки темы.

    sealed class ThemeMode {
        object Night : ThemeMode()
        object Light : ThemeMode()
    }
}
