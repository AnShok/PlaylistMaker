package com.example.playlistmaker.ui.settings.view_model

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.ThemeSettingsInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val themeSettingsInteractor: ThemeSettingsInteractor,
) : ViewModel() {
    private val _isThemeSwitcherEnabled = MutableLiveData(false)
    val isThemeSwitcherEnabled: LiveData<Boolean> = _isThemeSwitcherEnabled

    init {
        val theme = getThemeFromShared()
    }

    // Вспомогательная LiveData для обработки событий
    private val _settingsIntentEvent = MutableLiveData<Intent>()
    val settingsIntentEvent: LiveData<Intent> = _settingsIntentEvent


    fun onShareClick() {
        _settingsIntentEvent.value = sharingInteractor.shareApp()
    }

    fun onSupportClick() {
        _settingsIntentEvent.value = sharingInteractor.openSupport()
    }

    fun onTermsClick() {
        _settingsIntentEvent.value = sharingInteractor.openTerms()
    }

    fun setThemeToShared(status: ThemeSettingsInteractor.ThemeMode) {
        themeSettingsInteractor.setThemeToShared(status)
        getThemeFromShared()
    }

    private fun getThemeFromShared(): ThemeSettingsInteractor.ThemeMode {
        val theme = themeSettingsInteractor.applyTheme()
        _isThemeSwitcherEnabled.value = when (theme) {
            ThemeSettingsInteractor.ThemeMode.Light -> false
            ThemeSettingsInteractor.ThemeMode.Night -> true
        }
        return theme
    }

    fun onThemeSwitcherChecked(checked: Boolean) {
        if (checked) {
            setThemeToShared(ThemeSettingsInteractor.ThemeMode.Night)
        } else {
            setThemeToShared(ThemeSettingsInteractor.ThemeMode.Light)
        }
    }
}