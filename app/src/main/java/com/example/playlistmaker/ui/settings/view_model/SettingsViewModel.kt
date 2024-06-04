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

    // Вспомогательная LiveData для обработки событий
    private val _settingsIntentEvent = MutableLiveData<Event<Intent>>()
    val settingsIntentEvent: LiveData<Event<Intent>> = _settingsIntentEvent

    init {
        val theme = getTheme()
    }

    fun onShareClick() {
        _settingsIntentEvent.value = Event(sharingInteractor.shareApp())
    }

    fun onSupportClick() {
        _settingsIntentEvent.value = Event(sharingInteractor.openSupport())
    }

    fun onTermsClick() {
        _settingsIntentEvent.value = Event(sharingInteractor.openTerms())
    }

    fun setTheme(status: ThemeSettingsInteractor.ThemeMode) {
        themeSettingsInteractor.setThemeToShared(status)
        getTheme()
    }

    private fun getTheme(): ThemeSettingsInteractor.ThemeMode {
        val theme = themeSettingsInteractor.applyTheme()
        _isThemeSwitcherEnabled.value = when (theme) {
            ThemeSettingsInteractor.ThemeMode.Light -> false
            ThemeSettingsInteractor.ThemeMode.Night -> true
        }
        return theme
    }

    fun onThemeSwitcherChecked(checked: Boolean) {
        if (checked) {
            setTheme(ThemeSettingsInteractor.ThemeMode.Night)
        } else {
            setTheme(ThemeSettingsInteractor.ThemeMode.Light)
        }
    }
}

class Event<out T>(private val content: T) {
    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}