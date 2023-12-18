package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.settings.ThemeSettingsInteractor
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

/**
 * Класс приложения, расширяющий Application.
 * Отвечает за инициализацию приложения, управление темой и взаимодействие с ThemeInteractor.
 */
class App : Application() {

    // Интерфейс для взаимодействия с настройками темы
    private lateinit var themeSettingsInteractor: ThemeSettingsInteractor

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule)
        }
        themeSettingsInteractor = getKoin().get()
        switchTheme(themeSettingsInteractor.getThemeFromShared()) // Применение текущей темы
    }

    /**
     * Метод для смены темы приложения.
     *
     * @param darkThemeEnabled флаг, указывающий включена ли темная тема
     */
    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        themeSettingsInteractor.setThemeToShared(darkThemeEnabled) // Обновление настройки темы в ThemeInteractor
    }
}