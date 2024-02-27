package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.db.models.FavoriteTracksStatus
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.model.AudioPlayerProgressStatus
import com.example.playlistmaker.domain.player.model.AudioPlayerStatus
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {

    private val _favoriteState = MutableLiveData<FavoriteTracksStatus>()
    val favoriteState: LiveData<FavoriteTracksStatus> = _favoriteState

    private val _audioPlayerProgressStatus: MutableLiveData<AudioPlayerProgressStatus> =
        MutableLiveData(updateAudioPlayerProgressStatus())
    val audioPlayerProgressStatus: LiveData<AudioPlayerProgressStatus> get() = _audioPlayerProgressStatus

    private var timerJob: Job? = null

    fun loadTrack(track: Track) {
        audioPlayerInteractor.preparePlayer(track)
        _audioPlayerProgressStatus.value = updateAudioPlayerProgressStatus()
    }

    fun pauseAudioPlayer() {
        audioPlayerInteractor.pausePlayer()
        timerJob?.cancel()
    }

    fun destroyMediaPlayer() {
        audioPlayerInteractor.destroyPlayer()
        timerJob?.cancel()
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

    // Метод для запуска воспроизведения и отсчета таймера
    private fun startPlayer() {
        audioPlayerInteractor.startPlayer()
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                _audioPlayerProgressStatus.value = updateAudioPlayerProgressStatus()

                when (_audioPlayerProgressStatus.value!!.audioPlayerStatus) {
                    AudioPlayerStatus.STATE_PLAYING -> {
                        delay(TIMER_DELAY)
                    }

                    AudioPlayerStatus.STATE_PAUSED -> {
                        break
                    }

                    else -> {
                        break
                    }
                }
            }
        }
    }

    // Метод для паузы воспроизведения
    private fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
    }

    fun isFavorite(track: Track) {
        viewModelScope.launch {
            favoriteTracksInteractor.isFavoriteTrack(track.trackId ?: 0)
                .collect { isFavorite ->
                    _favoriteState.postValue(FavoriteTracksStatus(isFavorite))
                }
        }
    }

    fun clickOnFavorite(track: Track) {
        viewModelScope.launch {
            val currentFavoriteState = _favoriteState.value?.isFavorite ?: false
            if (currentFavoriteState) {
                favoriteTracksInteractor.deleteTrack(track.trackId ?: 0)
            } else {
                favoriteTracksInteractor.additionTrack(track)
            }
            _favoriteState.postValue(FavoriteTracksStatus(!currentFavoriteState))
        }
    }


    companion object {
        // Задержка для избегания многократных кликов (в миллисекундах)
        private const val TIMER_DELAY = 300L
    }
}