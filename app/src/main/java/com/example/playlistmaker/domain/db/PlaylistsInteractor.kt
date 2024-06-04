package com.example.playlistmaker.domain.db

import android.content.Context
import android.net.Uri
import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun addTrackToPlaylist(playList: Playlist, track: Track)

    suspend fun saveCoverToPrivateStorage(previewUri: Uri, context: Context): Uri?

    suspend fun getPlaylistById(playlistId: Int): Playlist

    suspend fun deletePlaylist(playlistId: Int)

    suspend fun newPlaylist(playlistName: String, playlistDescription: String, coverUri: Uri?)

    suspend fun getCover(): String

}