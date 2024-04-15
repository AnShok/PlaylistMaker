package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.player.model.AudioPlayerProgressStatus
import com.example.playlistmaker.domain.search.model.Track

/**
 * Интерфейс для работы с аудиоплеером.
 */
interface AudioPlayerRepository {
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun getAudioPlayerProgressStatus(): AudioPlayerProgressStatus
    fun destroyPlayer()
    fun setOnPreparedListener(listener: (() -> Unit)?)
    fun setOnCompletionListener(listener: (() -> Unit)?)
    fun reset()
    fun playerCheck(): Boolean
    fun getCurrentPosition(): Int
}