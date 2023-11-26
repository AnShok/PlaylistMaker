package com.example.playlistmaker.domain.api.settings

/**
 * Интерфейс ThemeRepository описывает операции для работы с настройками темы.
 * Предоставляет методы для получения текущей настройки темы и установки новой настройки темы.
 */
interface ThemeRepository {
    fun getThemeFromShared(): Boolean
    fun setThemeToShared(status: Boolean)
}
