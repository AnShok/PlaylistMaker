package com.example.playlistmaker.ui.mediateka.playlists

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistsInteractor
import com.example.playlistmaker.domain.search.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ModifyPlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) :
    NewPlaylistViewModel(playlistsInteractor) {

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> get() = _playlist

    fun getPlaylist(playlist: Playlist) {
        _playlist.postValue(playlist)
    }

    fun getCover() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor
                .getPlaylists()
                .collect { playlists ->
                    playlists.map { playlist ->
                        playlist.copy(
                            imageUri = Uri.parse(playlist.imageUri).toString()
                        )
                    }
                }
        }
    }

    fun modifyData(
        name: String,
        description: String,
        cover: String,
        coverUri: Uri?,
        originalPlayList: Playlist
    ) {
        viewModelScope.launch {
            playlistsInteractor.modifyData(
                name,
                description,
                cover,
                coverUri,
                originalPlayList
            )
        }
    }
}