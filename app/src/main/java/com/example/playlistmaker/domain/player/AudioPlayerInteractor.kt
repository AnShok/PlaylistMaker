package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.player.model.AudioPlayerProgressStatus
import com.example.playlistmaker.domain.search.model.Track

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