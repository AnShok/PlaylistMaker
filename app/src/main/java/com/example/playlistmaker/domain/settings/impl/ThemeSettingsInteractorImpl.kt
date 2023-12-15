package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.ThemeSettingsInteractor
import com.example.playlistmaker.domain.settings.ThemeSettingsRepository

/**
 * Реализация интерфейса ThemeInteractor для взаимодействия с настройками темы.
 * Использует ThemeRepository для выполнения операций с настройками темы.
 */
class ThemeSettingsInteractorImpl(private val themeSettingsRepository: ThemeSettingsRepository) : ThemeSettingsInteractor {
    override fun getThemeFromShared(): Boolean {
        return themeSettingsRepository.getThemeFromShared()
    }

    override fun setThemeToShared(status: Boolean) {
        themeSettingsRepository.setThemeToShared(status)
    }
    override fun switchTheme(darkThemeEnabled: Boolean) {
        themeSettingsRepository.switchTheme(darkThemeEnabled)
    }
}