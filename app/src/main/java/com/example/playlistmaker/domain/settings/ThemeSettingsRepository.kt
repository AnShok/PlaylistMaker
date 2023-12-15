package com.example.playlistmaker.domain.settings

/**
 * Интерфейс ThemeRepository описывает операции для работы с настройками темы.
 * Предоставляет методы для получения текущей настройки темы и установки новой настройки темы.
 */
interface ThemeSettingsRepository {
    fun getThemeFromShared(): Boolean
    fun setThemeToShared(status: Boolean)
    fun switchTheme(darkThemeEnabled: Boolean)
}
