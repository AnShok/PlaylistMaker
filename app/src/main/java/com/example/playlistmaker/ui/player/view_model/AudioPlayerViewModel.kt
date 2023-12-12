package com.example.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.model.AudioPlayerProgressStatus
import com.example.playlistmaker.domain.player.model.AudioPlayerStatus
import com.example.playlistmaker.domain.search.model.Track

class AudioPlayerViewModel(private val audioPlayerInteractor: AudioPlayerInteractor) : ViewModel() {

    private val updateProgressHandler = Handler(Looper.getMainLooper())

    private val _audioPlayerProgressStatus: MutableLiveData<AudioPlayerProgressStatus> =
        MutableLiveData(updateAudioPlayerProgressStatus())
    val audioPlayerProgressStatus: LiveData<AudioPlayerProgressStatus> get() = _audioPlayerProgressStatus

    fun loadTrack(track: Track) {
        audioPlayerInteractor.preparePlayer(track)
        _audioPlayerProgressStatus.value = updateAudioPlayerProgressStatus()
    }
    fun pauseAudioPlayer() {
        audioPlayerInteractor.pausePlayer()
    }

    fun destroyMediaPlayer() {
        updateProgressHandler.removeCallbacks(updateProgressRunnable())
        audioPlayerInteractor.destroyPlayer()
    }

    fun playbackControl() {
        _audioPlayerProgressStatus.value = updateAudioPlayerProgressStatus()
        when (_audioPlayerProgressStatus.value!!.audioPlayerStatus) {
            AudioPlayerStatus.STATE_PLAYING -> pausePlayer()
            AudioPlayerStatus.STATE_PREPARED, AudioPlayerStatus.STATE_PAUSED -> startPlayer()
            AudioPlayerStatus.STATE_DEFAULT -> startPlayer()
            AudioPlayerStatus.STATE_ERROR -> {}
        }
    }

    private fun updateAudioPlayerProgressStatus(): AudioPlayerProgressStatus {
        return audioPlayerInteractor.getAudioPlayerProgressStatus()
    }

    // Обработчик для обновления времени воспроизведения
    private fun updateProgressRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                _audioPlayerProgressStatus.value = updateAudioPlayerProgressStatus()

                when (_audioPlayerProgressStatus.value!!.audioPlayerStatus) {
                    AudioPlayerStatus.STATE_PLAYING -> {
                        updateProgressHandler.postDelayed(this, 300)
                    }

                    AudioPlayerStatus.STATE_PAUSED -> {
                        updateProgressHandler.removeCallbacks(this)
                    }

                    else -> {
                        updateProgressHandler.removeCallbacks(this)
                    }
                }
            }
        }
    }

    // Метод для запуска воспроизведения
    private fun startPlayer() {
        audioPlayerInteractor.startPlayer()
        updateProgressHandler.post(updateProgressRunnable())
        _audioPlayerProgressStatus.value = updateAudioPlayerProgressStatus()
    }

    // Метод для паузы воспроизведения
    private fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        _audioPlayerProgressStatus.value = updateAudioPlayerProgressStatus()
    }
}