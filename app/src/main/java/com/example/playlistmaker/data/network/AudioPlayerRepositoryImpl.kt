package com.example.playlistmaker.data.network

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.player.AudioPlayerRepository
import com.example.playlistmaker.domain.models.AudioPlayerProgressStatus
import com.example.playlistmaker.domain.models.AudioPlayerStatus
import com.example.playlistmaker.domain.models.Track

/**
 * Реализация интерфейса AudioPlayerRepository для работы с аудиоплеером.
 * Использует MediaPlayer для воспроизведения аудио.
 */
class AudioPlayerRepositoryImpl : AudioPlayerRepository {

    private var mediaPlayer = MediaPlayer()
    private var playerState = AudioPlayerStatus.STATE_DEFAULT

    /**
     * Подготовка аудиоплеера к воспроизведению трека.
     * @param track - трек, который необходимо воспроизвести.
     */
    override fun preparePlayer(track: Track) {
        try {
            mediaPlayer.setDataSource(track.previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = AudioPlayerStatus.STATE_PREPARED
            }
            mediaPlayer.setOnCompletionListener {
                playerState = AudioPlayerStatus.STATE_DEFAULT
            }
        } catch (e: Exception) {
            playerState = AudioPlayerStatus.STATE_ERROR
        }
    }

    /**
     * Запуск воспроизведения аудиоплеера.
     */
    override fun startPlayer() {
        mediaPlayer.start()
        playerState = AudioPlayerStatus.STATE_PLAYING
    }

    /**
     * Приостановка воспроизведения аудиоплеера.
     */
    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = AudioPlayerStatus.STATE_PAUSED
    }

    /**
     * Получение статуса и текущей позиции аудиоплеера.
     * @return AudioPlayerProgressStatus - объект, содержащий статус и текущую позицию.
     */
    override fun getAudioPlayerProgressStatus(): AudioPlayerProgressStatus {
        val currentPosition = mediaPlayer.currentPosition
        return when (playerState) {
            AudioPlayerStatus.STATE_PLAYING -> {
                AudioPlayerProgressStatus(
                    audioPlayerStatus = AudioPlayerStatus.STATE_PLAYING,
                    currentPosition = currentPosition
                )
            }

            AudioPlayerStatus.STATE_PAUSED -> {
                AudioPlayerProgressStatus(
                    audioPlayerStatus = AudioPlayerStatus.STATE_PAUSED,
                    currentPosition = currentPosition
                )
            }

            AudioPlayerStatus.STATE_PREPARED -> {
                AudioPlayerProgressStatus(
                    audioPlayerStatus = AudioPlayerStatus.STATE_PREPARED,
                    currentPosition = currentPosition
                )
            }

            AudioPlayerStatus.STATE_DEFAULT -> {
                AudioPlayerProgressStatus(
                    audioPlayerStatus = AudioPlayerStatus.STATE_DEFAULT,
                    currentPosition = currentPosition
                )
            }

            AudioPlayerStatus.STATE_ERROR -> {
                AudioPlayerProgressStatus(
                    audioPlayerStatus = AudioPlayerStatus.STATE_DEFAULT,
                    currentPosition = 0
                )
            }
        }
    }

    /**
     * Освобождение ресурсов аудиоплеера.
     */
    override fun destroyPlayer() {
        mediaPlayer.release()
        mediaPlayer = MediaPlayer()
    }
}