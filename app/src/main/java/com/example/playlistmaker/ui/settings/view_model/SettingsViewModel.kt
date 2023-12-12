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
    private val isDarkThemeEnabled  = MutableLiveData(getThemeFromShared())
    val darkThemeEnabled: LiveData<Boolean> = isDarkThemeEnabled

    // Вспомогательная LiveData для обработки событий
    private val _settingsIntentEvent = MutableLiveData<Intent>()
    val settingsIntentEvent: LiveData<Intent> = _settingsIntentEvent


    fun shareApp() {
        _settingsIntentEvent.value = sharingInteractor.shareApp()
    }

    fun openSupport() {
        _settingsIntentEvent.value = sharingInteractor.openSupport()
    }

    fun openTerms() {
        _settingsIntentEvent.value = sharingInteractor.openTerms()
    }

    fun setThemeToShared(status: Boolean) {
        themeSettingsInteractor.setThemeToShared(status)
    }

    fun switchTheme(checked: Boolean){
        themeSettingsInteractor.switchTheme(checked)
    }

    private fun getThemeFromShared() : Boolean {
        return themeSettingsInteractor.getThemeFromShared()
    }


}