package com.example.playlistmaker.domain.db

import android.content.Context
import android.net.Uri
import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun addTrackToPlaylist(playList: Playlist, track: Track)

    suspend fun saveCoverToPrivateStorage(previewUri: Uri, context: Context): Uri?

    suspend fun getPlaylistById(playlistId: Int): Playlist

    suspend fun getAllTracks(tracksIdsList: List<Long>): List<Track>

    suspend fun trackCountDecrease(playlistId: Int)

    suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Long)

    suspend fun deletePlaylist(playlistId: Int)

    suspend fun modifyData(
        name: String,
        description: String,
        cover: String,
        coverUri: Uri?,
        originalPlayList: Playlist
    )

    suspend fun newPlaylist(playlistName: String, playlistDescription: String, coverUri: Uri?)

    suspend fun getCover(): String
}