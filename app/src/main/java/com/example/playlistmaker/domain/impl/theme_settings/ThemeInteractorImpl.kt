package com.example.playlistmaker.domain.impl.theme_settings

import com.example.playlistmaker.domain.api.settings.ThemeInteractor
import com.example.playlistmaker.domain.api.settings.ThemeRepository

/**
 * Реализация интерфейса ThemeInteractor для взаимодействия с настройками темы.
 * Использует ThemeRepository для выполнения операций с настройками темы.
 */
class ThemeInteractorImpl(private val themeRepository: ThemeRepository) : ThemeInteractor {
    override fun getThemeFromShared(): Boolean {
        return themeRepository.getThemeFromShared()
    }

    override fun setThemeToShared(status: Boolean) {
        themeRepository.setThemeToShared(status)
    }
}