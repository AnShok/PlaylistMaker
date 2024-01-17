package com.example.playlistmaker.ui.settings.view_model

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.ThemeSettingsInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    // review private. Студия подсвечивает некоторые ошибки
    private val sharingInteractor: SharingInteractor,
    private val themeSettingsInteractor: ThemeSettingsInteractor,
) : ViewModel() {
    // review названия mutableLiveData и LiveData обычно различаются _
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
        // review паттерн repository подразумевает, что мы скрываем способ хранения данных внутри репо. Поэтому не стоит указывать способ хранения во viewModel.
        // Сейчас это shared, а в следующий раз может быть БД
        themeSettingsInteractor.setThemeToShared(status)
        getThemeFromShared()
    }

//    fun switchTheme(checked: Boolean) {
//        themeSettingsInteractor.switchTheme(checked)
//    }

    private fun getThemeFromShared(): ThemeSettingsInteractor.ThemeMode {
        val theme = themeSettingsInteractor.applyTheme()
        _isThemeSwitcherEnabled.value = when(theme){
            ThemeSettingsInteractor.ThemeMode.Light -> false
            ThemeSettingsInteractor.ThemeMode.Night -> true
        }
        return theme
    }

    fun onThemeSwitcherChecked(checked: Boolean) {
        // review Вместо Boolean можно использовать sealed class. Код будет легче читаться. Не нужно будет запоминать, что обозначают true - false
        if (checked){
            setThemeToShared(ThemeSettingsInteractor.ThemeMode.Night)
        } else {
            setThemeToShared(ThemeSettingsInteractor.ThemeMode.Light)
        }
    }

}