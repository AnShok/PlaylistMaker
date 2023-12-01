package com.example.playlistmaker.domain.settings

/**
 * Интерфейс ThemeInteractor определяет операции для взаимодействия с настройками темы.
 * Этот интерфейс предоставляет методы для получения текущей настройки темы и установки новой настройки темы.
 */
interface ThemeSettingsInteractor {
    fun getThemeFromShared(): Boolean //Получение текущей настройки темы.
    fun setThemeToShared(status: Boolean) //Установка новой настройки темы.
    fun switchTheme(darkThemeEnabled: Boolean)
}
