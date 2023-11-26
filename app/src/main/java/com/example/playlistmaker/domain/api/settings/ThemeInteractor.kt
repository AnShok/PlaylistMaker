package com.example.playlistmaker.domain.api.settings

/**
 * Интерфейс ThemeInteractor определяет операции для взаимодействия с настройками темы.
 * Этот интерфейс предоставляет методы для получения текущей настройки темы и установки новой настройки темы.
 */
interface ThemeInteractor {
    fun getThemeFromShared(): Boolean //Получение текущей настройки темы.
    fun setThemeToShared(status: Boolean) //Установка новой настройки темы.
}
