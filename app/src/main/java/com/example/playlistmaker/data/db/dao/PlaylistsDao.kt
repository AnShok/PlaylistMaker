package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.data.db.PlaylistEntity
import com.example.playlistmaker.data.db.TrackToPlaylistEntity

@Dao
interface PlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayList(playList: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylist(track: TrackToPlaylistEntity)

    @Query("SELECT * FROM PLAYLISTS_TABLET")
    fun getAllPlayLists(): List<PlaylistEntity>

    @Update
    suspend fun updatePlayList(playList: PlaylistEntity)

    @Query("SELECT * FROM TRACKS_IN_PLAYLIST")
    fun getAllPlaylistTracks(): List<TrackToPlaylistEntity>

    @Query("DELETE FROM TRACKS_IN_PLAYLIST WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: Long)

    @Query("SELECT * FROM PLAYLISTS_TABLET WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Int): PlaylistEntity

    @Query("UPDATE PLAYLISTS_TABLET SET tracksAmount = tracksAmount - 1 WHERE id = :playlistId")
    suspend fun trackCountDecrease(playlistId: Int)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)
}