package com.example.playlistmaker.domain.settings

import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

/**
 * Интерфейс ThemeRepository описывает операции для работы с настройками темы.
 * Предоставляет методы для получения текущей настройки темы и установки новой настройки темы.
 */
interface ThemeSettingsRepository {
    fun getThemeFromShared(): ThemeSettingsInteractor.ThemeMode
    fun setThemeToShared(status: ThemeSettingsInteractor.ThemeMode)
//    fun switchTheme(darkThemeEnabled: Boolean)
}
