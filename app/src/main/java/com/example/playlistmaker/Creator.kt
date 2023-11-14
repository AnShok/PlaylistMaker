package com.example.playlistmaker

import com.example.playlistmaker.data.network.AudioPlayerRepositoryImpl
import com.example.playlistmaker.domain.api.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.player.AudioPlayerRepository
import com.example.playlistmaker.domain.impl.player.AudioPlayerInteractorImpl

object Creator {
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
}
