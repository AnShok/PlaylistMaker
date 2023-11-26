package com.example.playlistmaker.domain.api.player

import com.example.playlistmaker.domain.models.AudioPlayerProgressStatus
import com.example.playlistmaker.domain.models.Track

/**
 * Интерфейс для взаимодействия с аудиоплеером.
 */
interface AudioPlayerInteractor {
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun getAudioPlayerProgressStatus(): AudioPlayerProgressStatus
    fun destroyPlayer()
}