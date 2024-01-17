package com.example.playlistmaker.domain.settings.impl

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.settings.ThemeSettingsInteractor
import com.example.playlistmaker.domain.settings.ThemeSettingsRepository
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

class ThemeSettingsInteractorImpl(private val themeSettingsRepository: ThemeSettingsRepository) :
    ThemeSettingsInteractor {
    override fun applyTheme(): ThemeSettingsInteractor.ThemeMode {
        val theme = themeSettingsRepository.getThemeFromShared()
        setTheme(theme)
        return theme
    }

    override fun setThemeToShared(status: ThemeSettingsInteractor.ThemeMode) {

        themeSettingsRepository.setThemeToShared(status)
    }

//    override fun switchTheme(darkThemeEnabled: Boolean) {
//        themeSettingsRepository.switchTheme(darkThemeEnabled)
//    }

    private fun setTheme(status: ThemeSettingsInteractor.ThemeMode){
        AppCompatDelegate.setDefaultNightMode(
            when(status){
                ThemeSettingsInteractor.ThemeMode.Light -> AppCompatDelegate.MODE_NIGHT_NO
                ThemeSettingsInteractor.ThemeMode.Night -> AppCompatDelegate.MODE_NIGHT_YES
            }
        )
    }
}