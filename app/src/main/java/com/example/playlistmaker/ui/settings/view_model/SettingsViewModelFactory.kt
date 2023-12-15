package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator

class SettingsViewModelFactory () : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            sharingInteractor = Creator.provideSharingInteractor(),
            themeSettingsInteractor = Creator.provideThemeSettingsInteractor()
        ) as T
    }
}