package com.example.playlistmaker.ui.settings.view_model

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

    private fun getThemeFromShared() : Boolean {
        return themeSettingsInteractor.getThemeFromShared()
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun setThemeToShared(status: Boolean) {
        themeSettingsInteractor.setThemeToShared(status)
    }

    fun switchTheme(checked: Boolean){
        themeSettingsInteractor.switchTheme(checked)
    }


}