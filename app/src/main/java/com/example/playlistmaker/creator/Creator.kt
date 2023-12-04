package com.example.playlistmaker.creator

import android.app.Activity
import com.example.playlistmaker.app.App
import com.example.playlistmaker.data.player.impl.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.network.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.search.local.impl.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.settings.impl.ThemeSettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.AudioPlayerRepository
import com.example.playlistmaker.domain.settings.ThemeSettingsInteractor
import com.example.playlistmaker.domain.settings.ThemeSettingsRepository
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.search.TrackHistoryInteractor
import com.example.playlistmaker.domain.search.TrackHistoryRepository
import com.example.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.settings.impl.ThemeSettingsInteractorImpl
import com.example.playlistmaker.domain.search.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.search.impl.TrackHistoryInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.SharingRepository
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import com.example.playlistmaker.ui.search.view_model.TrackSearchViewModel
import com.example.playlistmaker.ui.search.adapters.HistoryTracksAdapter
import com.example.playlistmaker.ui.search.adapters.SearchTracksAdapter

/**
 * Объект Creator предоставляет методы для создания экземпляров интеракторов и репозиториев.
 * Кроме того, он инициализирует приложение через метод initApplication.
 */
object Creator {
    /**
     * Инициализация приложения.
     *
     * @param application экземпляр класса Application для инициализации.
     */
    private lateinit var app: App

    fun initApplication(application: App) {
        app = application
    }

    //Работа с аудиоплеером.
    /**
     * Создание экземпляра AudioPlayerInteractor.
     * @return AudioPlayerInteractor - объект для взаимодействия с аудиоплеером.
     */
    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(getAudioPlayerRepisitory())
    }

    /**
     * Создание экземпляра AudioPlayerRepository.
     * @return AudioPlayerRepository - объект для работы с аудиоплеером.
     */
    private fun getAudioPlayerRepisitory(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl()
    }

    //Работа с темой.
    /**
     * Создание экземпляра ThemeInteractor.
     * @return ThemeInteractor - объект для взаимодействия с настройками темы.
     */
    fun provideThemeSettingsInteractor(): ThemeSettingsInteractor {
        return ThemeSettingsInteractorImpl(getThemeSettingsRepository())
    }

    /**
     * Создание экземпляра ThemeRepository.
     * @return ThemeRepository - объект для работы с настройками темы.
     */
    private fun getThemeSettingsRepository(): ThemeSettingsRepository {
        return ThemeSettingsRepositoryImpl(app)
    }

    //Работа с треком
    fun provideTrackInteractor() : TrackInteractor {
        return TrackInteractorImpl(getTracksRepository())
    }
    /**
     * Получение репозитория для работы с треками.
     * @return TrackRepository - объект для работы с треками.
     */
    private fun getTracksRepository() : TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    //Работа с историей поиска
    fun provideTrackHistoryInteractor(): TrackHistoryInteractor {
        return TrackHistoryInteractorImpl(getTrackHistoryRepository())
    }
    /**
     * Получение репозитория для работы с историей поиска.
     * @return TrackHistoryRepository - объект для работы с историей поиска.
     */
    private fun getTrackHistoryRepository() : TrackHistoryRepository {
        return TrackHistoryRepositoryImpl(app)
    }

    //инициализацию контроллера в Creator:
    fun provideTracksSearchController(activity: Activity, searchAdapter: SearchTracksAdapter, historyAdapter: HistoryTracksAdapter): TrackSearchViewModel {
        return TrackSearchViewModel(activity, searchAdapter, historyAdapter)
    }

    //Работа по отправки инф.о приложении
    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getSharingRepository())
    }

    private fun getSharingRepository(): SharingRepository {
        return SharingRepositoryImpl(app)
    }

}

