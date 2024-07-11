package com.example.playlistmaker.data.impl.db

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.PlaylistEntity
import com.example.playlistmaker.data.db.TrackToPlaylistEntity
import com.example.playlistmaker.data.db.converters.PlaylistsDbConverter
import com.example.playlistmaker.data.db.converters.TracksToPlaylistConverter
import com.example.playlistmaker.domain.db.PlaylistsRepository
import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.util.Date
import java.util.UUID

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistsDbConverter: PlaylistsDbConverter,
    private val tracksToPlaylistConverter: TracksToPlaylistConverter
) : PlaylistsRepository {
    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistsDao().getAllPlayLists()
        emit(playlists.map { playlistsDbConverter.map(it) })
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistsDao().insertPlayList(playlistsDbConverter.map(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistsDao().updatePlayList(playlistsDbConverter.map(playlist))
    }

    override suspend fun addTrackToPlaylist(playList: Playlist, track: Track) {
        val trackId = track.trackId?.toLong() ?: throw IllegalArgumentException(
            ILLEGAL_ARGUMENT_TRACK_ID
        )
        if (trackId == 0L) {
            throw IllegalArgumentException(NULL_ARGUMENT_TRACK_ID)
        }
        playList.tracks.add(trackId)
        playList.tracksAmount = playList.tracks.size
        updatePlaylist(playList)
        val addTime = Date().time
        appDatabase.playlistsDao().addTrackToPlaylist(tracksToPlaylistConverter.map(track, addTime))
    }

    override suspend fun saveCoverToPrivateStorage(previewUri: Uri, context: Context): Uri? {
        val path =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), STORAGE_DIR_NAME)
        if (!path.exists()) {
            path.mkdirs()
        }
        val file = File(path, "playlist_cover_${System.currentTimeMillis()}.jpg")
        val input = context.contentResolver.openInputStream(previewUri)
        val output = FileOutputStream(file)
        BitmapFactory
            .decodeStream(input)
            .compress(Bitmap.CompressFormat.JPEG, 30, output)
        val previewUri = Uri.fromFile(file)
        return previewUri
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return converterForPlaylistEntity(
            appDatabase.playlistsDao().getPlaylistById(playlistId)
        )
    }

    override suspend fun getAllTracks(tracksIdsList: List<Long>): List<Track> {
        val playlist = appDatabase.playlistsDao().getAllPlaylistTracks()
        return playlist
            .filter { it.trackId?.toLong() in tracksIdsList }
            .sortedByDescending { it.insertTime}
            .map { convertFromTrackEntity(it) }
    }

    override suspend fun trackCountDecrease(playlistId: Int) {
        appDatabase.playlistsDao().trackCountDecrease(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Long) {
        val playlist = getPlaylistById(playlistId)
        playlist.tracks.remove(trackId)
        updatePlaylist(playlist)
        if (!checkTrackInAnyPLaylist(trackId)) {
            deleteTrackIfNoMatch(trackId)
        }

    }

    override suspend fun deletePlaylist(playlistId: Int) {
        val playlist = getPlaylistById(playlistId)
        appDatabase.playlistsDao().deletePlaylist(playlistsDbConverter.map(playlist))
    }

    override suspend fun newPlaylist(
        playlistName: String,
        playlistDescription: String,
        coverUri: Uri?
    ) {
        val coverPath = getCover()
        val playlist = Playlist(
            id = 0,
            name = playlistName,
            description = playlistDescription,
            coverPath = coverPath,
            tracksIds = "",
            tracks = arrayListOf(),
            tracksAmount = 0,
            imageUri = coverUri?.toString() ?: ""
        )
        addPlaylist(playlist)
    }

    override suspend fun modifyData(
        name: String,
        description: String,
        cover: String,
        coverUri: Uri?,
        originalPlayList: Playlist
    ) {
        updatePlaylist(
            Playlist(
                id = originalPlayList.id,
                name = name,
                description = description,
                coverPath = cover,
                tracksIds = originalPlayList.tracksIds,
                tracksAmount = originalPlayList.tracksAmount,
                tracks = originalPlayList.tracks,
                imageUri = coverUri?.toString() ?: originalPlayList.imageUri
            )
        )
    }

    override suspend fun getCover(): String {
        return "cover_${UUID.randomUUID()}.jpg"
    }

    private fun converterFromPlaylistEntity(playlist: List<PlaylistEntity>): List<Playlist> {
        return playlist.map { playlists -> playlistsDbConverter.map(playlists) }
    }

    private fun converterForPlaylistEntity(playlist: PlaylistEntity): Playlist {
        return playlistsDbConverter.map(playlist)
    }

    private fun convertFromTrackEntity(trackEntity: TrackToPlaylistEntity): Track {
        val addTime = Date().time
        return tracksToPlaylistConverter.map(trackEntity, addTime)
    }

    private fun checkTrackInAnyPLaylist(trackId: Long): Boolean {
        val anyPlaylists = appDatabase.playlistsDao().getAllPlayLists()
        for (playlist in anyPlaylists) {
            if (trackId in playlist.tracks) {
                return true
            }
        }
        return false
    }

    private suspend fun deleteTrackIfNoMatch(trackId: Long) {
        appDatabase.playlistsDao().deleteTrackById(trackId)
    }

    companion object {
        const val ILLEGAL_ARGUMENT_TRACK_ID = "Track ID must not be null or  0"

        const val STORAGE_DIR_NAME = "myPics"
        const val NULL_ARGUMENT_TRACK_ID = "Track ID must not be 0"
    }
}
