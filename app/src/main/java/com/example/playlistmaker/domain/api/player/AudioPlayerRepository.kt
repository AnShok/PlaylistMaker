package com.example.playlistmaker.domain.api.player

import com.example.playlistmaker.domain.models.AudioPlayerProgressStatus
import com.example.playlistmaker.domain.models.Track

/**
 * Интерфейс для работы с аудиоплеером.
 */
interface AudioPlayerRepository {
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun getAudioPlayerProgressStatus(): AudioPlayerProgressStatus
    fun destroyPlayer()
}