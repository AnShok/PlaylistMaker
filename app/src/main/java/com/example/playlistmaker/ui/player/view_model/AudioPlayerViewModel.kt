package com.example.playlistmaker.ui.player.view_model

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.db.PlaylistsInteractor
import com.example.playlistmaker.domain.db.models.FavoriteTracksStatus
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.model.AudioPlayerProgressStatus
import com.example.playlistmaker.domain.player.model.AudioPlayerStatus
import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.domain.search.model.PlaylistState
import com.example.playlistmaker.domain.search.model.Track
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playlistsInteractor: PlaylistsInteractor,
    private val firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {

    private val _favoriteState = MutableLiveData<FavoriteTracksStatus>()
    val favoriteState: LiveData<FavoriteTracksStatus> = _favoriteState

    private val _playlistsState = MutableStateFlow<PlaylistState>(PlaylistState.Empty)
    val playlistState: StateFlow<PlaylistState> = _playlistsState

    private val _audioPlayerProgressStatus = MutableLiveData(
        AudioPlayerProgressStatus(
            audioPlayerStatus = AudioPlayerStatus.STATE_DEFAULT,
            currentPosition = 0
        )
    )
    val audioPlayerProgressStatus: LiveData<AudioPlayerProgressStatus> = _audioPlayerProgressStatus

    private var timerJob: Job? = null
    private var isFavoriteTrack: Boolean = false

    private fun setState(audioPlayerStatus: AudioPlayerStatus) {
        _audioPlayerProgressStatus.postValue(audioPlayerProgressStatus.value?.copy(audioPlayerStatus = audioPlayerStatus))
    }

    private fun setTimer() {
        timerJob = viewModelScope.launch {
            while (audioPlayerInteractor.playerCheck()) {
                delay(TIMER_DELAY)
                _audioPlayerProgressStatus.postValue(
                    _audioPlayerProgressStatus.value?.copy(
                        currentPosition = audioPlayerInteractor.getCurrentPosition()
                    )
                )
            }
        }
    }

    fun isFavorite(track: Track) {
        viewModelScope.launch {
            favoriteTracksInteractor
                .isFavoriteTrack(track.trackId ?: 0)
                .collect { isFavorite ->
                    isFavoriteTrack = isFavorite
                    _favoriteState.postValue(FavoriteTracksStatus(isFavorite))

                }
        }
    }

    fun inPlaylist(playlist: Playlist, trackId: Long): Boolean {
        var data = false
        for (track in playlist.tracks) {
            if (track == trackId) data = true
        }
        return data
    }

    fun clickOnAddtoPlaylist(playlist: Playlist, track: Track) {
        viewModelScope.launch {
            playlistsInteractor.addTrackToPlaylist(playlist, track)
            getPlaylists()  // Обновляем списки плейлистов после добавления трека
        }
    }

    fun getPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getPlaylists().collect { playlists ->
                if (playlists.isEmpty()) {
                    _playlistsState.value = PlaylistState.Empty
                } else {
                    _playlistsState.value = PlaylistState.Data(playlists)
                }
            }
        }
    }

    fun clickOnFavorite(track: Track) {
        viewModelScope.launch {
            if (isFavoriteTrack) {
                favoriteTracksInteractor.deleteTrack(track.trackId ?: 0)
                _favoriteState.postValue(FavoriteTracksStatus(false))
                isFavoriteTrack = false
            } else {
                favoriteTracksInteractor.additionTrack(track)
                _favoriteState.postValue(FavoriteTracksStatus(true))
                isFavoriteTrack = true

                val bundle = Bundle().apply {
                    putString(FirebaseAnalytics.Param.ITEM_NAME, track.trackName)
                    putString(FirebaseAnalytics.Param.ITEM_ID, track.trackId.toString())
                }
                firebaseAnalytics.logEvent("add_to_favorites", bundle)
            }
        }
    }

    fun onPause() {
        audioPlayerInteractor.pausePlayer()
        timerJob?.cancel()
        setState(audioPlayerStatus = AudioPlayerStatus.STATE_PAUSED)
    }

    fun onDestroy() {
        setState(AudioPlayerStatus.STATE_DEFAULT)
        timerJob?.cancel()
        audioPlayerInteractor.reset()
    }

    override fun onCleared() {
        super.onCleared()
        setState(AudioPlayerStatus.STATE_DEFAULT)
        timerJob?.cancel()
        audioPlayerInteractor.reset()
    }

    fun setPlayer(track: Track) {
        audioPlayerInteractor.preparePlayer(track)
        audioPlayerInteractor.setOnPreparedListener {
            setState(AudioPlayerStatus.STATE_PREPARED)
        }
        audioPlayerInteractor.setOnCompletionListener {
            timerJob?.cancel()
            _audioPlayerProgressStatus.postValue(
                _audioPlayerProgressStatus.value?.copy(
                    currentPosition = 0,
                    audioPlayerStatus = AudioPlayerStatus.STATE_PREPARED
                )
            )
        }
    }

    fun playControl() {
        if (audioPlayerInteractor.playerCheck()) {
            audioPlayerInteractor.pausePlayer()
            setState(AudioPlayerStatus.STATE_PAUSED)

        } else {
            audioPlayerInteractor.startPlayer()
            setState(AudioPlayerStatus.STATE_PLAYING)
            setTimer()
        }
    }

    companion object {
        // Задержка для избегания многократных кликов (в миллисекундах)
        private const val TIMER_DELAY = 300L
    }
}
