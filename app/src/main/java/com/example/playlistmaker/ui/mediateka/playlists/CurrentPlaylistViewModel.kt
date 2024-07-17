package com.example.playlistmaker.ui.mediateka.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistsInteractor
import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrentPlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private lateinit var tracks: List<Track>


    private val _playlistId = MutableLiveData<Playlist>()
    fun observePlaylistId(): LiveData<Playlist> = _playlistId

    private val _playlistTracks = MutableLiveData<List<Track>>()
    fun observePlaylistTracks(): LiveData<List<Track>> = _playlistTracks

    private val _trackCount = MutableLiveData<Int>()
    fun observeTrackCount(): LiveData<Int> = _trackCount

    private val _playlistTime = MutableLiveData<Long>()
    fun observePlaylistAllTime(): LiveData<Long> = _playlistTime


    fun playlistAllTime() {
        _playlistTracks.value?.let { tracks ->
            val totalTime = tracks.sumOf { it.trackTimeMillis ?: 0 }
            _playlistTime.postValue(totalTime)
        }
    }

    fun getPlaylistById(playlistId: Int) {
        viewModelScope.launch {
            _playlistId.postValue(playlistsInteractor.getPlaylistById(playlistId))
        }
    }

    fun getAllTracks(playlistId: List<Long>) {
        viewModelScope.launch(Dispatchers.IO) {
            val tracks = playlistsInteractor.getAllTracks(playlistId)
            _playlistTracks.postValue(tracks)
        }
    }

    fun deleteTrackFromPlaylist(playlist: Playlist, trackId: Long) {
        val playlistId = playlist.id
        viewModelScope.launch(Dispatchers.IO) {
            playlist.tracks.remove(trackId)
            playlist.tracksAmount = playlist.tracks.size
            playlistsInteractor.updatePlaylist(playlist)
            playlistsInteractor.deleteTrackFromPlaylist(playlistId, trackId)
            updatePlaylist(playlistId)
        }
    }

    fun deletePlaylist() {
        _playlistId.value?.let { playlist ->
            viewModelScope.launch(Dispatchers.IO) {
                playlistsInteractor.deletePlaylist(playlist.id)
            }
        }
    }

    private fun updatePlaylist(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedPlaylist = playlistsInteractor.getPlaylistById(playlistId)
            _playlistId.postValue(updatedPlaylist)
            _playlistTracks.postValue(playlistsInteractor.getAllTracks(updatedPlaylist.tracks))
            _trackCount.postValue(updatedPlaylist.tracks.size)
        }
    }

    private suspend fun trackCountDecrease(playlistId: Int) {
        playlistsInteractor.trackCountDecrease(playlistId)
    }
}