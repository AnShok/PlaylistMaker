package com.example.playlistmaker

import android.app.Activity
import com.example.playlistmaker.data.network.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TrackRepositoryImpl
import com.example.playlistmaker.data.repository.ThemeRepositoryImpl
import com.example.playlistmaker.data.repository.TrackHistoryRepositoryImpl
import com.example.playlistmaker.domain.api.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.player.AudioPlayerRepository
import com.example.playlistmaker.domain.api.settings.ThemeInteractor
import com.example.playlistmaker.domain.api.settings.ThemeRepository
import com.example.playlistmaker.domain.api.track.TrackInteractor
import com.example.playlistmaker.domain.api.track.TrackRepository
import com.example.playlistmaker.domain.api.track_history.TrackHistoryInteractor
import com.example.playlistmaker.domain.api.track_history.TrackHistoryRepository
import com.example.playlistmaker.domain.impl.player.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.theme_settings.ThemeInteractorImpl
import com.example.playlistmaker.domain.impl.track.TrackInteractorImpl
import com.example.playlistmaker.domain.impl.track_history.TrackHistoryInteractorImpl
import com.example.playlistmaker.presentation.activities.search.TracksSearchController
import com.example.playlistmaker.presentation.adapters.HistoryTracksAdapter
import com.example.playlistmaker.presentation.adapters.SearchTracksAdapter

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
        this.app = application
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
    fun provideThemeInteractor(): ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository())
    }

    /**
     * Создание экземпляра ThemeRepository.
     * @return ThemeRepository - объект для работы с настройками темы.
     */
    private fun getThemeRepository(): ThemeRepository {
        return ThemeRepositoryImpl(app)
    }

    //Работа с треком
    fun provideTrackInteractor() : TrackInteractor {
        return TrackInteractorImpl(getTracksRepository())
    }
    private fun getTracksRepository() : TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    //Работа с историей поиска
    fun provideTrackHistoryInteractor(): TrackHistoryInteractor {
        return TrackHistoryInteractorImpl(getTrackHistoryRepository())
    }
    private fun getTrackHistoryRepository() : TrackHistoryRepository {
        return TrackHistoryRepositoryImpl(app)
    }

    //Добавим инициализацию контроллера в Creator:
    fun provideTracksSearchController(activity: Activity, searchAdapter: SearchTracksAdapter, historyAdapter: HistoryTracksAdapter): TracksSearchController {
        return TracksSearchController(activity, searchAdapter, historyAdapter)
    }

}

