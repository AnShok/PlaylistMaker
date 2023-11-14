package com.example.playlistmaker.domain.impl.player

import com.example.playlistmaker.domain.api.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.player.AudioPlayerRepository
import com.example.playlistmaker.domain.models.AudioPlayerProgressStatus
import com.example.playlistmaker.domain.models.Track

/**
 * Реализация интерфейса AudioPlayerInteractor для взаимодействия с аудиоплеером.
 * Использует AudioPlayerRepository для выполнения действий с аудиоплеером.
 */
class AudioPlayerInteractorImpl(private val audioPlayerRepository: AudioPlayerRepository) :
    AudioPlayerInteractor {
    override fun preparePlayer(track: Track) {
        audioPlayerRepository.preparePlayer(track)
    }

    override fun startPlayer() {
        audioPlayerRepository.startPlayer()
    }

    override fun pausePlayer() {
        audioPlayerRepository.pausePlayer()
    }

    override fun getAudioPlayerProgressStatus(): AudioPlayerProgressStatus {
        return audioPlayerRepository.getAudioPlayerProgressStatus()
    }

    override fun destroyPlayer() {
        audioPlayerRepository.destroyPlayer()
    }

}